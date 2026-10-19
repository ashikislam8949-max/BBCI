package com.example

import com.example.data.consulting.InvestorInterestProfile
import com.example.data.firebase.investorProfileFromFirestore
import com.example.data.firebase.toFirestoreFields
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InvestorProfileSyncTest {

    @Test
    fun identityOnlyDocumentKeepsLocalCriteria() {
        val fallback = InvestorInterestProfile()
        val parsed = investorProfileFromFirestore(
            mapOf(
                "displayName" to "Ada Lovelace",
                "email" to "ada@example.com",
                "company" to "Analytical Engines"
            ),
            fallback
        )
        assertEquals("Ada Lovelace", parsed.investorName)
        assertEquals("ada@example.com", parsed.corporateEmail)
        assertEquals("Analytical Engines", parsed.companyName)
        assertEquals(fallback.selectedSectors, parsed.selectedSectors)
        assertEquals(fallback.preferredRegions, parsed.preferredRegions)
    }

    @Test
    fun fullDocumentReplacesCriteriaAndRoundTripsUid() {
        val parsed = investorProfileFromFirestore(
            mapOf(
                "displayName" to "Ada",
                "email" to "ada@example.com",
                "companyName" to "Engines",
                "selectedSectors" to listOf("Tourism"),
                "preferredRegions" to listOf("Jeddah"),
                "desiredIncentives" to listOf("None"),
                "specificObjectives" to "Open a desk"
            ),
            InvestorInterestProfile()
        )
        assertEquals(setOf("Tourism"), parsed.selectedSectors)
        assertEquals(setOf("Jeddah"), parsed.preferredRegions)
        assertEquals("Open a desk", parsed.specificObjectives)

        val stored = parsed.toFirestoreFields(uid = "firebase-uid-1", photoUrl = null)
        assertEquals("firebase-uid-1", stored["uid"])
        assertEquals("google", stored["provider"])
        assertEquals("Ada", stored["displayName"])
        assertTrue((stored["selectedSectors"] as List<*>).contains("Tourism"))
    }

    @Test
    fun emptyCloudDocumentUsesFallback() {
        val fallback = InvestorInterestProfile(investorName = "Local Name")
        val parsed = investorProfileFromFirestore(emptyMap(), fallback)
        assertEquals("Local Name", parsed.investorName)
    }
}
