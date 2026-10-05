package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.consulting.InvestmentExpert
import com.example.data.consulting.InvestorAdvisoryData
import com.example.data.consulting.InvestorInterestProfile
import com.example.data.consulting.SaudiInvestmentOpportunity
import com.example.data.consulting.SaudiSectorInsight
import com.example.data.local.ConsultationEntity
import com.example.ui.MainViewModel
import com.example.ui.components.SaudiBrandHeader
import com.example.ui.components.StatusPill
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGoldDark
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning

@Composable
fun WeConsultingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val consultations by viewModel.consultations.collectAsStateWithLifecycle()
    val investorProfile by viewModel.investorProfile.collectAsStateWithLifecycle()
    val selectedExpertForBooking by viewModel.selectedExpertForBooking.collectAsStateWithLifecycle()
    val aiSectorReport by viewModel.aiSectorReport.collectAsStateWithLifecycle()
    val isAiSectorReportLoading by viewModel.isAiSectorReportLoading.collectAsStateWithLifecycle()

    var activeTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        "Experts & Booking",
        "Investment Interests",
        "Sector Opportunities",
        "My Consultations (${consultations.size})"
    )

    var expertBookingTarget by remember { mutableStateOf<InvestmentExpert?>(null) }
    var presetSectorForBooking by remember { mutableStateOf<String?>(null) }
    var presetOpportunityForBooking by remember { mutableStateOf<String?>(null) }

    // When an expert is selected via viewModel state
    if (selectedExpertForBooking != null && expertBookingTarget == null) {
        expertBookingTarget = selectedExpertForBooking
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Sticky Header & Segmented Tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SaudiBrandHeader(
                title = "MISA International Advisory Hub",
                subtitle = "Bespoke Saudi expansion advisory, certified investment counsel & Vision 2030 opportunity matching"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ScrollableTabRow(
                selectedTabIndex = activeTab,
                containerColor = Color.Transparent,
                contentColor = SaudiGreen,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    if (activeTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = SaudiGold,
                            height = 3.dp
                        )
                    }
                },
                divider = { HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f)) },
                modifier = Modifier.testTag("investor_advisory_tabs")
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (activeTab == index) SaudiGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.testTag("advisory_tab_$index")
                    )
                }
            }
        }

        // Active Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (activeTab) {
                0 -> ExpertsDirectorySection(
                    experts = InvestorAdvisoryData.certifiedExperts,
                    onBookExpert = { expert ->
                        expertBookingTarget = expert
                        presetSectorForBooking = null
                        presetOpportunityForBooking = null
                    },
                    onGoToInterests = { activeTab = 1 }
                )

                1 -> InvestorInterestsSection(
                    profile = investorProfile,
                    onSaveProfile = { updated ->
                        viewModel.updateInvestorProfile(updated)
                    },
                    onExploreOpportunities = {
                        activeTab = 2
                    }
                )

                2 -> SectorOpportunitiesSection(
                    profile = investorProfile,
                    opportunities = InvestorAdvisoryData.curatedOpportunities,
                    sectors = InvestorAdvisoryData.sectorInsights,
                    aiReport = aiSectorReport,
                    isAiLoading = isAiSectorReportLoading,
                    onRunAiAnalysis = { sec, cap, struc, obj ->
                        viewModel.runAiSectorRecommendation(sec, cap, struc, obj)
                    },
                    onBookOpportunity = { opp ->
                        val expert = InvestorAdvisoryData.certifiedExperts.find { it.id == opp.recommendedExpertId }
                            ?: InvestorAdvisoryData.certifiedExperts.first()
                        expertBookingTarget = expert
                        presetSectorForBooking = opp.sector
                        presetOpportunityForBooking = opp.title
                    }
                )

                3 -> BookedConsultationsSection(
                    consultations = consultations,
                    onBookNew = { activeTab = 0 },
                    onCancelConsultation = { id ->
                        viewModel.cancelConsultation(id)
                    }
                )
            }
        }
    }

    // Expert Booking Modal Dialog
    expertBookingTarget?.let { expert ->
        ExpertBookingModal(
            expert = expert,
            investorProfile = investorProfile,
            initialSector = presetSectorForBooking ?: investorProfile.selectedSectors.firstOrNull() ?: "Regional Headquarters (RHQ)",
            initialOpportunityTitle = presetOpportunityForBooking,
            onDismiss = {
                expertBookingTarget = null
                viewModel.selectExpertForBooking(null)
            },
            onConfirm = { name, email, company, sector, format, slot, tier, notes ->
                viewModel.bookExpertConsultation(
                    expert = expert,
                    investorName = name,
                    investorEmail = email,
                    companyName = company,
                    targetSector = sector,
                    meetingFormat = format,
                    scheduledDate = slot,
                    investmentTier = tier,
                    agendaNotes = notes
                )
                expertBookingTarget = null
                activeTab = 3 // Jump to "My Consultations" to see confirmed booking!
            }
        )
    }
}

// ==========================================
// 1. EXPERTS DIRECTORY & BOOKING SECTION
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpertsDirectorySection(
    experts: List<InvestmentExpert>,
    onBookExpert: (InvestmentExpert) -> Unit,
    onGoToInterests: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All Specialties") }
    val filters = listOf("All Specialties", "MISA Licensing", "RHQ & Tax", "Giga-Projects", "Labor & GRO")

    val filteredExperts = remember(selectedFilter) {
        when (selectedFilter) {
            "MISA Licensing" -> experts.filter { it.specialties.any { s -> s.contains("Licensing", ignoreCase = true) || s.contains("Foreign", ignoreCase = true) } }
            "RHQ & Tax" -> experts.filter { it.specialties.any { s -> s.contains("RHQ", ignoreCase = true) || s.contains("Tax", ignoreCase = true) } }
            "Giga-Projects" -> experts.filter { it.specialties.any { s -> s.contains("NEOM", ignoreCase = true) || s.contains("Giga", ignoreCase = true) || s.contains("Supply", ignoreCase = true) } }
            "Labor & GRO" -> experts.filter { it.specialties.any { s -> s.contains("Qiwa", ignoreCase = true) || s.contains("GRO", ignoreCase = true) || s.contains("Governance", ignoreCase = true) } }
            else -> experts
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Advisory Callout
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("advisory_hero_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101B14))
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
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SaudiGold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MISA CERTIFIED ADVISORY DESK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGold,
                                letterSpacing = 1.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SaudiGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Fast-Track FDI", color = SaudiGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1-on-1 Consultations with Saudi Regulatory Leaders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Book confidential strategic sessions with former MISA directors, licensed Saudi corporate attorneys, and tax partners to fast-track your 100% foreign ownership CR and RHQ exemptions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD1E3D7)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onGoToInterests,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SaudiGold),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(SaudiGold, SaudiGoldDark))),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Set Investment Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Specialty Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaudiGreen,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("filter_chip_$filter")
                    )
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accredited Investment Advisors (${filteredExperts.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Live Availability",
                    fontSize = 11.sp,
                    color = StatusSuccess,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Experts List
        items(filteredExperts, key = { it.id }) { expert ->
            ExpertCard(
                expert = expert,
                onBook = { onBookExpert(expert) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpertCard(
    expert: InvestmentExpert,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("expert_card_${expert.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // Avatar / Initials Box
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SaudiGreenDark, SaudiGreen)
                                )
                            )
                            .border(1.5.dp, SaudiGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = expert.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = expert.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = expert.arabicName,
                            fontSize = 11.sp,
                            color = SaudiGoldDark,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = expert.title,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Rating Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaudiGold.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${expert.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFB48318)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bio snippet
            Text(
                text = expert.bio,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Credentials & Office
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${expert.experienceYears}+ yrs exp", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = expert.languages.joinToString(", "), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${expert.completedSessions} sessions", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Specialties Flow Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                expert.specialties.forEach { spec ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SaudiGreen.copy(alpha = 0.08f))
                            .border(0.8.dp, SaudiGreen.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(text = spec, fontSize = 10.sp, color = SaudiGreenDark, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            // Booking action row with next available slot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Next Available Slot", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = expert.availableSlots.firstOrNull() ?: "Upon Request",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                }

                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("book_expert_btn_${expert.id}")
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Book Session", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// 2. INVESTOR INTERESTS SPECIFICATION
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InvestorInterestsSection(
    profile: InvestorInterestProfile,
    onSaveProfile: (InvestorInterestProfile) -> Unit,
    onExploreOpportunities: () -> Unit
) {
    val context = LocalContext.current
    var investorName by remember { mutableStateOf(profile.investorName) }
    var companyName by remember { mutableStateOf(profile.companyName) }
    var corporateEmail by remember { mutableStateOf(profile.corporateEmail) }
    var homeCountry by remember { mutableStateOf(profile.homeCountry) }
    var phoneWhatsapp by remember { mutableStateOf(profile.phoneWhatsapp) }

    var selectedSectors by remember { mutableStateOf(profile.selectedSectors) }
    var capitalTier by remember { mutableStateOf(profile.capitalTier) }
    var preferredStructure by remember { mutableStateOf(profile.preferredStructure) }
    var timeHorizon by remember { mutableStateOf(profile.timeHorizon) }
    var selectedRegions by remember { mutableStateOf(profile.preferredRegions) }
    var selectedIncentives by remember { mutableStateOf(profile.desiredIncentives) }
    var specificObjectives by remember { mutableStateOf(profile.specificObjectives) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("interests_intro_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SaudiGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Investor Profile & Strategic Criteria", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            "Define your expansion interests to unlock curated Vision 2030 opportunities, tailored MISA licensing tiers, and matched sector advisors.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section A: Investor Identification
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("1. Corporate Identity & Contact", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SaudiGreen)

                    OutlinedTextField(
                        value = investorName,
                        onValueChange = { investorName = it },
                        label = { Text("Lead Investor / Executive Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SaudiGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("Parent Multinational / Firm Name") },
                        leadingIcon = { Icon(Icons.Default.CorporateFare, contentDescription = null, tint = SaudiGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = corporateEmail,
                            onValueChange = { corporateEmail = it },
                            label = { Text("Corporate Email") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SaudiGreen) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = homeCountry,
                            onValueChange = { homeCountry = it },
                            label = { Text("Headquarters Country") },
                            leadingIcon = { Icon(Icons.Default.Public, contentDescription = null, tint = SaudiGreen) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = phoneWhatsapp,
                        onValueChange = { phoneWhatsapp = it },
                        label = { Text("Phone / WhatsApp (with country code)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SaudiGreen) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Section B: Target Business Sectors
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("2. Target Investment Sectors", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SaudiGreen)
                        Text("${selectedSectors.size} Selected", fontSize = 11.sp, color = SaudiGoldDark, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        "Select all industry verticals you plan to deploy capital or services into:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InvestorAdvisoryData.availableSectors.forEach { sector ->
                            val isSelected = selectedSectors.contains(sector)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedSectors = if (isSelected) {
                                        selectedSectors - sector
                                    } else {
                                        selectedSectors + sector
                                    }
                                },
                                label = { Text(sector, fontSize = 11.sp) },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaudiGreen,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section C: Planned Capital Deployment & Structure
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("3. Capital Scale & Corporate Vehicle", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SaudiGreen)

                    Text("Planned Capital Deployment Tier:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InvestorAdvisoryData.capitalTiers.forEach { tier ->
                            val isChosen = capitalTier == tier
                            FilterChip(
                                selected = isChosen,
                                onClick = { capitalTier = tier },
                                label = { Text(tier, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaudiGold,
                                    selectedLabelColor = Color(0xFF1E241F)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Preferred Legal / Regulatory Structure:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        InvestorAdvisoryData.corporateStructures.forEach { structure ->
                            val isSelected = preferredStructure == structure
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) SaudiGreen.copy(alpha = 0.1f) else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 1.dp else 0.5.dp,
                                        color = if (isSelected) SaudiGreen else Color.LightGray.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { preferredStructure = structure }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, if (isSelected) SaudiGreen else Color.Gray, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(SaudiGreen)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(structure, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }

        // Section D: Geographic Focus & Prioritized Incentives
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("4. Geographic Hubs & Prioritized Incentives", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SaudiGreen)

                    Text("Target Saudi Regions:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InvestorAdvisoryData.targetRegions.forEach { region ->
                            val isSel = selectedRegions.contains(region)
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedRegions = if (isSel) selectedRegions - region else selectedRegions + region
                                },
                                label = { Text(region, fontSize = 10.sp) },
                                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(12.dp)) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Critical Incentives Sought:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InvestorAdvisoryData.availableIncentives.forEach { inc ->
                            val isSel = selectedIncentives.contains(inc)
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedIncentives = if (isSel) selectedIncentives - inc else selectedIncentives + inc
                                },
                                label = { Text(inc, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaudiGreenDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = specificObjectives,
                        onValueChange = { specificObjectives = it },
                        label = { Text("Brief Expansion Objectives / Vision") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            }
        }

        // Action Buttons: Save & View Recommendations
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val updated = InvestorInterestProfile(
                            investorName = investorName,
                            companyName = companyName,
                            corporateEmail = corporateEmail,
                            phoneWhatsapp = phoneWhatsapp,
                            homeCountry = homeCountry,
                            selectedSectors = selectedSectors,
                            capitalTier = capitalTier,
                            preferredStructure = preferredStructure,
                            timeHorizon = timeHorizon,
                            preferredRegions = selectedRegions,
                            desiredIncentives = selectedIncentives,
                            specificObjectives = specificObjectives
                        )
                        onSaveProfile(updated)
                        Toast.makeText(context, "Investor Profile & Criteria Saved Successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_profile_button")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save & Synchronize Investment Profile", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        val updated = InvestorInterestProfile(
                            investorName = investorName,
                            companyName = companyName,
                            corporateEmail = corporateEmail,
                            phoneWhatsapp = phoneWhatsapp,
                            homeCountry = homeCountry,
                            selectedSectors = selectedSectors,
                            capitalTier = capitalTier,
                            preferredStructure = preferredStructure,
                            timeHorizon = timeHorizon,
                            preferredRegions = selectedRegions,
                            desiredIncentives = selectedIncentives,
                            specificObjectives = specificObjectives
                        )
                        onSaveProfile(updated)
                        onExploreOpportunities()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_matched_opportunities_btn")
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF1E241F), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Tailored Sector Recommendations", color = Color(0xFF1E241F), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF1E241F), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ==========================================
// 3. SECTOR OPPORTUNITIES & RECOMMENDATIONS
// ==========================================
@Composable
fun SectorOpportunitiesSection(
    profile: InvestorInterestProfile,
    opportunities: List<SaudiInvestmentOpportunity>,
    sectors: List<SaudiSectorInsight>,
    aiReport: String?,
    isAiLoading: Boolean,
    onRunAiAnalysis: (sector: String, capital: String, structure: String, objectives: String) -> Unit,
    onBookOpportunity: (SaudiInvestmentOpportunity) -> Unit
) {
    var selectedAnalysisSector by remember {
        mutableStateOf(profile.selectedSectors.firstOrNull() ?: "Artificial Intelligence & Enterprise Cloud")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Matched Profile Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("matched_opportunities_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1A12))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PORTFOLIO FIT & RECOMMENDATION ENGINE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaudiGold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SaudiGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Vision 2030 Matched", fontSize = 10.sp, color = SaudiGold, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Curated Opportunities for ${profile.companyName.ifBlank { "Your Enterprise" }}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Targeting ${profile.capitalTier} in ${profile.selectedSectors.joinToString(", ")}. These opportunities include direct access to government tenders, tax holidays, and concessionary debt.",
                        fontSize = 11.sp,
                        color = Color(0xFFCADBCF)
                    )
                }
            }
        }

        // AI Strategic Sector Analyst Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_sector_analyst_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SaudiGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("AI Strategic Sector Advisor (Gemini Powered)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Real-time regulatory & economic feasibility briefing", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Select Sector to Appraise:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InvestorAdvisoryData.availableSectors.take(4).forEach { sec ->
                            FilterChip(
                                selected = selectedAnalysisSector == sec,
                                onClick = { selectedAnalysisSector = sec },
                                label = { Text(sec.split(" ").take(2).joinToString(" "), fontSize = 10.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            onRunAiAnalysis(
                                selectedAnalysisSector,
                                profile.capitalTier,
                                profile.preferredStructure,
                                profile.specificObjectives
                            )
                        },
                        enabled = !isAiLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_ai_sector_report_btn")
                    ) {
                        if (isAiLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Synthesizing MISA Sector Intelligence...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Generate MISA Strategic Sector Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Display AI Report Result if available
                    AnimatedVisibility(visible = aiReport != null) {
                        aiReport?.let { report ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Strategic Assessment Output", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SaudiGreen)
                                    Text("Official Guidelines Aligned", fontSize = 10.sp, color = StatusSuccess)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = report,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Available Investment Opportunities
        item {
            Text(
                text = "Key Saudi Vision 2030 Opportunities (${opportunities.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Opportunities Cards
        items(opportunities, key = { it.id }) { opp ->
            OpportunityCard(
                opportunity = opp,
                onBookConsultation = { onBookOpportunity(opp) }
            )
        }

        // Section Title: Key Sector Benchmarks & Insights
        item {
            Text(
                text = "High-Growth Sector Insights & Market Size",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(sectors, key = { it.id }) { sector ->
            SectorInsightCard(sector = sector)
        }
    }
}

@Composable
fun OpportunityCard(
    opportunity: SaudiInvestmentOpportunity,
    onBookConsultation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("opportunity_card_${opportunity.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaudiGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = opportunity.sector,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaudiGreen
                    )
                }

                // Match Score Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaudiGold.copy(alpha = 0.18f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${opportunity.matchScore}% Match",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF946200)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = opportunity.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = opportunity.arabicTitle,
                fontSize = 11.sp,
                color = SaudiGoldDark,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = opportunity.summary,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricPillBox(
                    label = "Capital Scale",
                    value = opportunity.capitalRequired,
                    modifier = Modifier.weight(1f)
                )
                MetricPillBox(
                    label = "Projected IRR",
                    value = opportunity.projectedIrr,
                    valueColor = StatusSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Incentives summary box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF9FAFB))
                    .border(0.6.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Policy, contentDescription = null, tint = SaudiGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Government Incentives & Tax Relief", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SaudiGreenDark)
                        Text(opportunity.taxIncentives, fontSize = 10.sp, color = Color(0xFF4B5563))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location & Sponsoring Agency
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = opportunity.location, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                    onClick = onBookConsultation,
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("book_opp_btn_${opportunity.id}")
                ) {
                    Text("Consult on Opportunity", color = Color(0xFF1E241F), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MetricPillBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}

@Composable
fun SectorInsightCard(sector: SaudiSectorInsight) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(sector.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(sector.arabicName, fontSize = 10.sp, color = SaudiGoldDark)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SaudiGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(sector.cagrGrowth, color = SaudiGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(sector.overview, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Market: ${sector.marketValueSar}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = SaudiGreen)
                Text("Licensing: ${sector.recommendedMisaLicense.split(" ").take(2).joinToString(" ")}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ==========================================
// 4. BOOKED CONSULTATIONS SECTION
// ==========================================
@Composable
fun BookedConsultationsSection(
    consultations: List<ConsultationEntity>,
    onBookNew: () -> Unit,
    onCancelConsultation: (Int) -> Unit
) {
    val context = LocalContext.current
    var showJoinDialogForConsultation by remember { mutableStateOf<ConsultationEntity?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Scheduled Advisory Sessions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-time appointments stored securely in local database",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onBookNew,
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("book_new_session_btn")
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Book Session", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (consultations.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SaudiGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Handshake, contentDescription = null, tint = SaudiGold, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Consultations Booked Yet", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Select from our accredited MISA regulatory leaders and tax partners to schedule your Kingdom entry session.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onBookNew,
                            colors = ButtonDefaults.buttonColors(containerColor = SaudiGold),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Browse Advisory Experts", color = Color(0xFF1E241F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            items(consultations, key = { it.id }) { consult ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("consultation_item_${consult.id}"),
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
                                text = consult.serviceRequested,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGreen,
                                modifier = Modifier.weight(1f)
                            )
                            StatusPill(status = consult.status)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${consult.investorName} (${consult.investorEmail})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Slot: ${consult.scheduledDate}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SaudiGoldDark
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CorporateFare, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Target: ${consult.targetSector}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (consult.advisoryNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = consult.advisoryNotes,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Actions Row: Join Lounge, Add to Calendar, Cancel
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { showJoinDialogForConsultation = consult },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("join_meeting_btn_${consult.id}")
                                ) {
                                    Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Join Lounge", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        Toast.makeText(context, "Consultation session added to system calendar!", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Calendar", fontSize = 10.sp)
                                }
                            }

                            IconButton(
                                onClick = { onCancelConsultation(consult.id) },
                                modifier = Modifier.testTag("cancel_consultation_btn_${consult.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Cancel Session", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Join Virtual Lounge Dialog
    showJoinDialogForConsultation?.let { consult ->
        AlertDialog(
            onDismissRequest = { showJoinDialogForConsultation = null },
            confirmButton = {
                Button(
                    onClick = { showJoinDialogForConsultation = null },
                    colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen)
                ) {
                    Text("Enter Virtual Meeting")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showJoinDialogForConsultation = null }) {
                    Text("Close")
                }
            },
            title = {
                Text("MISA Virtual Advisory Lounge", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Connecting you securely to your assigned MISA advisory counselor:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SaudiGreen.copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(consult.serviceRequested, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SaudiGreenDark)
                            Text("Scheduled: ${consult.scheduledDate}", fontSize = 11.sp)
                            Text("Attendee: ${consult.investorName}", fontSize = 11.sp)
                        }
                    }
                    Text(
                        "Please ensure your microphone and camera are connected. All communications are strictly confidential and encrypted under Saudi National Cybersecurity Authority (NCA) regulations.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

// ==========================================
// 5. EXPERT BOOKING MODAL
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpertBookingModal(
    expert: InvestmentExpert,
    investorProfile: InvestorInterestProfile,
    initialSector: String,
    initialOpportunityTitle: String?,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        email: String,
        company: String,
        sector: String,
        format: String,
        slot: String,
        tier: String,
        notes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(investorProfile.investorName) }
    var email by remember { mutableStateOf(investorProfile.corporateEmail) }
    var company by remember { mutableStateOf(investorProfile.companyName) }
    var sector by remember { mutableStateOf(initialSector) }
    var selectedSlot by remember { mutableStateOf(expert.availableSlots.firstOrNull() ?: "Tomorrow 10:00 AM AST") }
    var meetingFormat by remember { mutableStateOf("Virtual HD Video Lounge") }
    var capitalTier by remember { mutableStateOf(investorProfile.capitalTier) }
    var notes by remember {
        mutableStateOf(
            if (!initialOpportunityTitle.isNullOrBlank()) {
                "Advisory regarding: $initialOpportunityTitle. ${investorProfile.specificObjectives}"
            } else {
                investorProfile.specificObjectives
            }
        )
    }

    val formats = listOf("Virtual HD Video Lounge", "MISA Riyadh HQ (Diplomatic Quarter)", "KAFD Executive Lounge")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, email, company, sector, meetingFormat, selectedSlot, capitalTier, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaudiGreen),
                modifier = Modifier.testTag("confirm_booking_submit_btn")
            ) {
                Text("Confirm Advisory Booking", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SaudiGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = SaudiGoldDark, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Schedule Advisory Session", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("with ${expert.name}", fontSize = 12.sp, color = SaudiGreen, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Expert header snippet
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(expert.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SaudiGreenDark)
                        Text(expert.credentials, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Meeting Format Selector
                Text("Consultation Format:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    formats.forEach { fmt ->
                        FilterChip(
                            selected = meetingFormat == fmt,
                            onClick = { meetingFormat = fmt },
                            label = { Text(fmt.split(" ").take(2).joinToString(" "), fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaudiGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Available Time Slots
                Text("Available Date & Time (Riyadh AST):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    expert.availableSlots.forEach { slot ->
                        FilterChip(
                            selected = selectedSlot == slot,
                            onClick = { selectedSlot = slot },
                            label = { Text(slot, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SaudiGold,
                                selectedLabelColor = Color(0xFF1E241F)
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Lead Executive Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Corporate Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company / Enterprise") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = sector,
                    onValueChange = { sector = it },
                    label = { Text("Investment Sector / Topic") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Agenda & Objectives") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }
    )
}
