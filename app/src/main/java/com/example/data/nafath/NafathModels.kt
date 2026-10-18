package com.example.data.nafath

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
