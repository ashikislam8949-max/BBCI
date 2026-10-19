package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppNavTab
import com.example.ui.screens.MisaInvestmentApplicationScreen
import com.example.ui.MainViewModel
import com.example.R
import com.example.ui.components.AffiliationNoticeDialog
import com.example.ui.components.AuthProfileDialog
import com.example.ui.components.NewCompanyDialog
import com.example.ui.screens.AiRegulatorySuiteScreen
import com.example.ui.screens.ComplianceRadarScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GovIntegrationsScreen
import com.example.ui.screens.MisaCrWizardScreen
import com.example.ui.screens.SbTechPortalScreen
import com.example.ui.screens.WeConsultingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import androidx.compose.material.icons.filled.Language

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("sbtech_investor", Context.MODE_PRIVATE)
    }
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var showAuthDialog by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showFirstLaunchNotice by remember {
        mutableStateOf(!prefs.getBoolean("disclaimer_acknowledged", false))
    }
    var showNewCompanyDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SaudiGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SB",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SBTECH Investor Desk",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.affiliation_disclaimer),
                                fontSize = 10.sp,
                                color = Color(0xFFD4EBD9),
                                maxLines = 2
                            )
                        }
                    }
                },
                actions = {
                    // Quick sbtech.sa Live Portal Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (currentTab == AppNavTab.SBTECH_PORTAL) SaudiGold
                                else Color(0xFF0F172A).copy(alpha = 0.5f)
                            )
                            .clickable {
                                if (currentTab == AppNavTab.SBTECH_PORTAL) {
                                    viewModel.selectTab(AppNavTab.DASHBOARD)
                                } else {
                                    viewModel.selectTab(AppNavTab.SBTECH_PORTAL)
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("topbar_sbtech_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "sbtech.sa",
                                tint = if (currentTab == AppNavTab.SBTECH_PORTAL) Color(0xFF1E241F) else SaudiGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "sbtech.sa",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentTab == AppNavTab.SBTECH_PORTAL) Color(0xFF1E241F) else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { showAbout = true },
                        modifier = Modifier.testTag("about_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.about_title),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Investor profile badge with Firebase status
                    IconButton(
                        onClick = { showAuthDialog = true },
                        modifier = Modifier.testTag("auth_profile_button")
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Investor Account",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (currentUser != null) SaudiGold else Color.White.copy(alpha = 0.35f))
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaudiGreenDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AppNavTab.DASHBOARD,
                    onClick = { viewModel.selectTab(AppNavTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Overview") },
                    label = { Text("Overview", fontSize = 9.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGreen,
                        selectedTextColor = SaudiGreen,
                        indicatorColor = SaudiGreen.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_tab_overview")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.SBTECH_PORTAL,
                    onClick = { viewModel.selectTab(AppNavTab.SBTECH_PORTAL) },
                    icon = { Icon(Icons.Default.Language, contentDescription = "sbtech.sa") },
                    label = { Text("sbtech.sa", fontSize = 9.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGold,
                        selectedTextColor = SaudiGold,
                        indicatorColor = SaudiGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_tab_sbtech")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.GOV_INTEGRATIONS,
                    onClick = { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) },
                    icon = { Icon(Icons.Default.Description, contentDescription = "Notes") },
                    label = { Text("Notes", fontSize = 9.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGreen,
                        selectedTextColor = SaudiGreen,
                        indicatorColor = SaudiGreen.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_tab_gov_gate")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.COMPLIANCE_RADAR,
                    onClick = { viewModel.selectTab(AppNavTab.COMPLIANCE_RADAR) },
                    icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Compliance") },
                    label = { Text("Compliance", fontSize = 9.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGreen,
                        selectedTextColor = SaudiGreen,
                        indicatorColor = SaudiGreen.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_tab_compliance")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.WE_CONSULTING,
                    onClick = { viewModel.selectTab(AppNavTab.WE_CONSULTING) },
                    icon = { Icon(Icons.Default.Handshake, contentDescription = "WE Consulting") },
                    label = { Text("Advisory", fontSize = 9.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGreen,
                        selectedTextColor = SaudiGreen,
                        indicatorColor = SaudiGreen.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_tab_consulting")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.AI_ADVISORY,
                    onClick = { viewModel.selectTab(AppNavTab.AI_ADVISORY) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Suite") },
                    label = { Text("AI Suite", fontSize = 9.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaudiGreen,
                        selectedTextColor = SaudiGreen,
                        indicatorColor = SaudiGreen.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag("nav_tab_ai_suite")
                )
            }
        },
        floatingActionButton = {
            if (currentTab == AppNavTab.DASHBOARD || currentTab == AppNavTab.MISA_CR_WIZARD) {
                FloatingActionButton(
                    onClick = { showNewCompanyDialog = true },
                    containerColor = SaudiGold,
                    contentColor = Color(0xFF1E241F),
                    modifier = Modifier.testTag("fab_register_company")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Register New Company")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onOpenNewCompanyDialog = { showNewCompanyDialog = true }
                )
                AppNavTab.SBTECH_PORTAL -> SbTechPortalScreen(
                    viewModel = viewModel
                )
                AppNavTab.MISA_CR_WIZARD -> MisaCrWizardScreen(
                    viewModel = viewModel,
                    onOpenNewCompanyDialog = { showNewCompanyDialog = true }
                )
                AppNavTab.MISA_APPLICATION -> MisaInvestmentApplicationScreen(
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.selectTab(AppNavTab.MISA_CR_WIZARD) }
                )
                AppNavTab.GOV_INTEGRATIONS -> GovIntegrationsScreen(
                    viewModel = viewModel
                )
                AppNavTab.COMPLIANCE_RADAR -> ComplianceRadarScreen(
                    viewModel = viewModel
                )
                AppNavTab.WE_CONSULTING -> WeConsultingScreen(
                    viewModel = viewModel
                )
                AppNavTab.AI_ADVISORY -> AiRegulatorySuiteScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    if (showAuthDialog) {
        AuthProfileDialog(
            viewModel = viewModel,
            currentUser = currentUser,
            onDismiss = { showAuthDialog = false }
        )
    }

    if (showNewCompanyDialog) {
        NewCompanyDialog(
            viewModel = viewModel,
            onDismiss = { showNewCompanyDialog = false }
        )
    }

    if (showFirstLaunchNotice || showAbout) {
        AffiliationNoticeDialog(
            acknowledgeRequired = showFirstLaunchNotice,
            onDismiss = {
                if (showFirstLaunchNotice) {
                    prefs.edit().putBoolean("disclaimer_acknowledged", true).apply()
                    showFirstLaunchNotice = false
                }
                showAbout = false
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

