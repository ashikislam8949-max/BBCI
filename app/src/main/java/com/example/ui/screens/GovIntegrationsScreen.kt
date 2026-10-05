package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.nafath.NafathAuthStatus
import com.example.ui.MainViewModel
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning

@Composable
fun GovIntegrationsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedPortalTab by remember { mutableIntStateOf(0) }
    val portalTabs = listOf(
        "Nafath (نفاذ)",
        "Wathq (واثق MC)",
        "ZATCA (الزكاة)",
        "Qiwa (قوى MHRSD)",
        "Balady & GOSI"
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
                title = "Saudi Gov Unified Gateways",
                subtitle = "Direct interconnection with Nafath SSO, Wathq, ZATCA, Qiwa, GOSI, & Balady"
            )
        }

        // Horizontal Category Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedPortalTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                portalTabs.forEachIndexed { index, title ->
                    val isSelected = selectedPortalTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedPortalTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SaudiGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }

        item {
            when (selectedPortalTab) {
                0 -> NafathSsoSection(viewModel)
                1 -> WathqCrSection(viewModel)
                2 -> ZatcaSection(viewModel)
                3 -> QiwaSection(viewModel)
                4 -> BaladyGosiSection(viewModel)
            }
        }
    }
}

/**
 * 1. Nafath National Single Sign-On (نفاذ الوطني الموحد)
 */
@Composable
fun NafathSsoSection(viewModel: MainViewModel) {
    val nafathSession by viewModel.nafathSession.collectAsStateWithLifecycle()
    val authenticatedUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()
    var inputId by remember { mutableStateOf("1089334215") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("nafath_sso_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Nafath Header with Emerald/Gold Crest
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SaudiGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Nafath Identity",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Nafath SSO (نفاذ الوطني الموحد)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "National Information Center (NIC) Gateway",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (authenticatedUser != null) StatusSuccess.copy(alpha = 0.15f)
                            else Color(0xFFF1F5F9)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (authenticatedUser != null) "Verified (موثق)" else "Not Authenticated",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (authenticatedUser != null) StatusSuccess else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (authenticatedUser == null) {
                // When not authenticated: Login with Saudi ID / Iqama
                Text(
                    text = "Sign in using your Saudi National ID (هوية وطنية) or Resident Permit (إقامة). You will receive a two-digit push verification code to approve in the official Nafath App.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = inputId,
                    onValueChange = { if (it.length <= 10) inputId = it },
                    label = { Text("National ID / Iqama (رقم الهوية أو الإقامة)") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (nafathSession.status == NafathAuthStatus.WAITING_USER_ACTION) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 2-Digit Verification Code Display (Official Nafath UX)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF0F2B1D), Color(0xFF1E4631))
                                )
                            )
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "OPEN NAFATH APP & TAP NUMBER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "افتح تطبيق نفاذ واختر الرقم أدناه لتأكيد الهوية",
                                fontSize = 11.sp,
                                color = Color(0xFFD3E6DB)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(SaudiGold)
                                    .border(3.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${nafathSession.verificationCode}",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF132018)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Transaction ID: ${nafathSession.transactionId}",
                                fontSize = 10.sp,
                                color = Color(0xFFB8D3C3)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.completeNafathVerification(approved = true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("nafath_approve_sim_button"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Simulate Approve in Nafath", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.completeNafathVerification(approved = false) },
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .testTag("nafath_reject_sim_button"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Reject", color = Color(0xFFEF4444), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                } else if (nafathSession.status == NafathAuthStatus.BIOMETRIC_VERIFICATION) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SaudiGreen.copy(alpha = 0.1f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = SaudiGreen, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Verifying facial biometrics with NIC (جاري التحقق من البصمة)...", fontSize = 12.sp, color = SaudiGreen)
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.initiateNafathAuth(inputId) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("initiate_nafath_button")
                    ) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Nafath Verification Request", fontWeight = FontWeight.Bold)
                    }
                }

                if (nafathSession.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = nafathSession.errorMessage!!,
                        color = StatusDanger,
                        fontSize = 11.sp
                    )
                }
            } else {
                // Authenticated Citizen/Resident Identity Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0C3B24), Color(0xFF14532D))
                            )
                        )
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
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SaudiGold.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "NAFATH VERIFIED IDENTITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaudiGold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = "Token Active",
                                fontSize = 10.sp,
                                color = Color(0xFFBBF7D0),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = authenticatedUser!!.arabicFullName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = authenticatedUser!!.englishFullName,
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("National ID", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                Text(authenticatedUser!!.nationalId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column {
                                Text("Birthdate (Hijri)", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                Text(authenticatedUser!!.dateOfBirthHijri, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column {
                                Text("Absher Mobile", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                Text(authenticatedUser!!.absherMobile, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = authenticatedUser!!.nationalAddress,
                                fontSize = 10.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Commercial Mandates & Signatory Authorization (صلاحيات التوقيع):",
                            fontSize = 10.sp,
                            color = SaudiGold,
                            fontWeight = FontWeight.Bold
                        )
                        authenticatedUser!!.authorizedCompanies.forEach { comp ->
                            Text("• $comp", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.logoutNafath() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Disconnect Nafath Session (تسجيل الخروج من نفاذ)", color = StatusDanger, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 2. Wathq (واثق) Ministry of Commerce API
 */
@Composable
fun WathqCrSection(viewModel: MainViewModel) {
    var crInput by remember { mutableStateOf("1010892341") }
    val result by viewModel.wathqResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isWathqLoading.collectAsStateWithLifecycle()
    val authenticatedUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("wathq_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
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
                            .background(Color(0xFF0284C7).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Wathq Gateway (واثق)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Ministry of Commerce Live Commercial Registry API", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Instant real-time validation of Saudi Commercial Registrations (CR), electronic Articles of Association (AoA), Chamber of Commerce membership, and authorized board directors.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Nafath Signatory Status Indicator
            if (authenticatedUser != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SaudiGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Signatory Verified: ${authenticatedUser!!.arabicFullName} (National ID: ${authenticatedUser!!.nationalId})",
                        fontSize = 11.sp,
                        color = SaudiGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Authorized Entity:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = crInput == "1010776412",
                        onClick = { crInput = "1010776412" },
                        label = { Text("sbtech.sa (1010776412)", fontSize = 10.sp) }
                    )
                    FilterChip(
                        selected = crInput == "1010892341",
                        onClick = { crInput = "1010892341" },
                        label = { Text("Apex Cloud (1010892341)", fontSize = 10.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = crInput,
                onValueChange = { crInput = it },
                label = { Text("10-Digit Commercial Registration (رقم السجل التجاري)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { viewModel.queryWathq(crInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("query_wathq_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Querying Wathq Network...")
                } else {
                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Verify CR with Wathq API")
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF0284C7)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = Color(0xFF0284C7),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Querying Wathq Ministry of Commerce Registry...",
                        fontSize = 11.sp,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                WathqSkeletonLoader()
            } else if (result != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(result!!.companyName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(result!!.status, color = StatusSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(result!!.arabicName, fontSize = 12.sp, color = SaudiGreen, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Legal Form: ${result!!.legalForm}", fontSize = 11.sp)
                        Text("• Stated Capital: SAR ${result!!.capitalSar.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("• Issue / Expiry: ${result!!.issueDate} to ${result!!.expiryDate}", fontSize = 11.sp)
                        Text("• Activity (ISIC): ${result!!.mainActivity}", fontSize = 11.sp)
                        Text("• General Manager: ${result!!.managerName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("• Chamber of Commerce No: ${result!!.chamberOfCommerceNo}", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/**
 * 3. ZATCA Tax & Fatoora Phase 2 API
 */
@Composable
fun ZatcaSection(viewModel: MainViewModel) {
    var tinInput by remember { mutableStateOf("310499281700003") }
    val result by viewModel.zatcaResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isZatcaLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("zatca_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
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
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("ZATCA Tax & Fatoora Phase 2", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Zakat, Tax and Customs Authority Gateway", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Verify 15% VAT active status, 20% Corporate Income Tax file, and cryptographic E-Invoicing (Fatoora Phase 2) clearance integration.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = tinInput,
                onValueChange = { tinInput = it },
                label = { Text("15-Digit Tax Identification Number (الرقم الضريبي TIN)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { viewModel.queryZatca(tinInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("query_zatca_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Querying ZATCA Gateway...")
                } else {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Query ZATCA Compliance Status")
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
                        Text(result!!.taxPayerName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("TIN: ${result!!.tinNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• VAT Status: ${result!!.vatStatus}", fontSize = 11.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                        Text("• Fatoora Phase 2 E-Invoicing: " + if (result!!.fatooraPhase2Integrated) "Cryptographically Linked & Approved" else "Pending Setup", fontSize = 11.sp)
                        Text("• Zakat / Tax Certificate Expiry: ${result!!.zakatCertificateExpiry}", fontSize = 11.sp)
                        Text("• Statutory Tax Rating: ${result!!.complianceRating}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                    }
                }
            }
        }
    }
}

/**
 * 4. Qiwa (قوى) Labor & MHRSD API
 */
@Composable
fun QiwaSection(viewModel: MainViewModel) {
    var estInput by remember { mutableStateOf("7-189241") }
    val result by viewModel.qiwaResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isQiwaLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("qiwa_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE11D48).copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Qiwa & Mudad Platform (منصة قوى ومدد)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Ministry of Human Resources Labor & Nitaqat API", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Live workforce Saudization quota checks, Nitaqat tier allocation, instant work visa quota releases, and Mudad Wages Protection System (WPS) compliance.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = estInput,
                onValueChange = { estInput = it },
                label = { Text("Establishment Number (رقم المنشأة في وزارة الموارد البشرية)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { viewModel.queryQiwa(estInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("query_qiwa_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Querying Qiwa Portal...")
                } else {
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Check Qiwa & Saudization Quota")
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
                        Text(result!!.establishmentName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("File No: ${result!!.establishmentNo}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Nitaqat Tier: ${result!!.nitaqatTier}", fontSize = 11.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                        Text("• Saudization Ratio: ${result!!.saudizationRatio}% (${result!!.saudiEmployees} Saudis / ${result!!.totalEmployees} Total)", fontSize = 11.sp)
                        Text("• Available Instant Work Visas: ${result!!.availableVisas} Visas Available", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaudiGreen)
                        Text("• Mudad Wages Protection: ${result!!.mudadWpsCompliance}", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/**
 * 5. Balady & GOSI Portals
 */
@Composable
fun BaladyGosiSection(viewModel: MainViewModel) {
    var licInput by remember { mutableStateOf("4409182310") }
    val result by viewModel.baladyResult.collectAsStateWithLifecycle()
    val isLoading by viewModel.isBaladyLoading.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("balady_gosi_section"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706).copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocationCity, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Balady Municipal & GOSI Safety Gate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Commercial Municipal Licenses & Social Security", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Direct connection with Balady (وزارة البلديات والإسكان) for commercial premises permits, Civil Defense safety, and GOSI company social security certificates.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = licInput,
                onValueChange = { licInput = it },
                label = { Text("Balady License Number (رقم الرخصة البلدية)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { viewModel.queryBalady(licInput) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("query_balady_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Querying Balady Registry...")
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Query Balady & Safety Status")
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
                        Text(result!!.activityName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("License No: ${result!!.licenseNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Status: ${result!!.status}", fontSize = 11.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                        Text("• Registered Location: ${result!!.premiseAddress}", fontSize = 11.sp)
                        Text("• Civil Defense Safety Approval (الدفاع المدني): " + if (result!!.civilDefenseApproved) "Approved & Cleared" else "Action Required", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text("• Valid Until: ${result!!.expiryDate}", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WathqSkeletonLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "wathq_skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_anim"
    )

    val skeletonColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(skeletonColor)
                )
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(skeletonColor)
                )
            }
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(skeletonColor)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(skeletonColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(skeletonColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(skeletonColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(skeletonColor)
            )
        }
    }
}
