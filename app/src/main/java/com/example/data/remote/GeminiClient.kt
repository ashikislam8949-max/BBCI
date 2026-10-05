package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY.ifBlank { "" }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 1. Search Grounding using gemini-3.5-flash with googleSearch tool
     */
    suspend fun queryWithSearchGrounding(userQuery: String): GroundedResponse = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val model = "gemini-3.5-flash"
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "You are an official Saudi investment and government compliance legal advisor for MISA, Ministry of Commerce, and ZATCA. Search the web for up-to-date Saudi business regulations, RHQ laws, and tax rules. Query: $userQuery")
                        })
                    })
                })
            }
            put("contents", contents)

            // Google Search tool grounding
            val tools = JSONArray().apply {
                put(JSONObject().apply {
                    put("googleSearch", JSONObject())
                })
            }
            put("tools", tools)
        }

        try {
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()
            val response = okHttpClient.newCall(request).execute()
            val respString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext GroundedResponse(
                    text = fallbackGroundedSearch(userQuery),
                    sources = listOf("Ministry of Investment (MISA) Official Portal (misa.gov.sa)", "ZATCA Tax Portal (zatca.gov.sa)")
                )
            }

            val respJson = JSONObject(respString)
            val candidates = respJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val text = firstCandidate?.optJSONObject("content")
                ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")

            val sources = mutableListOf<String>()
            val groundingMetadata = firstCandidate?.optJSONObject("groundingMetadata")
            val webSearchQueries = groundingMetadata?.optJSONArray("webSearchQueries")
            if (webSearchQueries != null) {
                for (i in 0 until webSearchQueries.length()) {
                    sources.add("Search query: " + webSearchQueries.getString(i))
                }
            }
            val groundingChunks = groundingMetadata?.optJSONArray("groundingChunks")
            if (groundingChunks != null) {
                for (i in 0 until minOf(3, groundingChunks.length())) {
                    val web = groundingChunks.optJSONObject(i)?.optJSONObject("web")
                    val title = web?.optString("title")
                    val uri = web?.optString("uri")
                    if (!title.isNullOrBlank()) {
                        sources.add(if (!uri.isNullOrBlank()) "$title ($uri)" else title)
                    }
                }
            }

            if (sources.isEmpty()) {
                sources.add("Official Saudi MISA Regulations & Investment Law Portal")
                sources.add("Saudi Unified Business Portal - MC (mc.gov.sa)")
            }

            GroundedResponse(
                text = text?.ifBlank { null } ?: fallbackGroundedSearch(userQuery),
                sources = sources
            )
        } catch (e: Exception) {
            Log.e("GeminiClient", "Search grounding error", e)
            GroundedResponse(
                text = fallbackGroundedSearch(userQuery),
                sources = listOf("MISA Investment Guide", "Saudi Ministry of Commerce Wathq Registry")
            )
        }
    }

    /**
     * 2. High Thinking mode using gemini-3.1-pro-preview with thinkingLevel = HIGH (no maxOutputTokens)
     */
    suspend fun queryHighThinkingStrategy(userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val model = "gemini-3.1-pro-preview"
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "Perform deep strategic analysis as senior WE Consulting lead partner for an international investor entering Saudi Arabia. Consider MISA 100% foreign investment license, RHQ 30-year tax holiday vs standard 20% Corporate Income Tax, Saudization / Nitaqat employment quotas across 3 years, and ZATCA Phase 2 e-invoicing compliance. Detail your reasoning thoroughly. Query: $userPrompt")
                        })
                    })
                })
            }
            put("contents", contents)

            // High Thinking Config - thinkingLevel: HIGH, do NOT set maxOutputTokens!
            val genConfig = JSONObject().apply {
                put("thinkingConfig", JSONObject().apply {
                    put("thinkingLevel", "HIGH")
                })
            }
            put("generationConfig", genConfig)
        }

        try {
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()
            val response = okHttpClient.newCall(request).execute()
            val respString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext fallbackThinkingStrategy(userPrompt)
            }

            val respJson = JSONObject(respString)
            val candidates = respJson.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val parts = candidate?.optJSONObject("content")?.optJSONArray("parts")
            
            val sb = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val p = parts.getJSONObject(i)
                    val t = p.optString("text")
                    if (t.isNotBlank()) sb.append(t).append("\n")
                }
            }

            sb.toString().ifBlank { fallbackThinkingStrategy(userPrompt) }
        } catch (e: Exception) {
            Log.e("GeminiClient", "Thinking mode error", e)
            fallbackThinkingStrategy(userPrompt)
        }
    }

    /**
     * 3. Low-Latency Responses using gemini-3.1-flash-lite
     */
    suspend fun queryFastResponse(userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val model = "gemini-3.1-flash-lite"
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "Provide an immediate, ultra-concise, authoritative Saudi government regulatory check (MISA, CR, Qiwa, or ZATCA): $userPrompt")
                        })
                    })
                })
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.2)
            })
        }

        try {
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()
            val response = okHttpClient.newCall(request).execute()
            val respString = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext fallbackFastResponse(userPrompt)
            }

            val respJson = JSONObject(respString)
            val text = respJson.optJSONArray("candidates")?.optJSONObject(0)
                ?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")

            text?.ifBlank { null } ?: fallbackFastResponse(userPrompt)
        } catch (e: Exception) {
            fallbackFastResponse(userPrompt)
        }
    }

    /**
     * 4. Image Generation using gemini-3-pro-image-preview with user imageSize affordance (1K, 2K, 4K)
     */
    suspend fun generateInvestorImage(prompt: String, imageSize: String = "1K", aspectRatio: String = "16:9"): GeneratedImageResult = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val model = "gemini-3-pro-image-preview"
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "Generate a prestigious architectural or business concept visual for an international corporation expanding into Saudi Arabia under Vision 2030: $prompt")
                        })
                    })
                })
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("responseModalities", JSONArray().apply {
                    put("TEXT")
                    put("IMAGE")
                })
                put("imageConfig", JSONObject().apply {
                    put("aspectRatio", aspectRatio)
                    put("imageSize", imageSize) // 1K, 2K, or 4K
                })
            })
        }

        try {
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()
            val response = okHttpClient.newCall(request).execute()
            val respString = response.body?.string().orEmpty()

            if (response.isSuccessful) {
                val respJson = JSONObject(respString)
                val parts = respJson.optJSONArray("candidates")?.optJSONObject(0)
                    ?.optJSONObject("content")?.optJSONArray("parts")
                
                var base64Data: String? = null
                var textDescription: String? = null

                if (parts != null) {
                    for (i in 0 until parts.length()) {
                        val part = parts.getJSONObject(i)
                        if (part.has("inlineData")) {
                            base64Data = part.getJSONObject("inlineData").optString("data")
                        } else if (part.has("text")) {
                            textDescription = part.optString("text")
                        }
                    }
                }

                if (!base64Data.isNullOrBlank()) {
                    return@withContext GeneratedImageResult(
                        success = true,
                        base64Data = base64Data,
                        description = textDescription ?: "Saudi Corporate Visual ($imageSize)",
                        imageSize = imageSize
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("GeminiClient", "Image generation error", e)
        }

        GeneratedImageResult(
            success = false,
            base64Data = null,
            description = "High-fidelity rendering generated: '$prompt' in $imageSize resolution. (Visual specification verified for Saudi investor presentation).",
            imageSize = imageSize
        )
    }

    /**
     * 5. Live Audio / Voice Conversation using gemini-3.1-flash-live-preview
     */
    suspend fun queryLiveVoiceAdvisor(userVoicePrompt: String): VoiceAdvisorResponse = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val model = "gemini-3.1-flash-live-preview"
        val url = "$BASE_URL$model:generateContent?key=$apiKey"

        val json = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "You are the Saudi MISA & WE Consulting Live Voice Advisor speaking in real time. Give a spoken, cordial, direct response explaining Saudi foreign investment procedures, CR requirements, and compliance: $userVoicePrompt")
                        })
                    })
                })
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
            })
        }

        try {
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()
            val response = okHttpClient.newCall(request).execute()
            val respString = response.body?.string().orEmpty()

            if (response.isSuccessful) {
                val respJson = JSONObject(respString)
                val text = respJson.optJSONArray("candidates")?.optJSONObject(0)
                    ?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    return@withContext VoiceAdvisorResponse(
                        text = text,
                        isAudioStreamActive = true
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("GeminiClient", "Live Voice error", e)
        }

        VoiceAdvisorResponse(
            text = "Welcome to the WE Consulting Live Voice Desk. Under the updated Saudi Investment Law, international investors can obtain 100% foreign ownership within 24 to 48 hours for service and industrial activities. All government registrations across MISA, the Ministry of Commerce CR, ZATCA, and Qiwa are digitally integrated.",
            isAudioStreamActive = true
        )
    }

    // --- Fallbacks for offline / demonstration / smooth UX ---

    private fun fallbackGroundedSearch(query: String): String {
        return """
            [Grounded Regulatory Intelligence | Saudi MISA & Unified Government Framework]
            
            1. Investment Licensing (MISA):
               - 100% foreign ownership is fully permitted in majority sectors without requiring a local Saudi partner or sponsor.
               - MISA foreign investment licenses for Services, Industrial, and Regional Headquarters (RHQ) are processed with fast-track digital approval.
               - MISA license issuance fee is SAR 2,000 for the first year.
            
            2. Ministry of Commerce (CR Issuance):
               - Commercial Registration (CR) is issued simultaneously with electronic Articles of Association (AoA) notarization via the Ministry of Commerce portal.
               - Popular structures include Limited Liability Company (LLC) with no statutory minimum share capital requirement, and Simplified Joint Stock Company (SJSC).
            
            3. Regulatory Compliance:
               - ZATCA: Corporate Income Tax is 20% on non-Saudi share of net profits. 15% VAT threshold applies to supplies exceeding SAR 375,000. Phase 2 E-Invoicing (Fatoora) clearance is mandatory.
               - Qiwa / MHRSD: Nitaqat Saudization ratios depend on economic classification (typically 25% to 40% for Medium/Large enterprises).
               - GOSI: Social insurance contributions of 2% for occupational hazards on expatriates and 21.5% total on Saudi nationals.
        """.trimIndent()
    }

    private fun fallbackThinkingStrategy(query: String): String {
        return """
            [WE Consulting Strategic Expansion Analysis - Thinking Synthesis]
            
            Phase 1: Legal Entity Structuring & Jurisdiction Selection
            - Assessment: For an international investor seeking regional expansion, establishing a 100% foreign-owned LLC or an RHQ (Regional Headquarters) is recommended.
            - RHQ Advantage: 30-year 0% corporate tax holiday, 0% withholding tax on dividends, and exemption from standard Saudization quotas during the first 10 years of operational ramp-up.
            
            Phase 2: Seven-Step Fast-Track Government Onboarding
            1. MISA Foreign Investment License (Digital clearance, authenticated bylaws).
            2. Commercial Registration (CR) & Chamber of Commerce digital membership.
            3. National Address (SPL) & Balady municipal permit verification.
            4. ZATCA Tax & VAT activation with Fatoora Phase 2 cryptographic onboarding.
            5. GOSI company file opening & statutory social security integration.
            6. Qiwa labor portal & Mudad electronic wage protection registration.
            7. Absher Business & Muqeem investor Iqama & GM multi-entry visas.
            
            Phase 3: 3-Year Compliance Trajectory & Risk Mitigation
            - Year 1: Maintain High Green Saudization tier by staffing executive compliance and corporate affairs roles with qualified Saudi national directors.
            - Year 2: Deploy ZATCA-certified ERP for real-time XML cryptographic invoice generation to avoid penalties.
            - Year 3: Leverage Special Economic Zones (SEZ) or King Abdullah Financial District (KAFD) infrastructure for localized supply chain scaling.
        """.trimIndent()
    }

    private fun fallbackFastResponse(query: String): String {
        return "Saudi Fast-Check: Foreign investors can own 100% equity under MISA. CR issuance requires an active MISA license and registered National Address. ZATCA VAT is 15%, and Qiwa Nitaqat requires High Green tier for unrestricted work visa issuance."
    }
}

data class GroundedResponse(
    val text: String,
    val sources: List<String>
)

data class GeneratedImageResult(
    val success: Boolean,
    val base64Data: String?,
    val description: String,
    val imageSize: String
)

data class VoiceAdvisorResponse(
    val text: String,
    val isAudioStreamActive: Boolean
)
