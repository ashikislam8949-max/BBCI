package com.example.ui.screens

import androidx.compose.material.icons.filled.Schedule

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CompanyDossierEntity
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onOpenNewCompanyDialog: () -> Unit,
    onOpenNafathDialog: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val dossiers by viewModel.dossiers.collectAsStateWithLifecycle()
    val complianceItems by viewModel.complianceItems.collectAsStateWithLifecycle()
    val authenticatedNafathUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()

    val compliantCount = complianceItems.count { it.status == "COMPLIANT" }
    val totalCompliance = maxOf(1, complianceItems.size)
    val healthPercentage = (compliantCount * 100) / totalCompliance

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            SaudiBrandHeader(
                title = "Saudi MISA CR & Gov Portal",
                subtitle = "Unified foreign investment licensing, CR incorporation & regulatory monitoring"
            )
        }

        // Nafath SSO Identity Quick Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (onOpenNafathDialog != null) {
                            onOpenNafathDialog()
                        } else {
                            viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS)
                        }
                    }
                    .testTag("dashboard_nafath_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (authenticatedNafathUser != null) Color(0xFF0F3B24) else Color(0xFF1E293B)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (authenticatedNafathUser != null) SaudiGreen else SaudiGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = if (authenticatedNafathUser != null) Color.White else Color(0xFF1E241F),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (authenticatedNafathUser != null)
                                    authenticatedNafathUser!!.arabicFullName
                                else
                                    "Nafath SSO Gateway (نفاذ الوطني الموحد)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (authenticatedNafathUser != null)
                                    "ID: ${authenticatedNafathUser!!.nationalId} • Authorized Signatory (موثق)"
                                else
                                    "Authenticate with Saudi National ID / Iqama & Wathq",
                                fontSize = 10.sp,
                                color = if (authenticatedNafathUser != null) Color(0xFF86EFAC) else Color(0xFFCBD5E1)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (authenticatedNafathUser != null) Color(0xFF22C55E).copy(alpha = 0.2f)
                                else SaudiGold.copy(alpha = 0.2f)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (authenticatedNafathUser != null) "Active نفاذ" else "Connect ربط",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (authenticatedNafathUser != null) Color(0xFF86EFAC) else SaudiGold
                        )
                    }
                }
            }
        }

        // ZATCA Tax Compliance, Filing Deadlines & Fatoora Phase 2 Invoices
        item {
            ZatcaDashboardCard(viewModel = viewModel)
        }

        // Balady Municipal Licenses & Permit Expiry Alert System
        item {
            BaladyDashboardCard(viewModel = viewModel)
        }

        // Qiwa Workforce Compliance & Saudization Dashboard Card
        item {
            QiwaDashboardCard(viewModel = viewModel)
        }

        // sbtech.sa Live Connected Portal Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppNavTab.SBTECH_PORTAL) }
                    .testTag("dashboard_sbtech_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF162032))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SaudiGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SB",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "sbtech.sa",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SaudiGold.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "CR 1010776412",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaudiGold
                                    )
                                }
                            }
                            Text(
                                text = "شركة إس بي تك للتقنية • ربط حكومي شامل (نفاذ، واثق، زكاة، قوى، بلدي)",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SaudiGold.copy(alpha = 0.2f))
                            .border(1.dp, SaudiGold, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = SaudiGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Open بوابة",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold
                            )
                        }
                    }
                }
            }
        }

        // Live Metric Cards Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricStatCard(
                    label = "Active Entities",
                    value = "${dossiers.size} Companies",
                    badgeText = "100% Foreign",
                    badgeColor = StatusSuccess,
                    icon = Icons.Default.Business,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    label = "Compliance Health",
                    value = "$healthPercentage%",
                    badgeText = "Statutory Radar",
                    badgeColor = if (healthPercentage > 80) StatusSuccess else StatusWarning,
                    icon = Icons.Default.Verified,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Fast Action Bar: New Registration & Voice AI Consultation
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenNewCompanyDialog,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("dashboard_new_company_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Register MISA CR", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.selectTab(AppNavTab.AI_ADVISORY) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_voice_ai_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Voice", fontSize = 13.sp, color = SaudiGoldDark)
                }
            }
        }

        // International Investor Consultation & Opportunities Gateway Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppNavTab.WE_CONSULTING) }
                    .testTag("dashboard_investor_consultation_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14241B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SaudiGold.copy(alpha = 0.2f))
                                .border(1.dp, SaudiGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = null,
                                tint = SaudiGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Investor Consultations & Opportunities",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Book certified MISA advisors & explore matched Saudi Vision 2030 sectors",
                                fontSize = 10.sp,
                                color = Color(0xFFC7DBCF)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SaudiGold)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Book Session",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E241F)
                        )
                    }
                }
            }
        }

        // Active Company Registrations Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Investment Dossiers",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All Steps",
                    style = MaterialTheme.typography.bodySmall,
                    color = SaudiGreen,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { viewModel.selectTab(AppNavTab.MISA_CR_WIZARD) }
                )
            }
        }

        items(dossiers) { dossier ->
            CompanyDossierCard(
                dossier = dossier,
                onClick = {
                    viewModel.selectDossier(dossier)
                    viewModel.selectTab(AppNavTab.MISA_CR_WIZARD)
                }
            )
        }

        // All Saudi Government Services Integrated Ecosystem
        item {
            Text(
                text = "Integrated Government Services Ecosystem",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Direct unified interconnection with the Kingdom's regulatory authorities",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            GovServicesGrid(
                onServiceClick = { serviceName ->
                    if (serviceName == "MISA") {
                        viewModel.selectTab(AppNavTab.MISA_CR_WIZARD)
                    } else {
                        viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS)
                    }
                }
            )
        }

        // WE Consulting Services Spotlight Banner
        item {
            WeConsultingBanner(
                onExplore = { viewModel.selectTab(AppNavTab.WE_CONSULTING) }
            )
        }
    }
}

@Composable
fun CompanyDossierCard(
    dossier: CompanyDossierEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("dossier_card_${dossier.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dossier.companyName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dossier.arabicName,
                        style = MaterialTheme.typography.bodySmall,
                        color = SaudiGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
                StatusPill(status = dossier.misaStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("CR Number", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(dossier.crNumber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Legal Structure", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(dossier.legalStructure.take(18) + "..", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Foreign Equity", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${dossier.foreignOwnershipPercentage}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Step Progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Government Milestone: Step ${dossier.currentStep} of 7",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(dossier.currentStep * 100) / 7}% Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { dossier.currentStep / 7f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = SaudiGreen,
                    trackColor = SaudiGreen.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Composable
fun GovServicesGrid(
    onServiceClick: (String) -> Unit
) {
    val services = listOf(
        GovServiceItem("NAFATH", "Nafath SSO (نفاذ)", "National Single Sign-On Gateway", Icons.Default.Fingerprint, SaudiGreen),
        GovServiceItem("WATHQ", "Wathq (واثق MC)", "Live CR & AoA Direct Verification", Icons.Default.Business, Color(0xFF0284C7)),
        GovServiceItem("ZATCA", "ZATCA Tax & Fatoora", "VAT 15% & Cryptographic Stamps", Icons.Default.Receipt, Color(0xFF0D9488)),
        GovServiceItem("Qiwa", "Qiwa & Mudad", "Work Visas & Saudization Quota", Icons.Default.People, Color(0xFFE11D48)),
        GovServiceItem("GOSI", "GOSI Insurance", "WPS Protection & Wage Compliance", Icons.Default.Security, Color(0xFF7C3AED)),
        GovServiceItem("Balady", "Balady Licensing", "Commercial Premises & Safety", Icons.Default.Gavel, Color(0xFFD97706))
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in services.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GovServiceCard(item = services[i], onClick = { onServiceClick(services[i].code) }, modifier = Modifier.weight(1f))
                if (i + 1 < services.size) {
                    GovServiceCard(item = services[i + 1], onClick = { onServiceClick(services[i + 1].code) }, modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class GovServiceItem(
    val code: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun GovServiceCard(
    item: GovServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("gov_card_${item.code}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.code,
                    tint = item.accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.code,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = item.accentColor
            )
            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeConsultingBanner(
    onExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1E2B24), Color(0xFF0F1A14))
                )
            )
            .border(1.dp, SaudiGold.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
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
                            .background(SaudiGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WE CONSULTING SERVICES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "International Expansion",
                    fontSize = 10.sp,
                    color = Color(0xFFB0C4B6)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Strategic Saudi Market Entry & RHQ Relocation Advisory",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Expert advisory for multinational corporations seeking 30-year tax exemptions, Special Economic Zones (SEZ) selection, and localized government relations.",
                fontSize = 12.sp,
                color = Color(0xFFD0DFD5)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onExplore,
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("explore_we_consulting_button")
            ) {
                Text("Explore WE Consulting Advisory", color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun QiwaDashboardCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val qiwaResult by viewModel.qiwaResult.collectAsStateWithLifecycle()
    val isQiwaLoading by viewModel.isQiwaLoading.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (qiwaResult == null) {
            viewModel.queryQiwa("7-882193")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) }
            .testTag("dashboard_qiwa_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE11D48).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Qiwa",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Qiwa & Mudad Workforce Compliance",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = qiwaResult?.establishmentName ?: "Hyperion Robotics KSA (Est #7-882193)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = qiwaResult?.nitaqatTier ?: "Platinum (بلاتيني)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val ratio = qiwaResult?.saudizationRatio ?: 62.5
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Saudization Nitaqat Ratio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$ratio% (Required: 40%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (ratio / 100.0).toFloat().coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = StatusSuccess,
                    trackColor = StatusSuccess.copy(alpha = 0.15f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Workforce", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.totalEmployees ?: 12} Employees", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Saudi Nationals", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.saudiEmployees ?: 5} Local", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                }
                Column {
                    Text("Visa Quota Balance", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${qiwaResult?.availableVisas ?: 8} Available", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                }
                Column {
                    Text("Mudad WPS", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(qiwaResult?.mudadWpsCompliance ?: "Compliant (100%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                }
            }
        }
    }
}

@Composable
fun BaladyDashboardCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val baladyResult by viewModel.baladyResult.collectAsStateWithLifecycle()
    val isBaladyLoading by viewModel.isBaladyLoading.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (baladyResult == null) {
            viewModel.queryBalady("4409923812")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) }
            .testTag("dashboard_balady_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD97706).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Balady",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Balady Municipal Licenses & Permits",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "License #${baladyResult?.licenseNumber ?: "4409923812"} • MOMRA",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = baladyResult?.status ?: "Active (سارية)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expiration Warning Alert System Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8)),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SaudiGold))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = SaudiGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Permit Expiry Alert: Valid until ${baladyResult?.expiryDate ?: "2027-12-31"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF713F12)
                        )
                        Text(
                            text = "Civil Defense safety certificate: ${if (baladyResult?.civilDefenseApproved == true) "Approved ✓" else "Pending Renewal"}",
                            fontSize = 10.sp,
                            color = Color(0xFF854D0E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Commercial Activity", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(baladyResult?.activityName ?: "Technology & Software R&D Facility", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Premise Address (KSA)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(baladyResult?.premiseAddress ?: "King Abdullah Financial District, Riyadh", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ZatcaDashboardCard(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val zatcaResult by viewModel.zatcaResult.collectAsStateWithLifecycle()
    val isZatcaLoading by viewModel.isZatcaLoading.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (zatcaResult == null) {
            viewModel.queryZatca("310582910400003")
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { viewModel.selectTab(AppNavTab.GOV_INTEGRATIONS) }
            .testTag("dashboard_zatca_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D9488).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = "ZATCA",
                            tint = Color(0xFF0D9488),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ZATCA Tax Compliance & Fatoora Phase 2",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "TIN: ${zatcaResult?.tinNumber ?: "310582910400003"} • VAT 15%",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = zatcaResult?.complianceRating ?: "Compliant 100%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Fatoora Phase 2 CSID", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(if (zatcaResult?.fatooraPhase2Integrated == true) "Active & Cleared ✓" else "Integrated", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                }
                Column {
                    Text("Zakat Expiry", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(zatcaResult?.zakatCertificateExpiry ?: "2027-04-30", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("VAT Status", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(zatcaResult?.vatStatus ?: "Registered (15%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Upcoming ZATCA Filing Deadlines",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SaudiGreenDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
                    .testTag("zatca_filing_deadlines"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Q3 VAT Return Declaration (Form VAT01)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text("Due: Oct 31, 2026", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("• Annual Zakat & CIT Return (Fiscal Year)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text("Due: Apr 30, 2027", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Recent Fatoora Phase 2 Invoice Issuance History",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SaudiGreenDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
                    .testTag("zatca_invoice_history"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("INV-2026-891 (B2B Enterprise SaaS)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text("SAR 115,000 | Cleared ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("INV-2026-890 (AI Engineering License)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Text("SAR 46,000 | Cleared ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                    }
                }
            }
        }
    }
}
