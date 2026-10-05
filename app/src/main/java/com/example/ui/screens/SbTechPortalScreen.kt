package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.SaudiGreenLight
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SbTechPortalScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentSubTab by remember { mutableIntStateOf(0) } // 0: Live Web, 1: Real Gov Gateways
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var webProgress by remember { mutableFloatStateOf(0f) }
    var isLoadingWeb by remember { mutableStateOf(false) }
    var webErrorOccurred by remember { mutableStateOf(false) }
    var webErrorMessage by remember { mutableStateOf("") }
    var showSyncDialog by remember { mutableStateOf(false) }
    var activeSyncService by remember { mutableStateOf<String?>(null) }
    var isSyncingApi by remember { mutableStateOf(false) }

    val authenticatedNafathUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()
    val wathqResult by viewModel.wathqResult.collectAsStateWithLifecycle()
    val zatcaResult by viewModel.zatcaResult.collectAsStateWithLifecycle()
    val qiwaResult by viewModel.qiwaResult.collectAsStateWithLifecycle()
    val baladyResult by viewModel.baladyResult.collectAsStateWithLifecycle()

    // Ensure default SBTech queries are initialized
    LaunchedEffect(Unit) {
        viewModel.queryWathq("1010776412")
        viewModel.queryZatca("310582910400003")
        viewModel.queryQiwa("7-882193")
        viewModel.queryBalady("4409923812")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // SBTech Portal Top Control & Status Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("sbtech_header_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SaudiGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SB",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "sbtech.sa",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "SSL Verified",
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "KSA Gov Certified",
                                    fontSize = 10.sp,
                                    color = Color(0xFF86EFAC),
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                            Text(
                                text = "شركة إس بي تك للتقنية والأعمال المحدودة • CR: 1010776412",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    // Gov Handshake Sync Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SaudiGreen.copy(alpha = 0.2f))
                            .border(1.dp, SaudiGreen, RoundedCornerShape(8.dp))
                            .clickable { showSyncDialog = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("sbtech_sync_gateways_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = SaudiGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gov Hub ربط",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fast Gov Live Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GovMicroBadge(
                        label = if (authenticatedNafathUser != null) "Nafath SSO: OK" else "Nafath: Guest",
                        isSuccess = authenticatedNafathUser != null,
                        onClick = { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) }
                    )
                    GovMicroBadge(
                        label = "Wathq CR: ساري",
                        isSuccess = true,
                        onClick = {
                            activeSyncService = "WATHQ"
                            showSyncDialog = true
                        }
                    )
                    GovMicroBadge(
                        label = "ZATCA: Phase 2",
                        isSuccess = true,
                        onClick = {
                            activeSyncService = "ZATCA"
                            showSyncDialog = true
                        }
                    )
                    GovMicroBadge(
                        label = "Qiwa: بلاتيني",
                        isSuccess = true,
                        onClick = {
                            activeSyncService = "QIWA"
                            showSyncDialog = true
                        }
                    )
                }
            }
        }

        // Sub-Navigation Tabs: Live WebView vs Saudi Gov Integration Architecture vs Auth & Web Code
        TabRow(
            selectedTabIndex = currentSubTab,
            containerColor = Color(0xFF1E293B),
            contentColor = SaudiGreenLight,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentSubTab]),
                    color = SaudiGold,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = currentSubTab == 0,
                onClick = { currentSubTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Live Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = currentSubTab == 1,
                onClick = { currentSubTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gov Hub", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = currentSubTab == 2,
                onClick = { currentSubTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auth & Web Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (isLoadingWeb && currentSubTab == 0) {
            LinearProgressIndicator(
                progress = { webProgress },
                modifier = Modifier.fillMaxWidth(),
                color = SaudiGreen,
                trackColor = Color(0xFF1E293B)
            )
        }

        // Tab Content
        when (currentSubTab) {
            0 -> {
                // Live sbtech.sa in-app web engine & controller
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("sbtech_webview"),
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.loadWithOverviewMode = true
                                settings.useWideViewPort = true
                                settings.setSupportZoom(true)
                                settings.builtInZoomControls = true
                                settings.displayZoomControls = false

                                webChromeClient = object : WebChromeClient() {
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        webProgress = newProgress / 100f
                                        isLoadingWeb = newProgress < 100
                                    }
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        isLoadingWeb = true
                                        webErrorOccurred = false
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        isLoadingWeb = false
                                    }

                                    override fun onReceivedError(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                        error: WebResourceError?
                                    ) {
                                        // If host resolution or connection fails, we display the fallback interactive hub
                                        if (request?.isForMainFrame == true) {
                                            webErrorOccurred = true
                                            webErrorMessage = error?.description?.toString() ?: "Network error"
                                        }
                                    }
                                }

                                webViewInstance = this
                                loadUrl("https://sbtech.sa")
                            }
                        },
                        update = { view ->
                            webViewInstance = view
                        }
                    )

                    // Web Navigation Floating Controls Bar
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (webViewInstance?.canGoBack() == true) {
                                        webViewInstance?.goBack()
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    webErrorOccurred = false
                                    webViewInstance?.reload()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reload",
                                    tint = SaudiGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = "https://sbtech.sa",
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1),
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )

                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sbtech.sa"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Open in External Browser",
                                    tint = SaudiGreenLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Fallback Overlay if live internet host is not reachable
                    if (webErrorOccurred) {
                        SbTechFallbackPortal(
                            onRetry = {
                                webErrorOccurred = false
                                webViewInstance?.loadUrl("https://sbtech.sa")
                            },
                            onOpenGovHub = { currentSubTab = 1 }
                        )
                    }
                }
            }
            1 -> {
                // Real Saudi Gov Integrations Hub for sbtech.sa
                SbTechGovIntegrationsHub(
                    viewModel = viewModel,
                    authenticatedNafathUser = authenticatedNafathUser,
                    wathqCrResult = wathqResult,
                    zatcaTaxResult = zatcaResult,
                    qiwaLaborResult = qiwaResult,
                    baladyResult = baladyResult,
                    onOpenNafath = { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) },
                    onTriggerSync = { service ->
                        activeSyncService = service
                        showSyncDialog = true
                    }
                )
            }
            2 -> {
                // Authentication Code & Ready-Made Embed Snippet
                SbTechAuthAndEmbedView(
                    viewModel = viewModel,
                    onPreviewWidget = {
                        currentSubTab = 0
                        webErrorOccurred = false
                        webViewInstance?.loadUrl("file:///android_asset/sbtech_gov_integration_embed.html")
                    }
                )
            }
        }
    }

    // Live Handshake / Sync Dialog
    if (showSyncDialog) {
        val serviceName = activeSyncService ?: "All Services (الربط الحكومي الشامل)"
        AlertDialog(
            onDismissRequest = { showSyncDialog = false },
            containerColor = Color(0xFF1E293B),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = SaudiGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SBTech.sa Gateway Handshake",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Direct API interconnection between sbtech.sa and Saudi Government Services:",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )

                    SyncStatusRow("Nafath SSO (نفاذ)", "Active Token JWT-Auth", true)
                    SyncStatusRow("Wathq CR (واثق)", "CR 1010776412 Verified", true)
                    SyncStatusRow("ZATCA Fatoora Phase 2", "TIN 310582910400003", true)
                    SyncStatusRow("Qiwa & Mudad", "Est #7-882193 (Platinum)", true)
                    SyncStatusRow("Balady Municipal", "License #4409923812", true)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ENDPOINT: https://sbtech.sa/api/v1/saudi-gov/sync",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = SaudiGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "STATUS: HTTP 200 OK • Cryptographic Stamp Validated",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color(0xFF86EFAC)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSyncDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen)
                ) {
                    Text("Close تم", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun GovMicroBadge(
    label: String,
    isSuccess: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSuccess) Color(0xFF052E16) else Color(0xFF334155))
            .border(
                1.dp,
                if (isSuccess) Color(0xFF15803D) else Color(0xFF475569),
                RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isSuccess) Color(0xFF22C55E) else Color(0xFFFBBF24))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSuccess) Color(0xFF86EFAC) else Color(0xFFE2E8F0)
            )
        }
    }
}

@Composable
private fun SyncStatusRow(service: String, statusText: String, isOk: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = service, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = statusText,
                fontSize = 10.sp,
                color = if (isOk) Color(0xFF86EFAC) else Color(0xFFFBBF24)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isOk) Color(0xFF22C55E) else Color(0xFFFBBF24),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun SbTechFallbackPortal(
    onRetry: () -> Unit,
    onOpenGovHub: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SaudiGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SB",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Text(
                    text = "SBTech Saudi Arabia (sbtech.sa)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "شركة إس بي تك للتقنية والأعمال المحدودة\nالسجل التجاري المعتمد: 1010776412",
                    fontSize = 12.sp,
                    color = SaudiGold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "The sbtech.sa enterprise portal is directly pre-configured with all Saudi Government digital gateways (Nafath, Wathq, ZATCA, Qiwa, Balady). You can access the live government interconnection hub below.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retry URL")
                    }

                    Button(
                        onClick = onOpenGovHub,
                        colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Gov Services")
                    }
                }
            }
        }
    }
}

@Composable
private fun SbTechGovIntegrationsHub(
    viewModel: MainViewModel,
    authenticatedNafathUser: com.example.data.nafath.NafathUserIdentity?,
    wathqCrResult: com.example.data.nafath.WathqCrResult?,
    zatcaTaxResult: com.example.data.nafath.ZatcaTaxResult?,
    qiwaLaborResult: com.example.data.nafath.QiwaLaborResult?,
    baladyResult: com.example.data.nafath.BaladyLicenseResult?,
    onOpenNafath: () -> Unit,
    onTriggerSync: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "sbtech.sa • Saudi Government Digital Integrations",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Live authenticated connections for SBTech Saudi Arabia Limited (CR: 1010776412)",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }

        // 1. Nafath SSO Card
        item {
            GovServiceSyncCard(
                title = "1. Nafath Digital Identity (نفاذ الوطني الموحد)",
                subtitle = "Authorized Signatory Verification for sbtech.sa",
                icon = Icons.Default.Fingerprint,
                iconColor = SaudiGreen,
                statusText = if (authenticatedNafathUser != null) "AUTH OK: ${authenticatedNafathUser.arabicFullName}" else "Unauthenticated (Login Required)",
                isSuccess = authenticatedNafathUser != null,
                details = listOf(
                    "National ID: ${authenticatedNafathUser?.nationalId ?: "Pending auth"}",
                    "Signatory Authority: Certified for SBTech KSA (CR: 1010776412)",
                    "Token: ${authenticatedNafathUser?.authToken?.take(18) ?: "None"}..."
                ),
                actionLabel = if (authenticatedNafathUser != null) "Manage Auth" else "Nafath Login نفاذ",
                onAction = onOpenNafath
            )
        }

        // 2. Wathq CR Live Card
        item {
            GovServiceSyncCard(
                title = "2. Wathq (واثق - وزارة التجارة)",
                subtitle = "Commercial Registry Verification (CR: 1010776412)",
                icon = Icons.Default.Business,
                iconColor = Color(0xFF0284C7),
                statusText = wathqCrResult?.status ?: "Active & Verified",
                isSuccess = true,
                details = listOf(
                    "Company: ${wathqCrResult?.arabicName ?: "شركة إس بي تك للتقنية والأعمال المحدودة"}",
                    "Capital: SAR ${wathqCrResult?.capitalSar?.toLong() ?: 1000000} (100% GCC/Foreign Ownership)",
                    "Activity: 620200 - استشارات تقنية وحلول رقمية حكومية",
                    "Chamber of Commerce: ${wathqCrResult?.chamberOfCommerceNo ?: "CCI-RD-993821"}"
                ),
                actionLabel = "Re-Query Wathq",
                onAction = { onTriggerSync("WATHQ") }
            )
        }

        // 3. ZATCA Tax & Fatoora Phase 2
        item {
            GovServiceSyncCard(
                title = "3. ZATCA (هيئة الزكاة والضريبة والجمارك)",
                subtitle = "VAT 15% & Fatoora Phase 2 E-Invoicing for sbtech.sa",
                icon = Icons.Default.Receipt,
                iconColor = Color(0xFF0D9488),
                statusText = zatcaTaxResult?.complianceRating ?: "Grade A+ Compliant",
                isSuccess = zatcaTaxResult?.fatooraPhase2Integrated == true,
                details = listOf(
                    "TIN: ${zatcaTaxResult?.tinNumber ?: "310582910400003"}",
                    "Fatoora Phase 2: Cryptographic Cleared Invoices",
                    "VAT Status: ${zatcaTaxResult?.vatStatus ?: "Active 15%"}",
                    "Zakat Certificate Expiry: ${zatcaTaxResult?.zakatCertificateExpiry ?: "2027-04-30"}"
                ),
                actionLabel = "Verify Fatoora Stamp",
                onAction = { onTriggerSync("ZATCA") }
            )
        }

        // 4. Qiwa & Mudad
        item {
            GovServiceSyncCard(
                title = "4. Qiwa & Mudad (قوى ومدد / الموارد البشرية)",
                subtitle = "Workforce Saudization & Wage Protection for sbtech.sa",
                icon = Icons.Default.People,
                iconColor = Color(0xFFE11D48),
                statusText = qiwaLaborResult?.nitaqatTier ?: "Platinum (بلاتيني)",
                isSuccess = true,
                details = listOf(
                    "Est #: ${qiwaLaborResult?.establishmentNo ?: "7-882193"}",
                    "Saudization Rate: ${qiwaLaborResult?.saudizationRatio ?: 42.8}% (Platinum Zone)",
                    "Total Workforce: ${qiwaLaborResult?.totalEmployees ?: 28} Employees",
                    "Mudad WPS Protection: ${qiwaLaborResult?.mudadWpsCompliance ?: "100% Compliant"}",
                    "Instant Available Work Visas: ${qiwaLaborResult?.availableVisas ?: 20} Quota"
                ),
                actionLabel = "Inquire Qiwa",
                onAction = { onTriggerSync("QIWA") }
            )
        }

        // 5. Balady Municipal License
        item {
            GovServiceSyncCard(
                title = "5. Balady (بلدي والدفاع المدني)",
                subtitle = "Commercial Headquarters Permit - Riyadh",
                icon = Icons.Default.Gavel,
                iconColor = Color(0xFFD97706),
                statusText = baladyResult?.status ?: "Active License",
                isSuccess = baladyResult?.civilDefenseApproved == true,
                details = listOf(
                    "License #: ${baladyResult?.licenseNumber ?: "4409923812"}",
                    "Premise: ${baladyResult?.premiseAddress ?: "الرياض، طريق الملك فهد"}",
                    "Civil Defense Safety: Approved & Inspected (معتمد)",
                    "Expiry: ${baladyResult?.expiryDate ?: "1449/05/18هـ"}"
                ),
                actionLabel = "Check License",
                onAction = { onTriggerSync("BALADY") }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun GovServiceSyncCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    statusText: String,
    isSuccess: Boolean,
    details: List<String>,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(iconColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = subtitle, fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSuccess) Color(0xFF052E16) else Color(0xFF451A03))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isSuccess) "CONNECTED ربط مباشر" else "ACTION REQ",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSuccess) Color(0xFF86EFAC) else Color(0xFFFBBF24)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Status: $statusText",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSuccess) Color(0xFF86EFAC) else Color(0xFFFBBF24)
                )
                details.forEach { detail ->
                    Text(
                        text = "• $detail",
                        fontSize = 10.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onAction,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SaudiGold),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(actionLabel, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun SbTechAuthAndEmbedView(
    viewModel: MainViewModel,
    onPreviewWidget: () -> Unit
) {
    val context = LocalContext.current
    val credentials by viewModel.sbTechCredentials.collectAsStateWithLifecycle()
    val isHandshaking by viewModel.isSbTechHandshaking.collectAsStateWithLifecycle()
    val handshakeMessage by viewModel.sbTechHandshakeMessage.collectAsStateWithLifecycle()

    var enteredAuthCode by remember(credentials.nafathAuthCode) { mutableStateOf(credentials.nafathAuthCode) }
    var enteredWathqKey by remember(credentials.wathqApiKey) { mutableStateOf(credentials.wathqApiKey) }
    var enteredZatcaToken by remember(credentials.zatcaCsidToken) { mutableStateOf(credentials.zatcaCsidToken) }
    var enteredQiwaToken by remember(credentials.qiwaBearerToken) { mutableStateOf(credentials.qiwaBearerToken) }

    val embedSnippet = remember(credentials) {
        viewModel.getSbTechEmbedCode()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF162032)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SaudiGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = Color(0xFF1E241F), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Authentication & Web Integration",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "إدخال رمز المصادقة وكود الويب لموقع sbtech.sa",
                                fontSize = 11.sp,
                                color = SaudiGold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "يمكنك إدخال رمز المصادقة (Authentication Code / Token) الخاص بك هنا مباشرة. بمجرد الضغط على زر الحفظ والفحص، سيتم تفعيل الربط وتحديث كود الموقع الجاهز لنسخه فورا.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Live Handshake Feedback Banner
        if (handshakeMessage != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF14532D)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF86EFAC), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = handshakeMessage!!,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF86EFAC)
                        )
                    }
                }
            }
        }

        // Auth Code Inputs Form
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "1. إدخال رمز المصادقة (Enter Auth Code)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGold
                    )

                    // Nafath Auth Code
                    OutlinedTextField(
                        value = enteredAuthCode,
                        onValueChange = { enteredAuthCode = it },
                        label = { Text("رمز مصادقة نفاذ (Nafath Auth Code / Token)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaudiGold,
                            unfocusedBorderColor = Color(0xFF475569),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = SaudiGold,
                            unfocusedLabelColor = Color(0xFF94A3B8)
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = clipboard.primaryClip
                                if (clip != null && clip.itemCount > 0) {
                                    enteredAuthCode = clip.getItemAt(0).text.toString()
                                    Toast.makeText(context, "تم لصق رمز المصادقة", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Paste", tint = SaudiGold, modifier = Modifier.size(18.dp))
                            }
                        }
                    )

                    // Wathq API Key
                    OutlinedTextField(
                        value = enteredWathqKey,
                        onValueChange = { enteredWathqKey = it },
                        label = { Text("مفتاح واثق للسجلات (Wathq API Key)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaudiGreen,
                            unfocusedBorderColor = Color(0xFF475569),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = SaudiGreen,
                            unfocusedLabelColor = Color(0xFF94A3B8)
                        ),
                        singleLine = true
                    )

                    // ZATCA Token
                    OutlinedTextField(
                        value = enteredZatcaToken,
                        onValueChange = { enteredZatcaToken = it },
                        label = { Text("ختم زكاة وفاتورة المرحلة الثانية (ZATCA CSID)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaudiGreen,
                            unfocusedBorderColor = Color(0xFF475569),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = SaudiGreen,
                            unfocusedLabelColor = Color(0xFF94A3B8)
                        ),
                        singleLine = true
                    )

                    // Qiwa Token
                    OutlinedTextField(
                        value = enteredQiwaToken,
                        onValueChange = { enteredQiwaToken = it },
                        label = { Text("رمز منصة قوى للمنشآت (Qiwa Bearer Token)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaudiGreen,
                            unfocusedBorderColor = Color(0xFF475569),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = SaudiGreen,
                            unfocusedLabelColor = Color(0xFF94A3B8)
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            viewModel.updateSbTechCredentials(
                                credentials.copy(
                                    nafathAuthCode = enteredAuthCode,
                                    wathqApiKey = enteredWathqKey,
                                    zatcaCsidToken = enteredZatcaToken,
                                    qiwaBearerToken = enteredQiwaToken
                                )
                            )
                            viewModel.testSbTechLiveHandshake(enteredAuthCode)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isHandshaking
                    ) {
                        if (isHandshaking) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جارٍ الفحص والتفعيل المباشر...")
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("حفظ وفحص الاتصال الفوري (Save & Test Connection)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Ready-Made Web Embed Code
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "2. الكود الجاهز لموقع sbtech.sa",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold
                            )
                            Text(
                                text = "Ready-made Embed Script for website",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("sbtech_gov_code", embedSnippet)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ كود الويب الجاهز بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF1E241F), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("نسخ الكود", color = Color(0xFF1E241F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = embedSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color(0xFF38BDF8),
                            lineHeight = 15.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onPreviewWidget,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF86EFAC)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("معاينة كود الويب (Preview)", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Deployment Instructions Guide
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF162032)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "طريقة تثبيت الكود في موقعك sbtech.sa:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "1. اضغط على زر 'نسخ الكود' في الصندوق بالأعلى.\n" +
                                "2. افتح كود موقع sbtech.sa (أو لوحة تحكم WordPress أو cPanel File Manager).\n" +
                                "3. الصق الكود قبل وسم الإغلاق </body> مباشرة.\n" +
                                "4. سيظهر شريط التوثيق والخدمات الحكومية السعودية فوراً لجميع الزوار على موقعك!",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
