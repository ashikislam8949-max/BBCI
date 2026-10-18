package com.example.data.sbtech

data class SbTechCredentials(
    val websiteNote: String = "https://sbtech.sa",
    val isLiveProduction: Boolean = false,
    val lastHandshakeTime: Long = 0L
)

object SbTechEmbedGenerator {
    fun generateHtmlEmbedSnippet(credentials: SbTechCredentials): String {
        return """
<!-- SBTECH Investor Desk. Not a government login or filing widget. -->
<p>Independent notes for ${credentials.websiteNote}. Not affiliated with the Ministry of Investment (MISA) or any government entity.</p>
""".trimIndent()
    }
}
