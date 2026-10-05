package com.example.data.nafath

enum class NafathAuthStatus {
    IDLE,
    INITIATING,
    WAITING_USER_ACTION, // Displaying 2-digit code for user to tap in Nafath app
    BIOMETRIC_VERIFICATION,
    SUCCESS,
    EXPIRED,
    REJECTED
}

data class NafathUserIdentity(
    val nationalId: String,
    val arabicFullName: String,
    val englishFullName: String,
    val dateOfBirthHijri: String,
    val dateOfBirthGregorian: String,
    val nationality: String,
    val absherMobile: String,
    val nationalAddress: String,
    val isAuthorizedSignatory: Boolean = true,
    val authorizedCompanies: List<String> = emptyList(),
    val authToken: String = ""
)

data class NafathSession(
    val transactionId: String,
    val verificationCode: Int, // Two-digit code e.g. 42
    val nationalId: String,
    val status: NafathAuthStatus,
    val expiresInSeconds: Int = 180,
    val identity: NafathUserIdentity? = null,
    val errorMessage: String? = null
)

// Wathq (Ministry of Commerce) CR Query Result
data class WathqCrResult(
    val crNumber: String,
    val companyName: String,
    val arabicName: String,
    val status: String,
    val issueDate: String,
    val expiryDate: String,
    val capitalSar: Double,
    val legalForm: String,
    val mainActivity: String,
    val city: String,
    val managerName: String,
    val chamberOfCommerceNo: String
)

// ZATCA Query Result
data class ZatcaTaxResult(
    val tinNumber: String,
    val taxPayerName: String,
    val vatStatus: String, // Active / Registered
    val vatCertificateValid: Boolean,
    val fatooraPhase2Integrated: Boolean,
    val zakatCertificateExpiry: String,
    val complianceRating: String
)

// Qiwa Query Result
data class QiwaLaborResult(
    val establishmentNo: String,
    val establishmentName: String,
    val nitaqatTier: String,
    val saudizationRatio: Double,
    val totalEmployees: Int,
    val saudiEmployees: Int,
    val availableVisas: Int,
    val mudadWpsCompliance: String
)

// Balady Municipal Result
data class BaladyLicenseResult(
    val licenseNumber: String,
    val activityName: String,
    val premiseAddress: String,
    val civilDefenseApproved: Boolean,
    val status: String,
    val expiryDate: String
)
