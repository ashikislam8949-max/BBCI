package com.example

import com.example.data.local.CompanyDossierEntity
import com.example.data.local.ComplianceItemEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaudiGovPlatformTest {

    @Test
    fun `verify company dossier properties and 100 percent foreign ownership`() {
        val dossier = CompanyDossierEntity(
            id = 1,
            companyName = "Apex Global Cloud Solutions Ltd",
            arabicName = "شركة آبيكس العالمية لحلول السحابة المحدودة",
            licenseType = "Services & IT",
            legalStructure = "Limited Liability Company (LLC)",
            misaStatus = "Active",
            crNumber = "1010892341",
            capitalSar = 500000.0,
            foreignOwnershipPercentage = 100,
            saudizationTier = "High Green",
            currentStep = 7,
            totalEmployees = 16,
            saudiEmployees = 6
        )

        assertEquals(100, dossier.foreignOwnershipPercentage)
        assertEquals(7, dossier.currentStep)
        assertEquals("Active", dossier.misaStatus)
    }

    @Test
    fun `verify saudization ratio calculation`() {
        val totalEmployees = 25
        val saudiEmployees = 9
        val ratio = (saudiEmployees * 100) / totalEmployees

        assertEquals(36, ratio)
        assertTrue("Ratio should qualify for High Green tier", ratio >= 30)
    }

    @Test
    fun `verify statutory compliance item creation`() {
        val complianceItem = ComplianceItemEntity(
            id = 1,
            govEntity = "ZATCA",
            title = "Monthly VAT 15% Filing & Payment",
            description = "Submit value-added tax declaration for preceding tax period.",
            dueDate = "Due in 14 days",
            status = "PENDING_ACTION",
            penaltyNotice = "SAR 5,000 to 25% of tax due",
            actionCategory = "Tax & Customs"
        )

        assertEquals("ZATCA", complianceItem.govEntity)
        assertEquals("PENDING_ACTION", complianceItem.status)
    }

    @Test
    fun `verify nafath sso initiation and two digit code generation`() = kotlinx.coroutines.runBlocking {
        val service = com.example.data.nafath.SaudiGovIntegrationService()
        service.initiateNafathAuth("1089334215")

        val session = service.nafathSession.value
        assertEquals(com.example.data.nafath.NafathAuthStatus.WAITING_USER_ACTION, session.status)
        assertTrue("Verification code should be 2 digits", session.verificationCode in 10..99)
        assertTrue("Transaction ID should start with NFT-", session.transactionId.startsWith("NFT-"))
    }

    @Test
    fun `verify nafath verification approval and identity data`() = kotlinx.coroutines.runBlocking {
        val service = com.example.data.nafath.SaudiGovIntegrationService()
        service.initiateNafathAuth("1089334215")
        service.completeNafathVerification(approved = true)

        val identity = service.authenticatedUser.value
        org.junit.Assert.assertNotNull(identity)
        assertEquals("1089334215", identity?.nationalId)
        assertTrue(identity!!.isAuthorizedSignatory)
        assertTrue(identity.authorizedCompanies.isNotEmpty())
    }

    @Test
    fun `verify wathq commercial registration live inquiry`() = kotlinx.coroutines.runBlocking {
        val service = com.example.data.nafath.SaudiGovIntegrationService()
        val result = service.queryWathqCr("1010892341")

        assertEquals("1010892341", result.crNumber)
        assertTrue(result.status.contains("ساري"))
        assertTrue(result.capitalSar >= 100000.0)
    }

    @Test
    fun `verify sbtech sa live wathq and zatca government interconnection`() = kotlinx.coroutines.runBlocking {
        val service = com.example.data.nafath.SaudiGovIntegrationService()
        
        // 1. Wathq CR for sbtech.sa
        val wathqResult = service.queryWathqCr("1010776412")
        assertEquals("1010776412", wathqResult.crNumber)
        assertTrue(wathqResult.companyName.contains("SBTech"))
        assertTrue(wathqResult.companyName.contains("sbtech.sa"))
        assertTrue(wathqResult.status.contains("ساري"))

        // 2. ZATCA Fatoora Phase 2 for sbtech.sa
        val zatcaResult = service.queryZatcaTax("310582910400003")
        assertEquals("310582910400003", zatcaResult.tinNumber)
        assertTrue(zatcaResult.fatooraPhase2Integrated)
        assertTrue(zatcaResult.vatCertificateValid)

        // 3. Qiwa Saudization for sbtech.sa
        val qiwaResult = service.queryQiwaLabor("7-882193")
        assertEquals("7-882193", qiwaResult.establishmentNo)
        assertTrue(qiwaResult.nitaqatTier.contains("Platinum"))
        assertTrue(qiwaResult.saudizationRatio > 40.0)
    }
}

