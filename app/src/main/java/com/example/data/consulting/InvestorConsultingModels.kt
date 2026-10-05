package com.example.data.consulting

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.SaudiGold
import com.example.ui.theme.SaudiGreen
import com.example.ui.theme.SaudiGreenDark

data class InvestmentExpert(
    val id: String,
    val name: String,
    val arabicName: String,
    val title: String,
    val organization: String = "MISA Certified Advisory Partner",
    val credentials: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewCount: Int,
    val completedSessions: Int,
    val languages: List<String>,
    val specialties: List<String>,
    val availableSlots: List<String>,
    val officeLocation: String,
    val bio: String,
    val badgeColor: Color = SaudiGreen
)

data class InvestorInterestProfile(
    val investorName: String = "David Henderson",
    val companyName: String = "Global Vantage Technologies Ltd",
    val corporateEmail: String = "d.henderson@globalvantage.com",
    val phoneWhatsapp: String = "+44 20 7946 0912",
    val homeCountry: String = "United Kingdom",
    val selectedSectors: Set<String> = setOf("Artificial Intelligence & Enterprise Cloud", "Clean Tech & Green Energy"),
    val capitalTier: String = "$5M - $25M (Growth Expansion)",
    val preferredStructure: String = "Regional Headquarters (RHQ) - 0% Tax Holiday",
    val timeHorizon: String = "Immediate (0-3 Months)",
    val preferredRegions: Set<String> = setOf("Riyadh (KAFD)", "NEOM / Oxagon"),
    val desiredIncentives: Set<String> = setOf("30-Year 0% Corporate Income Tax", "0% Customs Duties & Tariff Exemption", "SIDF Soft Industrial Loans (up to 75%)"),
    val specificObjectives: String = "Establishing regional operational hub to serve GCC and North Africa giga-projects with 25 initial engineering hires."
)

data class SaudiSectorInsight(
    val id: String,
    val name: String,
    val arabicName: String,
    val marketValueSar: String,
    val cagrGrowth: String,
    val vision2030Target: String,
    val overview: String,
    val keyEnablers: List<String>,
    val recommendedMisaLicense: String,
    val regulatoryLeadAgency: String
)

data class SaudiInvestmentOpportunity(
    val id: String,
    val title: String,
    val arabicTitle: String,
    val sector: String,
    val sponsoringAgency: String,
    val location: String,
    val capitalRequired: String,
    val projectedIrr: String,
    val taxIncentives: String,
    val summary: String,
    val eligibleStructures: List<String>,
    val keyMilestones: List<String>,
    val recommendedExpertId: String,
    val matchScore: Int = 95
)

object InvestorAdvisoryData {

    val certifiedExperts = listOf(
        InvestmentExpert(
            id = "exp_sultan",
            name = "Dr. Sultan Al-Muqbil",
            arabicName = "د. سلطان المقبل",
            title = "Senior MISA Licensing & Regulatory Partner",
            credentials = "Ex-Deputy Director MISA, Harvard Law LL.M, 16+ yrs KSA",
            experienceYears = 16,
            rating = 4.95,
            reviewCount = 142,
            completedSessions = 380,
            languages = listOf("English", "Arabic", "French"),
            specialties = listOf("100% Foreign Ownership", "MISA Investment Licenses", "Ministry of Commerce CR", "Cross-Border Tax"),
            availableSlots = listOf(
                "Tomorrow 10:00 AM AST",
                "Tomorrow 2:30 PM AST",
                "Thursday 11:00 AM AST",
                "Next Monday 3:00 PM AST"
            ),
            officeLocation = "MISA HQ - Diplomatic Quarter, Riyadh",
            bio = "Specializing in turnkey foreign direct investment setup, navigating cross-border corporate reorganization, bilateral investment treaties, and fast-track MISA licensing for Fortune 500 and high-growth scale-ups."
        ),
        InvestmentExpert(
            id = "exp_sarah",
            name = "Sarah Jenkins, CFA",
            arabicName = "سارة جنكينز (مستشارة ضرائب)",
            title = "RHQ 30-Year Tax & Special Economic Zones Counsel",
            credentials = "Former Big-4 Middle East Tax Director, 14 yrs KSA Experience",
            experienceYears = 14,
            rating = 4.98,
            reviewCount = 189,
            completedSessions = 520,
            languages = listOf("English", "German", "Arabic"),
            specialties = listOf("RHQ 30-Year 0% Tax Holiday", "SEZ 5% CIT Structuring", "ZATCA Withholding Tax", "Transfer Pricing"),
            availableSlots = listOf(
                "Today 4:00 PM AST",
                "Tomorrow 11:30 AM AST",
                "Wednesday 1:00 PM AST",
                "Thursday 3:30 PM AST"
            ),
            officeLocation = "KAFD Tower 2.08, Floor 19, Riyadh",
            bio = "Leading international expert on qualifying multinational groups for the Saudi Regional Headquarters (RHQ) 30-year corporate income tax holiday, 0% withholding tax on dividend repatriations, and Special Economic Zone customs relief."
        ),
        InvestmentExpert(
            id = "exp_fahad",
            name = "Eng. Fahad Al-Otaibi",
            arabicName = "م. فهد العتيبي",
            title = "Vision 2030 Giga-Projects & Sovereign Supply Chains",
            credentials = "PIF Strategic Advisory Council Alum, MIT Master of Engineering",
            experienceYears = 18,
            rating = 4.92,
            reviewCount = 115,
            completedSessions = 295,
            languages = listOf("English", "Arabic"),
            specialties = listOf("NEOM & Oxagon Vendor Enlistment", "Red Sea Global Contracts", "Qiddiya & Diriyah Frameworks", "SIDF Industrial Financing"),
            availableSlots = listOf(
                "Tomorrow 9:30 AM AST",
                "Wednesday 10:00 AM AST",
                "Thursday 2:00 PM AST"
            ),
            officeLocation = "PIF Tower Lounge, KAFD, Riyadh",
            bio = "Guiding international engineering, clean technology, and industrial conglomerates to pre-qualify and secure major procurement frameworks across Saudi sovereign wealth fund (PIF) giga-projects."
        ),
        InvestmentExpert(
            id = "exp_lin",
            name = "Lin Wei, MBA",
            arabicName = "لين وي (خبير الاستثمار الآسيوي)",
            title = "Asia-Pacific & Middle East Cross-Border Investment Lead",
            credentials = "Ex-China-Arabia Bilateral Investment Forum Board, Tsinghua/LBS",
            experienceYears = 13,
            rating = 4.90,
            reviewCount = 98,
            completedSessions = 240,
            languages = listOf("Mandarin", "English", "Arabic"),
            specialties = listOf("China-Saudi Bilateral JVs", "Advanced Manufacturing", "Automotive & EV Supply Chains", "Sovereign Co-Investment"),
            availableSlots = listOf(
                "Wednesday 9:00 AM AST",
                "Thursday 1:30 PM AST",
                "Next Tuesday 10:00 AM AST"
            ),
            officeLocation = "MISA Asia Investment Desk, Riyadh & Shanghai",
            bio = "Specialized in cross-border capital deployment between East Asia and Saudi Arabia, with deep domain expertise in electric mobility, battery materials, semiconductor supply chain, and joint ventures with local champions."
        ),
        InvestmentExpert(
            id = "exp_mona",
            name = "Mona Al-Zahrani, Esq.",
            arabicName = "المحامية منى الزهراني",
            title = "Corporate Governance, Qiwa Labor & GRO Legal Counsel",
            credentials = "Saudi Bar Association Certified, Ex-Ministry of Justice Counsel",
            experienceYears = 12,
            rating = 4.96,
            reviewCount = 164,
            completedSessions = 410,
            languages = listOf("Arabic", "English"),
            specialties = listOf("Qiwa & Saudization (Nitaqat)", "Commercial Court Disputes", "Ejar Leases & Civil Defense", "Muqeem Executive Visas"),
            availableSlots = listOf(
                "Tomorrow 1:00 PM AST",
                "Wednesday 3:00 PM AST",
                "Thursday 10:30 AM AST"
            ),
            officeLocation = "Olaya Commercial District, Level 11, Riyadh",
            bio = "Providing full-spectrum government relations (GRO) support, bilingual commercial contract formulation under modern Saudi Company Law, executive visa quotas, and labor tribunal representations."
        )
    )

    val availableSectors = listOf(
        "Artificial Intelligence & Enterprise Cloud",
        "Clean Tech & Green Energy",
        "Advanced Manufacturing & Defense",
        "Healthcare & Life Sciences",
        "Luxury Tourism & Hospitality",
        "Mining & Rare Earth Minerals",
        "FinTech & Digital Capital Markets",
        "Logistics, Ports & Global Supply Chain"
    )

    val capitalTiers = listOf(
        "Under $5M (Venture / Seed)",
        "$5M - $25M (Growth Expansion)",
        "$25M - $100M (Corporate Scale)",
        "$100M+ (Mega / Sovereign Scale)"
    )

    val corporateStructures = listOf(
        "Regional Headquarters (RHQ) - 0% Tax Holiday",
        "100% Foreign-Owned LLC (Services/Industrial)",
        "Special Economic Zone (SEZ) Status",
        "Branch of Foreign Corporation",
        "Joint Venture with Saudi Local Champion"
    )

    val targetRegions = listOf(
        "Riyadh (KAFD & City Hub)",
        "NEOM / Oxagon / The Line",
        "Western (Jeddah & Red Sea Coast)",
        "Eastern Province (Jubail & Ras Al Khair)"
    )

    val availableIncentives = listOf(
        "30-Year 0% Corporate Income Tax",
        "0% Customs Duties & Tariff Exemption",
        "SIDF Soft Industrial Loans (up to 75%)",
        "HRDF Saudi Wage Subsidies (up to 50%)",
        "PIF / Giga-Project Direct Vendor Enlistment",
        "Exemption from Work Visa Saudization (10 Years)"
    )

    val sectorInsights = listOf(
        SaudiSectorInsight(
            id = "sec_ai_cloud",
            name = "Artificial Intelligence & Enterprise Cloud",
            arabicName = "الذكاء الاصطناعي والحوسبة السحابية",
            marketValueSar = "SAR 72 Billion by 2030",
            cagrGrowth = "+28.4% YoY",
            vision2030Target = "Top 10 Global AI & Supercomputing Nations",
            overview = "Driven by the National Strategy for Data and AI (SDAIA) and the newly launched Cloud Computing SEZ. Major hyperscalers and sovereign compute clusters receive accelerated power allocations and fiber connectivity.",
            keyEnablers = listOf(
                "Cloud Computing Special Economic Zone tax exemptions",
                "Direct STC and sovereign data center fiber interconnection",
                "SDAIA AI ethics regulatory sandbox with fast-track licensing"
            ),
            recommendedMisaLicense = "MISA ICT & Services License (100% Foreign Ownership)",
            regulatoryLeadAgency = "SDAIA & Ministry of Communications and IT (MCIT)"
        ),
        SaudiSectorInsight(
            id = "sec_clean_energy",
            name = "Clean Tech & Green Energy",
            arabicName = "الطاقة النظيفة والتقنيات الخضراء",
            marketValueSar = "SAR 180 Billion by 2030",
            cagrGrowth = "+31.2% YoY",
            vision2030Target = "50% Power from Renewables by 2030 & World's Largest Green Hydrogen Hub",
            overview = "The Saudi Green Initiative and NEOM Oxagon Green Hydrogen facility anchor multi-gigawatt solar, wind, and hydrogen infrastructure projects with sovereign off-take agreements from SPPC.",
            keyEnablers = listOf(
                "25-year sovereign Power Purchase Agreements (PPAs)",
                "SIDF concessionary financing up to 75% of capital expenditure",
                "0% customs duty on imported photovoltaic and electrolyzer equipment"
            ),
            recommendedMisaLicense = "MISA Industrial & Energy License",
            regulatoryLeadAgency = "Ministry of Energy & Saudi Green Initiative (SGI)"
        ),
        SaudiSectorInsight(
            id = "sec_advanced_mfg",
            name = "Advanced Manufacturing & Defense",
            arabicName = "التصنيع المتقدم والصناعات الدفاعية",
            marketValueSar = "SAR 310 Billion by 2030",
            cagrGrowth = "+19.8% YoY",
            vision2030Target = "50% Military Equipment Spending Localized in KSA",
            overview = "The General Authority for Military Industries (GAMI) and National Industrial Development and Logistics Program (NIDLP) incentivize local robotics, precision tooling, avionics, and composite materials.",
            keyEnablers = listOf(
                "GAMI 50% localization procurement mandate",
                "MODON industrial land at subsidized rates (SAR 1-4 per sqm/year)",
                "SIDF loan tenors up to 20 years with grace periods"
            ),
            recommendedMisaLicense = "MISA Industrial License (Requires GAMI / MODON clearance)",
            regulatoryLeadAgency = "GAMI & Ministry of Industry and Mineral Resources"
        ),
        SaudiSectorInsight(
            id = "sec_tourism",
            name = "Luxury Tourism & Hospitality",
            arabicName = "السياحة والضيافة الفاخرة",
            marketValueSar = "SAR 420 Billion by 2030",
            cagrGrowth = "+22.5% YoY",
            vision2030Target = "150 Million Annual Visits & 10% Contribution to National GDP",
            overview = "Led by Red Sea Global, Diriyah Gate, AlUla, and Soudah Development. Unprecedented international hotel flag expansion, ecotourism resorts, and luxury marina master developments.",
            keyEnablers = listOf(
                "Tourism Development Fund (TDF) co-investment debt & equity",
                "Special visa and foreign resident hospitality regulations",
                "Municipal license fast-tracking via Balady single-window"
            ),
            recommendedMisaLicense = "MISA Tourism & Hospitality Operator License",
            regulatoryLeadAgency = "Ministry of Tourism & Tourism Development Fund"
        )
    )

    val curatedOpportunities = listOf(
        SaudiInvestmentOpportunity(
            id = "opp_oxagon_hydrogen",
            title = "NEOM Oxagon Green Hydrogen & Synthetic Fuel Component Hub",
            arabicTitle = "مركز نيوم أوكساجون للهيدروجين الأخضر والوقود الصناعي",
            sector = "Clean Tech & Green Energy",
            sponsoringAgency = "NEOM Industrial Authority & Ministry of Energy",
            location = "Oxagon Industrial City, Tabuk Province",
            capitalRequired = "$45M - $180M",
            projectedIrr = "17.5% - 21.0% IRR",
            taxIncentives = "0% customs duties, 100% foreign ownership, 20-year renewable power tariff",
            summary = "Establish dedicated electrolyzer sub-assembly, specialized cryo-storage, and pipeline monitoring components inside Oxagon's automated net-zero port to supply the NEOM Green Hydrogen mega-plant.",
            eligibleStructures = listOf("100% Foreign Industrial LLC", "Joint Venture with ACWA Power", "SEZ Industrial Branch"),
            keyMilestones = listOf(
                "Land allocation at Oxagon Port Zone A",
                "Direct power interconnection agreement with NEOM ENOWA",
                "Export customs pre-clearance to European & Asian shipping lanes"
            ),
            recommendedExpertId = "exp_fahad",
            matchScore = 98
        ),
        SaudiInvestmentOpportunity(
            id = "opp_kafd_rhq_cloud",
            title = "KAFD Regional Headquarters AI & Enterprise Software Cluster",
            arabicTitle = "مجمع مركز الملك عبد الله المالي للذكاء الاصطناعي والمقرات الإقليمية",
            sector = "Artificial Intelligence & Enterprise Cloud",
            sponsoringAgency = "KAFD Authority & MISA RHQ Directorate",
            location = "King Abdullah Financial District (KAFD), Riyadh",
            capitalRequired = "$10M - $35M",
            projectedIrr = "22.0% - 28.5% IRR",
            taxIncentives = "30-year 0% corporate income tax holiday, 0% withholding tax, 10-year Saudization exemption",
            summary = "Deploy regional headquarters for Middle East and North Africa operations. Qualify for exclusive government tender eligibility, top-tier executive visas, and direct access to PIF venture subsidiaries.",
            eligibleStructures = listOf("Regional Headquarters (RHQ) License", "Services LLC"),
            keyMilestones = listOf(
                "MISA RHQ license issuance within 14 business days",
                "KAFD Grade-A commercial lease with plug-and-play fitout",
                "Direct integration into Saudi Government unified procurement portal (Etimad)"
            ),
            recommendedExpertId = "exp_sarah",
            matchScore = 96
        ),
        SaudiInvestmentOpportunity(
            id = "opp_ras_alkhair_maritime",
            title = "Ras Al-Khair Special Economic Zone Offshore Rig & Supply Base",
            arabicTitle = "المنطقة الاقتصادية الخاصة برأس الخير لتجهيزات المنصات البحرية",
            sector = "Advanced Manufacturing & Defense",
            sponsoringAgency = "Economic Cities and Special Zones Authority (ECZA)",
            location = "Ras Al-Khair SEZ, Eastern Province",
            capitalRequired = "$30M - $95M",
            projectedIrr = "16.0% - 19.5% IRR",
            taxIncentives = "5% corporate tax rate for 20 years, 0% withholding tax on profit repatriation, 0% customs",
            summary = "State-of-the-art heavy fabrication yard and maritime servicing center capitalizing on Aramco's King Salman International Complex for Maritime Industries.",
            eligibleStructures = listOf("SEZ Registered Enterprise", "Industrial Joint Venture"),
            keyMilestones = listOf(
                "ECZA SEZ operational license approval",
                "Deep-water quay access and dry-dock logistics agreement",
                "Long-term master service agreement with regional offshore drilling operators"
            ),
            recommendedExpertId = "exp_sultan",
            matchScore = 92
        ),
        SaudiInvestmentOpportunity(
            id = "opp_redsea_wellness",
            title = "Red Sea & Amaala Ultra-Luxury Regenerative Wellness Resort",
            arabicTitle = "منتجع الاستشفاء الفاخر والتجددي في وجهة البحر الأحمر وآمالا",
            sector = "Luxury Tourism & Hospitality",
            sponsoringAgency = "Red Sea Global & Tourism Development Fund (TDF)",
            location = "Amaala Triple Bay, Red Sea Coast",
            capitalRequired = "$50M - $220M",
            projectedIrr = "15.5% - 18.0% IRR",
            taxIncentives = "Subsidized debt from TDF (up to 60%), 0% foreign investment dividend restrictions",
            summary = "Co-develop and operate a world-class wellness hospitality resort powered 100% by off-grid solar energy, dark-sky protected astronomy lounges, and bespoke marine eco-villas.",
            eligibleStructures = listOf("Special Purpose Vehicle (SPV)", "Foreign Hotel Operator Branch"),
            keyMilestones = listOf(
                "TDF financing term sheet signed",
                "Master concession agreement with Red Sea Global",
                "Red Sea International Airport direct international charter connectivity"
            ),
            recommendedExpertId = "exp_fahad",
            matchScore = 94
        )
    )
}
