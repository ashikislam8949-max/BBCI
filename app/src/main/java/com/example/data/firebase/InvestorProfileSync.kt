package com.example.data.firebase

import com.example.data.consulting.InvestorInterestProfile

internal const val INVESTOR_PROFILES = "investor_profiles"
internal const val INVESTOR_DOSSIERS = "investor_dossiers"

data class LinkedInvestorProfile(
    val profile: InvestorInterestProfile,
    val fromCloud: Boolean
)

fun InvestorInterestProfile.toFirestoreFields(uid: String, photoUrl: String?): Map<String, Any> {
    return mapOf(
        "uid" to uid,
        "name" to investorName,
        "displayName" to investorName,
        "email" to corporateEmail,
        "company" to companyName,
        "companyName" to companyName,
        "phoneWhatsapp" to phoneWhatsapp,
        "homeCountry" to homeCountry,
        "selectedSectors" to selectedSectors.toList(),
        "capitalTier" to capitalTier,
        "preferredStructure" to preferredStructure,
        "timeHorizon" to timeHorizon,
        "preferredRegions" to preferredRegions.toList(),
        "desiredIncentives" to desiredIncentives.toList(),
        "specificObjectives" to specificObjectives,
        "photoUrl" to (photoUrl ?: ""),
        "provider" to "google",
        "lastActive" to System.currentTimeMillis()
    )
}

fun investorProfileFromFirestore(
    data: Map<String, Any?>?,
    fallback: InvestorInterestProfile
): InvestorInterestProfile {
    if (data.isNullOrEmpty()) return fallback
    fun text(vararg keys: String, default: String): String {
        for (key in keys) {
            val value = data[key] as? String
            if (!value.isNullOrBlank()) return value
        }
        return default
    }
    fun texts(key: String, default: Set<String>): Set<String> {
        val raw = data[key] as? List<*> ?: return default
        val values = raw.mapNotNull { it as? String }.filter { it.isNotBlank() }
        return values.toSet().ifEmpty { default }
    }
    return fallback.copy(
        investorName = text("displayName", "name", default = fallback.investorName),
        companyName = text("companyName", "company", default = fallback.companyName),
        corporateEmail = text("email", default = fallback.corporateEmail),
        phoneWhatsapp = text("phoneWhatsapp", default = fallback.phoneWhatsapp),
        homeCountry = text("homeCountry", default = fallback.homeCountry),
        selectedSectors = texts("selectedSectors", fallback.selectedSectors),
        capitalTier = text("capitalTier", default = fallback.capitalTier),
        preferredStructure = text("preferredStructure", default = fallback.preferredStructure),
        timeHorizon = text("timeHorizon", default = fallback.timeHorizon),
        preferredRegions = texts("preferredRegions", fallback.preferredRegions),
        desiredIncentives = texts("desiredIncentives", fallback.desiredIncentives),
        specificObjectives = text("specificObjectives", default = fallback.specificObjectives)
    )
}
