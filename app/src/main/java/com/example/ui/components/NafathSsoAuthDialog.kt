package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.nafath.NafathAuthStatus
import com.example.data.nafath.NafathUserIdentity
import com.example.ui.MainViewModel
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NafathSsoAuthDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val nafathSession by viewModel.nafathSession.collectAsStateWithLifecycle()
    val authenticatedUser by viewModel.authenticatedNafathUser.collectAsStateWithLifecycle()

    var inputNationalId by remember { mutableStateOf("1089334215") }
    var countdownSeconds by remember { mutableIntStateOf(180) }

    // Live countdown effect when waiting for user action in Nafath mobile app
    LaunchedEffect(nafathSession.status) {
        if (nafathSession.status == NafathAuthStatus.WAITING_USER_ACTION) {
            countdownSeconds = 180
            while (countdownSeconds > 0) {
                delay(1000)
                countdownSeconds -= 1
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(SaudiGreenDark, SaudiGreen)
                                )
                            )
                            .border(1.5.dp, SaudiGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Nafath SSO",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Nafath SSO",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "نفاذ الوطني الموحد",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGoldDark
                            )
                        }
                        Text(
                            text = "National Information Center (NIC) Gateway",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (authenticatedUser == null) {
                    // ==========================================
                    // STEP 1 & 2: UNAUTHENTICATED LOGIN FLOW
                    // ==========================================
                    Text(
                        text = "Securely authenticate your Saudi National Identity (هوية وطنية) or Resident Permit (إقامة) to access government-regulated portals (Wathq, ZATCA, Qiwa, Balady & MISA CR).",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preset Quick Demo Personas
                    Text(
                        text = "Quick Demo Personas (انقر للاختيار السريع):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SaudiGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = inputNationalId == "1089334215",
                            onClick = { inputNationalId = "1089334215" },
                            label = { Text("H.E. Sultan Al-Saud (Citizen / 1089334215)", fontSize = 10.sp) },
                            leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(13.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaudiGreen,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = inputNationalId == "2194883012",
                            onClick = { inputNationalId = "2194883012" },
                            label = { Text("Tarek Al-Khalidi (Resident / 2194883012)", fontSize = 10.sp) },
                            leadingIcon = { Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(13.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaudiGold,
                                selectedLabelColor = Color(0xFF1E241F),
                                selectedLeadingIconColor = Color(0xFF1E241F)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = inputNationalId,
                        onValueChange = { if (it.length <= 10) inputNationalId = it },
                        label = { Text("National ID / Iqama (رقم الهوية أو الإقامة)") },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SaudiGreen) },
                        supportingText = {
                            Text(
                                text = "10 digits: starts with 1 for Citizens, 2 for Residents (${inputNationalId.length}/10)",
                                fontSize = 10.sp
                            )
                        },
                        isError = inputNationalId.isNotEmpty() && inputNationalId.length < 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("nafath_national_id_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Nafath Mobile Challenge Screen (Official 2-Digit Flow)
                    if (nafathSession.status == NafathAuthStatus.WAITING_USER_ACTION) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .testTag("nafath_challenge_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2417)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "NAFATH MOBILE CHALLENGE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SaudiGold,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                    Text(
                                        text = "${countdownSeconds}s",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (countdownSeconds < 30) StatusDanger else SaudiGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "افتح تطبيق نفاذ على هاتفك واختر الرقم أدناه",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Open Nafath App & tap the 2-digit number",
                                    fontSize = 11.sp,
                                    color = Color(0xFFBCE0CB)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Official 2-Digit Circle Display
                                Box(
                                    modifier = Modifier
                                        .size(86.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(SaudiGold, SaudiGoldDark)
                                            )
                                        )
                                        .border(3.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${nafathSession.verificationCode}",
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFF0F1E15)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "TX: ${nafathSession.transactionId}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF9EC4AF)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Simulator action buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.completeNafathVerification(approved = true) },
                                        colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("nafath_approve_sim_btn")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Approve in Nafath", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedButton(
                                        onClick = { viewModel.completeNafathVerification(approved = false) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .testTag("nafath_reject_sim_btn")
                                    ) {
                                        Text("Reject", color = StatusDanger, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    } else if (nafathSession.status == NafathAuthStatus.BIOMETRIC_VERIFICATION) {
                        // Biometric progress state
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = SaudiGreen.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = SaudiGreen,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Verifying NIC Facial Biometrics...",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SaudiGreenDark
                                    )
                                    Text(
                                        text = "جاري التحقق من البصمة ومطابقة الهوية الرقمية",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        // Initiate button
                        Button(
                            onClick = {
                                viewModel.initiateNafathAuth(inputNationalId)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("nafath_initiate_auth_btn")
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Nafath Verification Push", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    // Error Message Display
                    if (nafathSession.errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(StatusDanger.copy(alpha = 0.1f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = StatusDanger, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = nafathSession.errorMessage!!,
                                color = StatusDanger,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                } else {
                    // ==========================================
                    // STEP 3: AUTHENTICATED DIGITAL IDENTITY CARD
                    // ==========================================
                    AuthenticatedIdentityCard(
                        identity = authenticatedUser!!,
                        onSignOut = {
                            viewModel.logoutNafath()
                            Toast.makeText(context, "Signed out of Nafath SSO", Toast.LENGTH_SHORT).show()
                        },
                        onContinue = onDismiss
                    )
                }
            }
        }
    )
}

@Composable
fun AuthenticatedIdentityCard(
    identity: NafathUserIdentity,
    onSignOut: () -> Unit,
    onContinue: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Official Saudi Digital Identity Card Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nafath_digital_id_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1F14)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "KINGDOM OF SAUDI ARABIA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "المملكة العربية السعودية • مركز المعلومات الوطني",
                            fontSize = 10.sp,
                            color = Color(0xFFC7E2D2)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StatusSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("MOHTHAQ (موثق)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar, Arabic & English Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(SaudiGreenDark, SaudiGreen)
                                )
                            )
                            .border(2.dp, SaudiGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = identity.arabicFullName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = identity.englishFullName,
                            fontSize = 11.sp,
                            color = SaudiGold,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "National ID: ${identity.nationalId} • ${identity.nationality}",
                            fontSize = 10.sp,
                            color = Color(0xFFBACDC1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Details: DOB & National Address
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DOB (Hijri/Gregorian)", fontSize = 9.sp, color = Color(0xFF9EBFAC))
                        Text("${identity.dateOfBirthHijri} (${identity.dateOfBirthGregorian})", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Signatory Role", fontSize = 9.sp, color = Color(0xFF9EBFAC))
                        Text(if (identity.isAuthorizedSignatory) "Authorized Signatory (مفوض)" else "Standard Partner", fontSize = 10.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = identity.nationalAddress,
                        fontSize = 10.sp,
                        color = Color(0xFFE2EFE7),
                        lineHeight = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Authorized Entities
                Text("Authorized Commercial Entities (الكيانات التجارية المرخصة):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SaudiGold)
                Spacer(modifier = Modifier.height(4.dp))
                identity.authorizedCompanies.forEach { comp ->
                    Row(
                        modifier = Modifier.padding(vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CorporateFare, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = comp, fontSize = 10.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cryptographic Session Token
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "JWT: ${identity.authToken.take(24)}...",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFCADCCE)
                            )
                        }
                        Text("Active 12h", fontSize = 9.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Regulated Services Unlocked Checklist
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Regulated Services Status (الخدمات الحكومية المفعلة):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaudiGreen
                )
                Spacer(modifier = Modifier.height(6.dp))
                RegulatedServiceItem("Wathq Commercial Registry (سجل تجاري)", "Active & Verified (1010776412)")
                RegulatedServiceItem("ZATCA Fatoora Phase 2 (هيئة الزكاة والضريبة)", "Active & Cleared (TIN: 310582910400003)")
                RegulatedServiceItem("Qiwa Labor & Saudization (منصة قوى)", "Platinum Tier (62.5% Saudization)")
                RegulatedServiceItem("Balady & Civil Defense (بلدي والدفاع المدني)", "Commercial Operating License Valid")
                RegulatedServiceItem("MISA Investor Portal (وزارة الاستثمار)", "100% Foreign Ownership Authorized")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom CTA Buttons
        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nafath_continue_btn")
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Proceed with Government Services", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedButton(
            onClick = onSignOut,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nafath_sign_out_btn")
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Sign Out / Switch National Identity", fontSize = 11.sp)
        }
    }
}

@Composable
fun RegulatedServiceItem(title: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
        Text(status, fontSize = 9.sp, color = SaudiGreenDark, fontWeight = FontWeight.Bold)
    }
}
