package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCompanyDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    var englishName by remember { mutableStateOf("Hyperion Robotics KSA") }
    var arabicName by remember { mutableStateOf("شركة هايبريون للروبوتات السعودية المحدودة") }
    var capitalText by remember { mutableStateOf("1000000") }
    var foreignOwnership by remember { mutableFloatStateOf(100f) }

    val licenseOptions = listOf(
        "Services & IT Consulting",
        "Industrial & Advanced Manufacturing",
        "Regional Headquarters (RHQ)",
        "Trading & Wholesale",
        "FinTech & Innovation"
    )
    var expandedLicense by remember { mutableStateOf(false) }
    var selectedLicense by remember { mutableStateOf(licenseOptions[0]) }

    val structureOptions = listOf(
        "Limited Liability Company (LLC)",
        "Simplified Joint Stock Company (SJSC)",
        "Branch of Foreign Company"
    )
    var expandedStructure by remember { mutableStateOf(false) }
    var selectedStructure by remember { mutableStateOf(structureOptions[0]) }

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val capital = capitalText.toDoubleOrNull() ?: 500000.0
                    viewModel.createNewDossier(
                        companyName = englishName,
                        arabicName = arabicName,
                        licenseType = selectedLicense,
                        legalStructure = selectedStructure,
                        capitalSar = capital,
                        foreignOwnership = foreignOwnership.toInt()
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                modifier = Modifier.testTag("submit_new_company_button")
            ) {
                Text("Issue MISA Application & CR")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = {
            Text("Register New Saudi Entity (MISA)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = englishName,
                    onValueChange = { englishName = it },
                    label = { Text("English Trade Name") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = arabicName,
                    onValueChange = { arabicName = it },
                    label = { Text("Arabic Commercial Name (الاسم التجاري)") },
                    leadingIcon = { Icon(Icons.Default.Language, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // License Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedLicense,
                    onExpandedChange = { expandedLicense = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedLicense,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("MISA Investment License Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLicense) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLicense,
                        onDismissRequest = { expandedLicense = false }
                    ) {
                        licenseOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedLicense = option
                                    expandedLicense = false
                                }
                            )
                        }
                    }
                }

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
                        label = { Text("Legal Form (Corporate Structure)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStructure) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStructure,
                        onDismissRequest = { expandedStructure = false }
                    ) {
                        structureOptions.forEach { option ->
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
                    value = capitalText,
                    onValueChange = { capitalText = it },
                    label = { Text("Stated Capital (SAR)") },
                    leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Foreign Ownership Percentage Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Foreign Ownership Share", fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
            }
        }
    )
}
