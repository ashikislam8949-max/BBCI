package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark

@Composable
fun AiRegulatorySuiteScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedAiMode by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Live Voice" to Icons.Default.Mic,
        "Search Grounding" to Icons.Default.Public,
        "High Thinking" to Icons.Default.Psychology,
        "Concept Visuals" to Icons.Default.Image,
        "Fast Check" to Icons.Default.Bolt
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SaudiBrandHeader(
                title = "AI Regulatory Intelligence Suite",
                subtitle = "Multimodal Gemini intelligence for Saudi investment & statutory compliance"
            )
        }

        // Subtabs for the AI capabilities
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedAiMode,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, (title, icon) ->
                    val isSelected = selectedAiMode == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedAiMode = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) SaudiGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) SaudiGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
        }

        // Content based on selected mode
        when (selectedAiMode) {
            0 -> item { LiveVoiceAdvisorSection(viewModel) }
            1 -> item { SearchGroundingSection(viewModel) }
            2 -> item { HighThinkingSection(viewModel) }
            3 -> item { ImageGenerationSection(viewModel) }
            4 -> item { FastResponseSection(viewModel) }
        }
    }
}

/**
 * 1. Live Voice Conversation using gemini-3.1-flash-live-preview
 */
@Composable
fun LiveVoiceAdvisorSection(viewModel: MainViewModel) {
    val isVoiceActive by viewModel.isVoiceActive.collectAsStateWithLifecycle()
    val transcript by viewModel.voiceTranscript.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isVoiceProcessing.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition(label = "voice_wave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isVoiceActive) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("live_voice_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isVoiceActive) SaudiGreen else Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isVoiceActive) "LIVE AUDIO ACTIVE" else "LIVE DESK IDLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isVoiceActive) SaudiGreen else Color.Gray
                    )
                }
                Text("gemini-3.1-flash-live-preview", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Audio Pulsing Sphere
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(waveScale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (isVoiceActive) listOf(SaudiGreen, SaudiGreenDark) else listOf(Color(0xFF64748B), Color(0xFF334155))
                        )
                    )
                    .clickable { viewModel.toggleVoiceAdvisor() }
                    .testTag("voice_toggle_sphere"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isVoiceActive) Icons.Default.GraphicEq else Icons.Default.Mic,
                    contentDescription = "Voice Mic",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { viewModel.toggleVoiceAdvisor() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVoiceActive) Color(0xFFDC2626) else SaudiGreen
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("voice_toggle_button")
            ) {
                Icon(
                    imageVector = if (isVoiceActive) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isVoiceActive) "End Voice Conversation" else "Start Live Voice Consultation")
            }

            if (isProcessing) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = SaudiGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Streaming Live Audio from Advisor...", fontSize = 11.sp, color = SaudiGreen)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Voice Spoken Output Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "LIVE ADVISOR TRANSCRIPT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = transcript,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Sample Spoken Prompts:", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(6.dp))

            val samplePrompts = listOf(
                "Explain the 30-year tax holiday under Saudi RHQ",
                "What are MISA 100% foreign ownership criteria?",
                "How do I maintain High Green Saudization on Qiwa?"
            )
            samplePrompts.forEach { prompt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEFF8F2))
                        .clickable { viewModel.askVoiceAdvisor(prompt) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(prompt, fontSize = 11.sp, color = SaudiGreen, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/**
 * 2. Search Grounding using gemini-3.5-flash with googleSearch tool
 */
@Composable
fun SearchGroundingSection(viewModel: MainViewModel) {
    var queryText by remember { mutableStateOf("What are the 2025/2026 MISA license updates and 30-year tax exemptions for Regional Headquarters (RHQ)?") }
    val result by viewModel.groundedResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isGroundedLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("search_grounding_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Google Search Grounded Regulatory Check",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("gemini-3.5-flash", fontSize = 10.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Access live, verified Saudi regulatory data, ZATCA tax circulars, and MISA legal announcements directly grounded in web search.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = queryText,
                onValueChange = { queryText = it },
                label = { Text("Regulatory Search Query") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.runGroundedSearch(queryText) },
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("run_grounded_search_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Searching Grounded Web Data...")
                } else {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Execute Search Grounded Query")
                }
            }

            if (result != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "GROUNDED INTELLIGENCE REPORT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result!!.text,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        if (result!!.sources.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Cited Regulatory Sources:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            result!!.sources.forEach { source ->
                                Text("• $source", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 3. High Thinking mode using gemini-3.1-pro-preview with thinkingLevel = HIGH
 */
@Composable
fun HighThinkingSection(viewModel: MainViewModel) {
    var promptText by remember { mutableStateOf("Design a strategic 3-year market expansion roadmap into Saudi Arabia for an AI & Fintech enterprise under RHQ, factoring in ZATCA Phase 2 Fatoora and Qiwa Nitaqat quotas.") }
    val result by viewModel.thinkingResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isThinkingLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("high_thinking_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "High Thinking Strategic Advisor",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF3E8FF))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("gemini-3.1-pro-preview (Thinking: HIGH)", fontSize = 10.sp, color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Executes deep analytical reasoning with ThinkingLevel.HIGH for intricate corporate tax structuring, multi-year staffing trajectories, and cross-border MISA compliance.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                label = { Text("Complex Advisory Query") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.runHighThinkingStrategy(promptText) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B21B6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("run_high_thinking_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyzing with Deep High-Thinking Model...")
                } else {
                    Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Initiate Deep High-Thinking Strategy")
                }
            }

            if (result != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFC084FC)))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "WE CONSULTING DEEP REASONING SYNTHESIS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5B21B6)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result!!,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF1E1B4B)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 4. Image Generation using gemini-3-pro-image-preview with user imageSize affordance (1K, 2K, 4K)
 */
@Composable
fun ImageGenerationSection(viewModel: MainViewModel) {
    var promptText by remember { mutableStateOf("Futuristic corporate headquarters tower in King Abdullah Financial District Riyadh with Saudi solar glass facade and luxury investor boardroom") }
    val selectedSize by viewModel.selectedImageSize.collectAsStateWithLifecycle()
    val imageResult by viewModel.imageResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isImageLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("image_generation_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expansion Concept Visualizer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaudiGold.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("gemini-3-pro-image-preview", fontSize = 10.sp, color = SaudiGoldDark, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Generate high-resolution corporate expansion mockups, KAFD Riyadh tower architectural concepts, or investor pitch assets with resolution selection.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Affordance for User to select Image Size: 1K, 2K, 4K
            Text("Select Output Resolution:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("1K", "2K", "4K").forEach { size ->
                    val isSelected = selectedSize == size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) SaudiGold else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { viewModel.setImageSize(size) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = size,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF1F2937) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                label = { Text("Corporate Concept Prompt") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.generatePitchVisual(promptText) },
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generate_image_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFF1E241F), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Rendering Concept in $selectedSize...", color = Color(0xFF1E241F), fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF1E241F), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Generate Visual ($selectedSize Resolution)", color = Color(0xFF1E241F), fontWeight = FontWeight.Bold)
                }
            }

            if (imageResult != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "OUTPUT RESOLUTION: ${imageResult!!.imageSize}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGoldDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        if (imageResult!!.base64Data != null) {
                            val decoded = Base64.decode(imageResult!!.base64Data, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Generated Concept",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF0F2B1D), Color(0xFF18422E))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("High-Quality Architecture Render ($selectedSize)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = imageResult!!.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 5. Low-latency fast responses using gemini-3.1-flash-lite
 */
@Composable
fun FastResponseSection(viewModel: MainViewModel) {
    var promptText by remember { mutableStateOf("What is the minimum capital required for a 100% foreign-owned Services LLC in Saudi Arabia?") }
    val result by viewModel.fastResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isFastLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("fast_response_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Low-Latency Instant Regulatory Check",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("gemini-3.1-flash-lite", fontSize = 10.sp, color = Color(0xFFB45309), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Provides sub-second fast answers on Saudi government codes, CR thresholds, and fast compliance checklists using gemini-3.1-flash-lite.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                label = { Text("Instant Regulatory Query") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.runFastCheck(promptText) },
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("run_fast_check_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fetching Fast Answer...")
                } else {
                    Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Instant Fast Check")
                }
            }

            if (result != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("INSTANT CHECK RESULT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result!!,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
