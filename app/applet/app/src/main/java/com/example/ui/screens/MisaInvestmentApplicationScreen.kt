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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.StatusSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisaInvestmentApplicationScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authenticatedNafathUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()

    // Wizard Step State (1 to 5)
    var currentStep by remember { mutableIntStateOf(1) }

    // Step 1: Signatory & Legal Representative
    var representativeName = authenticatedNafathUser?.arabicFullName ?: "Dr. Tariq Al-Mansoor"
    var nationalId = authenticatedNafathUser?.nationalId ?: "1098234567"
    var contactEmail = "tariq.mansoor@hyperion-robotics.sa"
    var contactPhone = "+966 50 123 4567"

    // Step 2: Company Registration Details
    var englishName by remember { mutableStateOf("Hyperion Robotics Advanced KSA") }
    var arabicName by remember { mutableStateOf("شركة هايبريون للروبوتات المتقدمة السعودية") }
    var statedCapitalText by remember { mutableStateOf("2500000") }
    var foreignOwnership by remember { mutableFloatStateOf(100f) }
    var headOfficeCity by remember { mutableStateOf("Riyadh (KAFD)") }

    // Step 3: Business Activity Selection
    val activityOptions = listOf(
        "ICT, Artificial Intelligence & Cloud Computing (ISIC 6201)",
        "Advanced Robotics & Industrial Manufacturing (ISIC 2819)",
        "Clean Tech, Solar & Green Hydrogen Infrastructure (ISIC 3510)",
        "Regional Headquarters (RHQ) Corporate Management (ISIC 7010)",
        "FinTech, Payment Gateways & Digital Ledger (ISIC 6499)",
        "Specialized Professional Consulting & Engineering (ISIC 7110)"
    )
    var expandedActivity by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf(activityOptions[0]) }

    val legalStructureOptions = listOf(
        "Limited Liability Company (LLC)",
        "Simplified Joint Stock Company (SJSC)",
        "Branch of Foreign Parent Company",
        "Regional Headquarters (RHQ)"
    )
    var expandedStructure by remember { mutableStateOf(false) }
    var selectedStructure by remember { mutableStateOf(legalStructureOptions[0]) }

    // Step 4: Supporting Documents Checklist
    var parentCertChecked by remember { mutableStateOf(true) }
    var financialsChecked by remember { mutableStateOf(true) }
    var boardResChecked by remember { mutableStateOf(true) }
    var poaChecked by remember { mutableStateOf(true) }

    // Step 5: Final Submission Confirmation Dialog State
    var showSuccessDialog by remember { mutableStateOf(false) }
    var generatedCrNumber by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("btn_back_to_wizard")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Back to Milestones")
                }
                Text(
                    text = "MISA e-License Portal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaudiGreen
                )
            }
        }

        item {
            SaudiBrandHeader(
                title = "MISA Investment License Application",
                subtitle = "Official Ministry of Investment 5-Step e-Filing & Commercial Registration Wizard"
            )
        }

        // Stepper Progress Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        Text(
                            text = "Step $currentStep of 5: ${
                                when (currentStep) {
                                    1 -> "Signatory Verification"
                                    2 -> "Company Details & Capital"
                                    3 -> "Business Activity & Sector"
                                    4 -> "Supporting Documents"
                                    else -> "Review & Official Submit"
                                }
                            }",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGreenDark
                        )
                        Text(
                            text = "${(currentStep * 100) / 5}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGoldDark
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { currentStep / 5f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SaudiGreen,
                        trackColor = SaudiGreen.copy(alpha = 0.15f)
                    )
                }
            }
        }

        // Form Step Content
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("misa_form_step_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (currentStep) {
                        1 -> {
                            // Step 1: Signatory & Legal Representative
                            Text(
                                text = "1. Authorized Signatory & Nafath Verification",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Verify your Saudi National Identity or authorized residency via Nafath Single Sign-On to act as the legal applicant for the MISA investment license.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Nafath Status Box
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (authenticatedNafathUser != null) Color(0xFFF0FDF4) else Color(0xFFFEFCE8)
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(if (authenticatedNafathUser != null) StatusSuccess else SaudiGold)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (authenticatedNafathUser != null) Icons.Default.VerifiedUser else Icons.Default.Fingerprint,
                                            contentDescription = null,
                                            tint = if (authenticatedNafathUser != null) StatusSuccess else SaudiGoldDark,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = if (authenticatedNafathUser != null) "Nafath Verified Signatory" else "Nafath SSO Recommended",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = if (authenticatedNafathUser != null) "${authenticatedNafathUser!!.arabicFullName} • ID: ${authenticatedNafathUser!!.nationalId}" else "Tap to authenticate with National Single Sign-On",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                            text = if (authenticatedNafathUser != null) "Verified ✓" else "Active",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (authenticatedNafathUser != null) StatusSuccess else Color(0xFF1E241F)
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = representativeName,
                                onValueChange = {},
                                label = { Text("Legal Representative / General Manager Full Name") },
                                leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )

                            OutlinedTextField(
                                value = nationalId,
                                onValueChange = {},
                                label = { Text("National ID / Iqama Number") },
                                leadingIcon = { Icon(Icons.Default.Fingerprint, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )

                            OutlinedTextField(
                                value = contactEmail,
                                onValueChange = { contactEmail = it },
                                label = { Text("Corporate Email Address") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = contactPhone,
                                onValueChange = { contactPhone = it },
                                label = { Text("Mobile Phone Number (+966)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        2 -> {
                            // Step 2: Company Registration Details
                            Text(
                                text = "2. Proposed Company Details & Capital Structure",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Enter your desired trade names in English and Arabic, select the legal structure, and declare the stated capital and foreign ownership percentage.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedTextField(
                                value = englishName,
                                onValueChange = { englishName = it },
                                label = { Text("English Trade Name (e.g. Hyperion Robotics KSA)") },
                                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = arabicName,
                                onValueChange = { arabicName = it },
                                label = { Text("Arabic Trade Name (الاسم التجاري بالعربية)") },
                                leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Legal Structure Dropdown
                            ExposedDropdownMenuBox(
                                expanded = expandedStructure,
                                onExpandedChange = { expandedStructure = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = selectedStructure,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Legal Entity Form") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStructure) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedStructure,
                                    onDismissRequest = { expandedStructure = false }
                                ) {
                                    legalStructureOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                selectedStructure = option
                                                expandedStructure = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = statedCapitalText,
                                onValueChange = { statedCapitalText = it },
                                label = { Text("Stated Share Capital (SAR)") },
                                leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Foreign Ownership Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Foreign Equity Share", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("${foreignOwnership.toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SaudiGoldDark)
                                }
                                Slider(
                                    value = foreignOwnership,
                                    onValueChange = { foreignOwnership = it },
                                    valueRange = 10f..100f,
                                    steps = 8,
                                    colors = SliderDefaults.colors(
                                        thumbColor = SaudiGreen,
                                        activeTrackColor = SaudiGreen
                                    )
                                )
                            }

                            OutlinedTextField(
                                value = headOfficeCity,
                                onValueChange = { headOfficeCity = it },
                                label = { Text("Intended Head Office City in KSA") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        3 -> {
                            // Step 3: Business Activity Selection
                            Text(
                                text = "3. MISA Business Activity & ISIC-4 Classification",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Select your primary commercial activity licensed under MISA foreign investment regulations and Vision 2030 priority sectors.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandedActivity,
                                onExpandedChange = { expandedActivity = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = selectedActivity,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Primary MISA Licensed Activity") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedActivity) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedActivity,
                                    onDismissRequest = { expandedActivity = false }
                                ) {
                                    activityOptions.forEach { act ->
                                        DropdownMenuItem(
                                            text = { Text(act, fontSize = 13.sp) },
                                            onClick = {
                                                selectedActivity = act
                                                expandedActivity = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Selected Activity Regulatory Benefits:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = SaudiGreenDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• 100% foreign ownership authorized under MISA Foreign Investment Law.\n• Immediate eligibility for regional tenders and Etimad government procurement.\n• Fast-track Wathq CR and Chamber of Commerce automated linkage.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        4 -> {
                            // Step 4: Supporting Documents
                            Text(
                                text = "4. Supporting Documents & Statutory Declarations",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Confirm attachment of mandatory parent company documents, financial statements, and board resolutions.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { parentCertChecked = !parentCertChecked }
                                ) {
                                    Checkbox(checked = parentCertChecked, onCheckedChange = { parentCertChecked = it })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Parent Company Certificate of Incorporation (Attested by Saudi Embassy)", fontSize = 12.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { financialsChecked = !financialsChecked }
                                ) {
                                    Checkbox(checked = financialsChecked, onCheckedChange = { financialsChecked = it })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Audited Financial Statements for Past Fiscal Year", fontSize = 12.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { boardResChecked = !boardResChecked }
                                ) {
                                    Checkbox(checked = boardResChecked, onCheckedChange = { boardResChecked = it })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Board of Directors Resolution approving KSA Branch / Subsidiary Setup", fontSize = 12.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { poaChecked = !poaChecked }
                                ) {
                                    Checkbox(checked = poaChecked, onCheckedChange = { poaChecked = it })
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Power of Attorney (PoA) notarized for General Manager in KSA", fontSize = 12.sp)
                                }
                            }
                        }

                        5 -> {
                            // Step 5: Review & Submit
                            Text(
                                text = "5. Review Application Summary & Submit to MISA",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please review your investment license dossier before transmitting to the Ministry of Investment electronic portal.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3FAF5)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("Company: $englishName ($arabicName)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Legal Form: $selectedStructure", fontSize = 12.sp)
                                    Text("Stated Capital: SAR ${statedCapitalText.toLongOrNull() ?: 2500000}", fontSize = 12.sp)
                                    Text("Foreign Equity: ${foreignOwnership.toInt()}%", fontSize = 12.sp)
                                    Text("Activity: $selectedActivity", fontSize = 12.sp, color = SaudiGreenDark)
                                    Text("Head Office: $headOfficeCity", fontSize = 12.sp)
                                    Text("Signatory: $representativeName (ID: $nationalId)", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Navigation Buttons inside card / bottom of card
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (currentStep > 1) {
                            OutlinedButton(
                                onClick = { currentStep-- },
                                modifier = Modifier.testTag("btn_misa_step_prev")
                            ) {
                                Text("Previous")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        if (currentStep < 5) {
                            Button(
                                onClick = { currentStep++ },
                                colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                                modifier = Modifier.testTag("btn_misa_step_next")
                            ) {
                                Text("Continue")
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        } else {
                            Button(
                                onClick = {
                                    val cap = statedCapitalText.toDoubleOrNull() ?: 2500000.0
                                    viewModel.createNewDossier(
                                        companyName = englishName,
                                        arabicName = arabicName,
                                        licenseType = selectedActivity,
                                        legalStructure = selectedStructure,
                                        capitalSar = cap,
                                        foreignOwnership = foreignOwnership.toInt()
                                    )
                                    generatedCrNumber = "1010" + (100000 + (Math.random() * 899999).toInt())
                                    showSuccessDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                                contentColor = Color(0xFF1E241F),
                                modifier = Modifier.testTag("btn_submit_misa_application")
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Submit MISA Application & Issue CR", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                    modifier = Modifier.testTag("btn_success_dismiss")
                ) {
                    Text("View in Dossiers & Milestones")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("MISA License Issued Successfully!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Your Saudi Ministry of Investment (MISA) license application has been successfully processed and submitted.")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3FAF5)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Company: $englishName", fontWeight = FontWeight.Bold)
                            Text("Generated CR: $generatedCrNumber", fontWeight = FontWeight.Bold, color = SaudiGreenDark)
                            Text("Status: Under Review / Wathq Integration Active", fontSize = 12.sp)
                        }
                    }
                }
            }
        )
    }
}
