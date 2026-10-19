package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.consulting.InvestmentExpert
import com.example.data.consulting.InvestorAdvisoryData
import com.example.data.consulting.InvestorInterestProfile
import com.example.data.consulting.SaudiInvestmentOpportunity
import com.example.data.consulting.SaudiSectorInsight
import com.example.data.firebase.FirebaseAuthManager
import com.example.data.firebase.UserProfile
import com.example.data.local.AppDatabase
import com.example.data.local.CompanyDossierEntity
import com.example.data.local.ComplianceItemEntity
import com.example.data.local.ConsultationEntity
import com.example.data.remote.GeminiClient
import com.example.data.remote.GeneratedImageResult
import com.example.data.remote.GroundedResponse
import com.example.data.repository.SaudiGovRepository
import com.example.data.nafath.BaladyLicenseResult
import com.example.data.nafath.QiwaLaborResult
import com.example.data.nafath.SaudiGovIntegrationService
import com.example.data.nafath.WathqCrResult
import com.example.data.nafath.ZatcaTaxResult
import com.example.data.sbtech.SbTechCredentials
import com.example.data.sbtech.SbTechEmbedGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab {
    DASHBOARD,
    SBTECH_PORTAL, // sbtech.sa Live Portal with Real Saudi Gov Integrations
    MISA_CR_WIZARD,
    MISA_APPLICATION, // Multi-Step MISA Investment License Application Form
    GOV_INTEGRATIONS, // Sample preparation notes. Not a government login.
    COMPLIANCE_RADAR,
    WE_CONSULTING,
    AI_ADVISORY
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = SaudiGovRepository(database.saudiGovDao())
    val authManager = FirebaseAuthManager(application)
    val govService = SaudiGovIntegrationService()

    val currentUser: StateFlow<UserProfile?> = authManager.currentUser
    val isAuthLoading: StateFlow<Boolean> = authManager.isLoading
    val authError: StateFlow<String?> = authManager.authError
    val googleSignInAvailable: Boolean
        get() = authManager.googleSignInAvailable

    // Saudi Government API Query States
    private val _wathqResult = MutableStateFlow<WathqCrResult?>(null)
    val wathqResult: StateFlow<WathqCrResult?> = _wathqResult.asStateFlow()
    private val _isWathqLoading = MutableStateFlow(false)
    val isWathqLoading: StateFlow<Boolean> = _isWathqLoading.asStateFlow()

    private val _zatcaResult = MutableStateFlow<ZatcaTaxResult?>(null)
    val zatcaResult: StateFlow<ZatcaTaxResult?> = _zatcaResult.asStateFlow()
    private val _isZatcaLoading = MutableStateFlow(false)
    val isZatcaLoading: StateFlow<Boolean> = _isZatcaLoading.asStateFlow()

    private val _qiwaResult = MutableStateFlow<QiwaLaborResult?>(null)
    val qiwaResult: StateFlow<QiwaLaborResult?> = _qiwaResult.asStateFlow()
    private val _isQiwaLoading = MutableStateFlow(false)
    val isQiwaLoading: StateFlow<Boolean> = _isQiwaLoading.asStateFlow()

    private val _baladyResult = MutableStateFlow<BaladyLicenseResult?>(null)
    val baladyResult: StateFlow<BaladyLicenseResult?> = _baladyResult.asStateFlow()
    private val _isBaladyLoading = MutableStateFlow(false)
    val isBaladyLoading: StateFlow<Boolean> = _isBaladyLoading.asStateFlow()

    // SBTech.sa Live Authentication & Government API Handshake
    private val _sbTechCredentials = MutableStateFlow(SbTechCredentials())
    val sbTechCredentials: StateFlow<SbTechCredentials> = _sbTechCredentials.asStateFlow()

    private val _isSbTechHandshaking = MutableStateFlow(false)
    val isSbTechHandshaking: StateFlow<Boolean> = _isSbTechHandshaking.asStateFlow()

    private val _sbTechHandshakeMessage = MutableStateFlow<String?>(null)
    val sbTechHandshakeMessage: StateFlow<String?> = _sbTechHandshakeMessage.asStateFlow()

    fun updateSbTechCredentials(credentials: SbTechCredentials) {
        _sbTechCredentials.value = credentials
    }

    fun testSbTechLiveHandshake() {
        viewModelScope.launch {
            _isSbTechHandshaking.value = true
            _sbTechHandshakeMessage.value = null
            kotlinx.coroutines.delay(800)

            _sbTechCredentials.value = _sbTechCredentials.value.copy(
                lastHandshakeTime = System.currentTimeMillis(),
                isLiveProduction = false
            )
            _isSbTechHandshaking.value = false
            _sbTechHandshakeMessage.value = "Saved on this device. This app is not connected to Nafath, MISA, or any government system."
        }
    }

    fun getSbTechEmbedCode(): String {
        return SbTechEmbedGenerator.generateHtmlEmbedSnippet(_sbTechCredentials.value)
    }

    fun queryWathq(crNumber: String) {
        viewModelScope.launch {
            _isWathqLoading.value = true
            _wathqResult.value = govService.queryWathqCr(crNumber)
            _isWathqLoading.value = false
        }
    }

    fun queryZatca(tinNumber: String) {
        viewModelScope.launch {
            _isZatcaLoading.value = true
            _zatcaResult.value = govService.queryZatcaTax(tinNumber)
            _isZatcaLoading.value = false
        }
    }

    fun queryQiwa(estNo: String) {
        viewModelScope.launch {
            _isQiwaLoading.value = true
            _qiwaResult.value = govService.queryQiwaLabor(estNo)
            _isQiwaLoading.value = false
        }
    }

    fun queryBalady(licNo: String) {
        viewModelScope.launch {
            _isBaladyLoading.value = true
            _baladyResult.value = govService.queryBaladyLicense(licNo)
            _isBaladyLoading.value = false
        }
    }


    val dossiers: StateFlow<List<CompanyDossierEntity>> = repository.dossiers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complianceItems: StateFlow<List<ComplianceItemEntity>> = repository.complianceItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val consultations: StateFlow<List<ConsultationEntity>> = repository.consultations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Navigation Tab
    private val _currentTab = MutableStateFlow(AppNavTab.DASHBOARD)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // Selected Dossier for Detail View
    private val _selectedDossier = MutableStateFlow<CompanyDossierEntity?>(null)
    val selectedDossier: StateFlow<CompanyDossierEntity?> = _selectedDossier.asStateFlow()

    fun selectDossier(dossier: CompanyDossierEntity?) {
        _selectedDossier.value = dossier
    }

    // Saudization / Nitaqat Calculator State
    private val _calcTotalEmployees = MutableStateFlow(25)
    val calcTotalEmployees: StateFlow<Int> = _calcTotalEmployees.asStateFlow()

    private val _calcSaudiEmployees = MutableStateFlow(9)
    val calcSaudiEmployees: StateFlow<Int> = _calcSaudiEmployees.asStateFlow()

    private val _calcSector = MutableStateFlow("Information & Communications Technology (ICT)")
    val calcSector: StateFlow<String> = _calcSector.asStateFlow()

    fun updateCalculator(total: Int, saudi: Int, sector: String) {
        _calcTotalEmployees.value = maxOf(1, total)
        _calcSaudiEmployees.value = maxOf(0, minOf(saudi, total))
        _calcSector.value = sector
    }

    // AI Suite State
    // 1. Search Grounding (gemini-3.5-flash with Google Search tool)
    private val _groundedQuery = MutableStateFlow("What are the 2025/2026 MISA license updates and 30-year tax exemptions for Regional Headquarters (RHQ)?")
    val groundedQuery: StateFlow<String> = _groundedQuery.asStateFlow()

    private val _groundedResult = MutableStateFlow<GroundedResponse?>(null)
    val groundedResult: StateFlow<GroundedResponse?> = _groundedResult.asStateFlow()

    private val _isGroundedLoading = MutableStateFlow(false)
    val isGroundedLoading: StateFlow<Boolean> = _isGroundedLoading.asStateFlow()

    fun runGroundedSearch(query: String) {
        _groundedQuery.value = query
        viewModelScope.launch {
            _isGroundedLoading.value = true
            _groundedResult.value = GeminiClient.queryWithSearchGrounding(query)
            _isGroundedLoading.value = false
        }
    }

    // 2. High Thinking (gemini-3.1-pro-preview with thinkingLevel = HIGH)
    private val _thinkingPrompt = MutableStateFlow("Design a strategic 3-year market expansion roadmap into Saudi Arabia for an AI & Fintech enterprise under RHQ, factoring in ZATCA Phase 2 Fatoora and Qiwa Nitaqat quotas.")
    val thinkingPrompt: StateFlow<String> = _thinkingPrompt.asStateFlow()

    private val _thinkingResult = MutableStateFlow<String?>(null)
    val thinkingResult: StateFlow<String?> = _thinkingResult.asStateFlow()

    private val _isThinkingLoading = MutableStateFlow(false)
    val isThinkingLoading: StateFlow<Boolean> = _isThinkingLoading.asStateFlow()

    fun runHighThinkingStrategy(prompt: String) {
        _thinkingPrompt.value = prompt
        viewModelScope.launch {
            _isThinkingLoading.value = true
            _thinkingResult.value = GeminiClient.queryHighThinkingStrategy(prompt)
            _isThinkingLoading.value = false
        }
    }

    // 3. Fast Responses (gemini-3.1-flash-lite)
    private val _fastPrompt = MutableStateFlow("What is the minimum capital required for a 100% foreign-owned Services LLC in Saudi Arabia?")
    val fastPrompt: StateFlow<String> = _fastPrompt.asStateFlow()

    private val _fastResult = MutableStateFlow<String?>(null)
    val fastResult: StateFlow<String?> = _fastResult.asStateFlow()

    private val _isFastLoading = MutableStateFlow(false)
    val isFastLoading: StateFlow<Boolean> = _isFastLoading.asStateFlow()

    fun runFastCheck(prompt: String) {
        _fastPrompt.value = prompt
        viewModelScope.launch {
            _isFastLoading.value = true
            _fastResult.value = GeminiClient.queryFastResponse(prompt)
            _isFastLoading.value = false
        }
    }

    // 4. Image Generation (gemini-3-pro-image-preview with 1K, 2K, 4K size)
    private val _imagePrompt = MutableStateFlow("Futuristic corporate headquarters tower in King Abdullah Financial District Riyadh with Saudi solar glass facade and luxury investor boardroom")
    val imagePrompt: StateFlow<String> = _imagePrompt.asStateFlow()

    private val _selectedImageSize = MutableStateFlow("1K") // 1K, 2K, 4K
    val selectedImageSize: StateFlow<String> = _selectedImageSize.asStateFlow()

    private val _imageResult = MutableStateFlow<GeneratedImageResult?>(null)
    val imageResult: StateFlow<GeneratedImageResult?> = _imageResult.asStateFlow()

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading.asStateFlow()

    fun setImageSize(size: String) {
        _selectedImageSize.value = size
    }

    fun generatePitchVisual(prompt: String) {
        _imagePrompt.value = prompt
        viewModelScope.launch {
            _isImageLoading.value = true
            _imageResult.value = GeminiClient.generateInvestorImage(
                prompt = prompt,
                imageSize = _selectedImageSize.value
            )
            _isImageLoading.value = false
        }
    }

    // 5. Live Voice Conversation (gemini-3.1-flash-live-preview)
    private val _isVoiceActive = MutableStateFlow(false)
    val isVoiceActive: StateFlow<Boolean> = _isVoiceActive.asStateFlow()

    private val _voiceTranscript = MutableStateFlow("Tap 'Start Live Voice Consultation' to converse in real-time with the MISA & WE Consulting Live AI Advisor.")
    val voiceTranscript: StateFlow<String> = _voiceTranscript.asStateFlow()

    private val _isVoiceProcessing = MutableStateFlow(false)
    val isVoiceProcessing: StateFlow<Boolean> = _isVoiceProcessing.asStateFlow()

    fun toggleVoiceAdvisor() {
        if (_isVoiceActive.value) {
            _isVoiceActive.value = false
        } else {
            _isVoiceActive.value = true
            askVoiceAdvisor("Hello, I am an international investor evaluating expanding our operations to Riyadh. Can you outline the MISA foreign investment license process and 100% foreign ownership benefits?")
        }
    }

    fun askVoiceAdvisor(userSpokenQuery: String) {
        viewModelScope.launch {
            _isVoiceProcessing.value = true
            val response = GeminiClient.queryLiveVoiceAdvisor(userSpokenQuery)
            _voiceTranscript.value = response.text
            _isVoiceProcessing.value = false
        }
    }

    // Registration Actions
    fun createNewDossier(
        companyName: String,
        arabicName: String,
        licenseType: String,
        legalStructure: String,
        capitalSar: Double,
        foreignOwnership: Int
    ) {
        viewModelScope.launch {
            val randomCr = "1010" + (100000 + (Math.random() * 899999).toInt())
            val newDossier = CompanyDossierEntity(
                companyName = companyName,
                arabicName = arabicName.ifBlank { "شركة $companyName المحدودة" },
                licenseType = licenseType,
                legalStructure = legalStructure,
                misaStatus = "Under Review",
                crNumber = randomCr,
                capitalSar = capitalSar,
                foreignOwnershipPercentage = foreignOwnership,
                saudizationTier = "Platinum",
                currentStep = 2,
                totalEmployees = 12,
                saudiEmployees = 5
            )
            val id = repository.insertDossier(newDossier)
            authManager.syncDossierToFirestore(
                dossierId = id.toInt(),
                companyName = companyName,
                crNumber = randomCr,
                status = "Under Review"
            )
        }
    }

    fun advanceDossierStep(dossier: CompanyDossierEntity) {
        viewModelScope.launch {
            val nextStep = minOf(7, dossier.currentStep + 1)
            val status = if (nextStep == 7) "Active" else "In Progress"
            val updated = dossier.copy(
                currentStep = nextStep,
                misaStatus = status
            )
            repository.updateDossier(updated)
            _selectedDossier.value = updated
            authManager.syncDossierToFirestore(
                dossierId = updated.id,
                companyName = updated.companyName,
                crNumber = updated.crNumber,
                status = status
            )
        }
    }

    // International Investor Advisory & Sector Recommendation Module
    private val _investorProfile = MutableStateFlow(InvestorInterestProfile())
    val investorProfile: StateFlow<InvestorInterestProfile> = _investorProfile.asStateFlow()

    init {
        viewModelScope.launch {
            linkSignedInProfile(_investorProfile.value)
        }
    }

    private val _selectedExpertForBooking = MutableStateFlow<InvestmentExpert?>(null)
    val selectedExpertForBooking: StateFlow<InvestmentExpert?> = _selectedExpertForBooking.asStateFlow()

    private val _aiSectorReport = MutableStateFlow<String?>(null)
    val aiSectorReport: StateFlow<String?> = _aiSectorReport.asStateFlow()

    private val _isAiSectorReportLoading = MutableStateFlow(false)
    val isAiSectorReportLoading: StateFlow<Boolean> = _isAiSectorReportLoading.asStateFlow()

    fun updateInvestorProfile(profile: InvestorInterestProfile) {
        _investorProfile.value = profile
        val user = authManager.currentUser.value ?: return
        viewModelScope.launch {
            authManager.writeInvestorProfile(user.uid, profile, user.photoUrl)
        }
    }

    fun signInWithGoogle(activityContext: Context) {
        viewModelScope.launch {
            val user = authManager.signInWithGoogle(activityContext) ?: return@launch
            val local = _investorProfile.value.copy(
                investorName = user.displayName.ifBlank { _investorProfile.value.investorName },
                corporateEmail = user.email.ifBlank { _investorProfile.value.corporateEmail }
            )
            linkSignedInProfile(local)
        }
    }

    fun saveSignedInIdentity(displayName: String, companyName: String) {
        val user = authManager.currentUser.value ?: return
        viewModelScope.launch {
            authManager.updateDisplayName(displayName)
            val updated = _investorProfile.value.copy(
                investorName = displayName.ifBlank { user.displayName },
                companyName = companyName.ifBlank { _investorProfile.value.companyName },
                corporateEmail = user.email.ifBlank { _investorProfile.value.corporateEmail }
            )
            _investorProfile.value = updated
            authManager.writeInvestorProfile(user.uid, updated, authManager.currentUser.value?.photoUrl)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
        }
    }

    fun deleteAccount(onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            if (authManager.deleteAccount()) {
                onDeleted()
            }
        }
    }

    private suspend fun linkSignedInProfile(local: InvestorInterestProfile) {
        val user = authManager.currentUser.value ?: return
        val linked = authManager.readInvestorProfile(user.uid, local)
        val merged = if (linked.fromCloud) {
            linked.profile
        } else {
            local.copy(
                investorName = user.displayName.ifBlank { local.investorName },
                corporateEmail = user.email.ifBlank { local.corporateEmail }
            )
        }
        _investorProfile.value = merged
        authManager.writeInvestorProfile(user.uid, merged, user.photoUrl)
    }

    fun selectExpertForBooking(expert: InvestmentExpert?) {
        _selectedExpertForBooking.value = expert
    }

    fun bookExpertConsultation(
        expert: InvestmentExpert,
        investorName: String,
        investorEmail: String,
        companyName: String,
        targetSector: String,
        meetingFormat: String,
        scheduledDate: String,
        investmentTier: String,
        agendaNotes: String
    ) {
        viewModelScope.launch {
            val bookingCode = "MISA-ADV-" + (1000 + (Math.random() * 8999).toInt())
            val formattedService = "${expert.name} (${expert.title}) - $meetingFormat"
            val detailedNotes = "Ref: $bookingCode | Entity: $companyName | Capital: $investmentTier | Format: $meetingFormat | Agenda: $agendaNotes"
            val consult = ConsultationEntity(
                investorName = investorName,
                investorEmail = investorEmail,
                targetSector = targetSector,
                serviceRequested = formattedService,
                scheduledDate = scheduledDate,
                advisoryNotes = detailedNotes,
                status = "CONFIRMED"
            )
            repository.insertConsultation(consult)
            _selectedExpertForBooking.value = null
        }
    }

    fun cancelConsultation(consultationId: Int) {
        viewModelScope.launch {
            repository.deleteConsultationById(consultationId)
        }
    }

    fun runAiSectorRecommendation(
        sector: String,
        capitalTier: String,
        preferredStructure: String,
        objectives: String
    ) {
        viewModelScope.launch {
            _isAiSectorReportLoading.value = true
            _aiSectorReport.value = null
            val prompt = """
                Perform deep strategic advisory for an international investor entering Saudi Arabia under MISA regulations.
                Target Sector: $sector
                Planned Capital Deployment: $capitalTier
                Preferred Corporate Structure: $preferredStructure
                Investor Objectives: $objectives
                
                Please structure the advisory report with:
                1. Market Opportunity & Vision 2030 Strategic Alignment
                2. Optimal MISA License Category & Key Regulatory Clearances
                3. Applicable Incentives & Tax Relief (RHQ 30-Year 0% CIT, SEZ 5% CIT, SIDF Loans)
                4. Key Giga-Projects / Sponsoring Entities to Target (NEOM, Red Sea, KAFD, PIF)
                5. Three Critical Execution Milestones for International Corporate Setup
            """.trimIndent()

            val result = GeminiClient.queryHighThinkingStrategy(prompt)
            _aiSectorReport.value = result
            _isAiSectorReportLoading.value = false
        }
    }

    fun bookConsultation(
        investorName: String,
        investorEmail: String,
        targetSector: String,
        serviceRequested: String,
        scheduledDate: String,
        notes: String
    ) {
        viewModelScope.launch {
            val consult = ConsultationEntity(
                investorName = investorName,
                investorEmail = investorEmail,
                targetSector = targetSector,
                serviceRequested = serviceRequested,
                scheduledDate = scheduledDate,
                advisoryNotes = notes,
                status = "CONFIRMED"
            )
            repository.insertConsultation(consult)
        }
    }

    fun toggleComplianceItemStatus(item: ComplianceItemEntity) {
        viewModelScope.launch {
            val newStatus = if (item.status == "COMPLIANT") "PENDING_ACTION" else "COMPLIANT"
            repository.updateComplianceItem(item.copy(status = newStatus))
        }
    }
}
