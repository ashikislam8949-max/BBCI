package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CompanyDossierEntity
import com.example.ui.AppNavTab
import com.example.ui.MainViewModel
import com.example.ui.components.NafathSsoAuthDialog
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.StatusSuccess

data class GovMilestoneStep(
    val stepNumber: Int,
    val authority: String,
    val title: String,
    val description: String,
    val processingTime: String,
    val feeEst: String,
    val keyDocuments: List<String>
)

val SAUDI_GOV_STEPS = listOf(
    GovMilestoneStep(
        stepNumber = 1,
        authority = "Ministry of Investment (MISA)",
        title = "Foreign Investment License Issuance",
        description = "Obtain 100% foreign ownership license under the Saudi Investment Law. Authorizes foreign commercial operations in KSA.",
        processingTime = "24 - 48 Hours",
        feeEst = "SAR 2,000 (1st Year)",
        keyDocuments = listOf("Parent Company Certificate of Incorporation", "Audited Financials (Past 1 Year)", "Board Resolution of KSA Expansion")
    ),
    GovMilestoneStep(
        stepNumber = 2,
        authority = "Ministry of Commerce (MC)",
        title = "Commercial Registration (CR) & Notarized AoA",
        description = "Register commercial trade name, issue digital 10-digit CR, and notarize Articles of Association (AoA) via Wathq.",
        processingTime = "Instant (Digital)",
        feeEst = "SAR 1,200 (CR + Chamber of Commerce)",
        keyDocuments = listOf("Approved MISA License", "Drafted Articles of Association", "Designated General Manager ID/Passport")
    ),
    GovMilestoneStep(
        stepNumber = 3,
        authority = "SPL & Balady (MOMRA)",
        title = "National Address & Municipal Operating License",
        description = "Register official Saudi Post (SPL) National Address for corporate premises and obtain Balady municipal safety certificate.",
        processingTime = "1 - 3 Business Days",
        feeEst = "SAR 500 - 1,500 (Varies by premises m²)",
        keyDocuments = listOf("Active CR Certificate", "Ejar Registered Commercial Lease Contract", "Civil Defense Safety Approval")
    ),
    GovMilestoneStep(
        stepNumber = 4,
        authority = "ZATCA (Zakat, Tax & Customs)",
        title = "Tax, VAT 15% & Fatoora Phase 2 Onboarding",
        description = "Open corporate tax file (20% Corporate Income Tax for foreign shares), obtain VAT certificate, and integrate Phase 2 e-invoicing cryptographic stamps.",
        processingTime = "Instant / 24 Hours",
        feeEst = "Free (Government Registration)",
        keyDocuments = listOf("Commercial Registration (CR)", "Bank IBAN Certificate", "ERP E-Invoicing Technical Solution Provider File")
    ),
    GovMilestoneStep(
        stepNumber = 5,
        authority = "GOSI (General Org for Social Insurance)",
        title = "Social Insurance Entity Account Activation",
        description = "Establish company social security registry for expatriate occupational hazard insurance and Saudi employee retirement pensions.",
        processingTime = "1 Business Day",
        feeEst = "Monthly Contribution (2% - 21.5%)",
        keyDocuments = listOf("CR & National Address", "General Manager Authorization", "Initial Employee Rosters")
    ),
    GovMilestoneStep(
        stepNumber = 6,
        authority = "Qiwa & Mudad (MHRSD)",
        title = "Labor File & Mudad Wages Protection System",
        description = "Open Qiwa establishment account, formulate compliant internal labor bylaws, allocate work visa quotas, and link payroll to Mudad WPS.",
        processingTime = "1 - 2 Business Days",
        feeEst = "SAR 1,200 / Year (Qiwa Portal Subscription)",
        keyDocuments = listOf("MISA License & CR", "Corporate Bank Account IBAN", "Electronic Labor Contract Templates")
    ),
    GovMilestoneStep(
        stepNumber = 7,
        authority = "Muqeem & Absher Business (Passports)",
        title = "General Manager Iqama & Investor Work Visas",
        description = "Activate digital immigration portal for issuing foreign investor Iqamas, work visas, and multi-entry business visas for international leadership.",
        processingTime = "Instant to 5 Days",
        feeEst = "SAR 650 (Iqama) + Visa fees",
        keyDocuments = listOf("Qiwa Quota Approval", "Medical Screening Clearance", "Authenticated Employment Agreement")
    )
)

@Composable
fun MisaCrWizardScreen(
    viewModel: MainViewModel,
    onOpenNewCompanyDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dossiers by viewModel.dossiers.collectAsStateWithLifecycle()
    val selectedDossier by viewModel.selectedDossier.collectAsStateWithLifecycle()
    val authenticatedNafathUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()

    val activeDossier = selectedDossier ?: dossiers.firstOrNull()
    var viewingStepIndex by remember { mutableIntStateOf(0) }
    var showNafathDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SaudiBrandHeader(
                title = "MISA CR & Government Integration",
                subtitle = "7-Step seamless company registration and digital regulatory issuance"
            )
        }
        // Launch Multi-Step MISA Application Form Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(AppNavTab.MISA_APPLICATION) }
                    .testTag("card_launch_misa_form"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2F)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "New: Multi-Step MISA e-License Form",
                            color = Color(0xFF86EFAC),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Launch interactive 5-step form for company registration, business activity selection, and capital filing.",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD4AF37))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Start Form →",
                            color = Color(0xFF1E241F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Dossier Selector Bar
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Investor Dossier",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+ Register New Entity",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen,
                        modifier = Modifier
                            .clickable(onClick = onOpenNewCompanyDialog)
                            .testTag("wizard_new_company_button")
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dossiers) { dossier ->
                        val isSelected = activeDossier?.id == dossier.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) SaudiGreen else MaterialTheme.colorScheme.surface
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) SaudiGreen else Color(0xFFD5DCD5),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    viewModel.selectDossier(dossier)
                                    viewingStepIndex = (dossier.currentStep - 1).coerceIn(0, 6)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = dossier.companyName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "CR: ${dossier.crNumber}",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFD7EEDF) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Dossier Summary & Progress Banner
        if (activeDossier != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = activeDossier.companyName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${activeDossier.licenseType} • ${activeDossier.foreignOwnershipPercentage}% Foreign Equity",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SaudiGoldDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            StatusPill(status = activeDossier.misaStatus)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Registration Progress: Step ${activeDossier.currentStep} / 7",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${(activeDossier.currentStep * 100) / 7}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { activeDossier.currentStep / 7f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SaudiGreen,
                            trackColor = SaudiGreen.copy(alpha = 0.15f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Authority: ${SAUDI_GOV_STEPS[minOf(6, activeDossier.currentStep - 1)].authority}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (activeDossier.currentStep < 7) {
                                Button(
                                    onClick = { viewModel.advanceDossierStep(activeDossier) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("advance_step_button")
                                ) {
                                    Text("Complete Step ${activeDossier.currentStep}", fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(12.dp))
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Fully Incorporated", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Nafath Signatory Status for Regulated Commercial Filing
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showNafathDialog = true }
                    .testTag("wizard_nafath_signatory_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (authenticatedNafathUser != null) Color(0xFF0F3621) else Color(0xFF1E2836)
                ),
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
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (authenticatedNafathUser != null) SaudiGreen else SaudiGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = if (authenticatedNafathUser != null) Color.White else Color(0xFF1E241F),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (authenticatedNafathUser != null) "Nafath Signatory Verified (مفوض معتمد)"
                                    else "Government-Regulated Signatory Filing",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = if (authenticatedNafathUser != null)
                                    "${authenticatedNafathUser!!.arabicFullName} • National ID: ${authenticatedNafathUser!!.nationalId}"
                                else
                                    "Sign in via Nafath SSO to verify your legal signatory status for MISA & Wathq filings",
                                fontSize = 10.sp,
                                color = if (authenticatedNafathUser != null) Color(0xFF86EFAC) else Color(0xFFCBD5E1)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (authenticatedNafathUser != null) StatusSuccess.copy(alpha = 0.2f) else SaudiGold)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (authenticatedNafathUser != null) "Verified ✓" else "Verify via نفاذ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (authenticatedNafathUser != null) StatusSuccess else Color(0xFF1E241F)
                        )
                    }
                }
            }
        }

        // 7-Step Horizontal Stepper Navigation
        item {
            Text(
                text = "The 7 Government Integration Milestones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Select any step to view required documents, statutory fees, and clearance timelines",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Stepper Items
        items(SAUDI_GOV_STEPS) { step ->
            val isCompleted = activeDossier != null && activeDossier.currentStep > step.stepNumber
            val isCurrent = activeDossier != null && activeDossier.currentStep == step.stepNumber

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step_card_${step.stepNumber}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) Color(0xFFF3FAF5) else MaterialTheme.colorScheme.surface
                ),
                border = if (isCurrent) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SaudiGreen)) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 3.dp else 1.dp)
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
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCompleted -> StatusSuccess
                                            isCurrent -> SaudiGreen
                                            else -> Color(0xFFCBD5E1)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                } else {
                                    Text(
                                        text = "${step.stepNumber}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = step.authority,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) SaudiGreen else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = step.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        if (isCompleted) {
                            Text("Cleared", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                        } else if (isCurrent) {
                            Text("Active Stage", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = step.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(step.processingTime, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(step.feeEst, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mandatory Filings & Documents:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    step.keyDocuments.forEach { doc ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 3.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(doc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    if (showNafathDialog) {
        NafathSsoAuthDialog(
            viewModel = viewModel,
            onDismiss = { showNafathDialog = false }
        )
    }
}
