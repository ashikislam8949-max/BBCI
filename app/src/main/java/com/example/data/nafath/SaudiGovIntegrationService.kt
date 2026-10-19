package com.example.data.nafath

import kotlinx.coroutines.delay

class SaudiGovIntegrationService {

    // Sample planning records only. This service does not sign in to a government system.
    // 2. Wathq (Ministry of Commerce) API Query
    suspend fun queryWathqCr(crNumber: String): WathqCrResult {
        delay(800) // Simulated real-time Gov gateway latency
        val cleanCr = crNumber.trim().ifBlank { "1010776412" }
        val isSbTech = cleanCr == "1010776412" || cleanCr.contains("sbtech", ignoreCase = true)

        return if (isSbTech) {
            WathqCrResult(
                crNumber = "1010776412",
                companyName = "SBTech Saudi Arabia Limited (sbtech.sa)",
                arabicName = "شركة إس بي تك للتقنية والأعمال المحدودة",
                status = "ساري وموثق (Active & Certified)",
                issueDate = "1443/07/15هـ (2022-02-16)",
                expiryDate = "1448/07/14هـ (2027-02-10)",
                capitalSar = 1000000.0,
                legalForm = "شركة ذات مسؤولية محدودة (LLC - Tech & Gov Services)",
                mainActivity = "620200 - استشارات تقنية المعلومات وحلول التحول الرقمي الحكومي (Digital Gov Solutions)",
                city = "الرياض - طريق الملك فهد (Riyadh, King Fahd Rd)",
                managerName = "Sample contact",
                chamberOfCommerceNo = "CCI-RD-993821"
            )
        } else {
            WathqCrResult(
                crNumber = cleanCr,
                companyName = "Apex Global Cloud Solutions Limited",
                arabicName = "شركة آبيكس العالمية لحلول السحابة المحدودة",
                status = "ساري وموثق (Active & Certified)",
                issueDate = "1444/03/12هـ (2022-10-08)",
                expiryDate = "1449/03/11هـ (2027-10-06)",
                capitalSar = 500000.0,
                legalForm = "شركة ذات مسؤولية محدودة (LLC - 100% Foreign Owned)",
                mainActivity = "620101 - تطوير البرمجيات وحلول السحابة الإلكترونية (Cloud Systems & Software)",
                city = "الرياض (Riyadh - KAFD)",
                managerName = "Sample contact",
                chamberOfCommerceNo = "CCI-RD-884219"
            )
        }
    }

    // 3. ZATCA Tax & Fatoora Phase 2 API Query
    suspend fun queryZatcaTax(tin: String): ZatcaTaxResult {
        delay(700)
        val cleanTin = tin.trim().ifBlank { "310582910400003" }
        val isSbTech = cleanTin == "310582910400003" || cleanTin.contains("sbtech", ignoreCase = true)

        return if (isSbTech) {
            ZatcaTaxResult(
                tinNumber = "310582910400003",
                taxPayerName = "SBTech Saudi Arabia Ltd (sbtech.sa)",
                vatStatus = "مسجل وفعال في ضريبة القيمة المضافة 15%",
                vatCertificateValid = true,
                fatooraPhase2Integrated = true,
                zakatCertificateExpiry = "2027-04-30",
                complianceRating = "Grade A+ (Full Tax Clearance & Fatoora Cryptographic Clearance)"
            )
        } else {
            ZatcaTaxResult(
                tinNumber = cleanTin,
                taxPayerName = "Apex Global Cloud Solutions KSA",
                vatStatus = "مسجل وفعال في ضريبة القيمة المضافة 15%",
                vatCertificateValid = true,
                fatooraPhase2Integrated = true,
                zakatCertificateExpiry = "2027-04-30",
                complianceRating = "Grade A+ (Full Tax Clearance & Fatoora Integrated)"
            )
        }
    }

    // 4. Qiwa Labor & Nitaqat API Query
    suspend fun queryQiwaLabor(estNo: String): QiwaLaborResult {
        delay(700)
        val cleanEst = estNo.trim().ifBlank { "7-882193" }
        val isSbTech = cleanEst == "7-882193" || cleanEst.contains("sbtech", ignoreCase = true)

        return if (isSbTech) {
            QiwaLaborResult(
                establishmentNo = "7-882193",
                establishmentName = "شركة إس بي تك السعودية (sbtech.sa)",
                nitaqatTier = "Platinum (نطاق بلاتيني متقدم)",
                saudizationRatio = 42.8,
                totalEmployees = 28,
                saudiEmployees = 12,
                availableVisas = 20,
                mudadWpsCompliance = "100% Compliant (حماية الأجور موثقة ومعتمدة)"
            )
        } else {
            QiwaLaborResult(
                establishmentNo = cleanEst,
                establishmentName = "شركة آبيكس العالمية - فرع الرياض",
                nitaqatTier = "High Green (نطاق أخضر مرتفع)",
                saudizationRatio = 37.5,
                totalEmployees = 16,
                saudiEmployees = 6,
                availableVisas = 12,
                mudadWpsCompliance = "100% Compliant (حماية الأجور موثقة)"
            )
        }
    }

    // 5. Balady Municipal License API Query
    suspend fun queryBaladyLicense(licenseNo: String): BaladyLicenseResult {
        delay(600)
        val cleanLic = licenseNo.trim().ifBlank { "4409923812" }
        val isSbTech = cleanLic == "4409923812" || cleanLic.contains("sbtech", ignoreCase = true)

        return if (isSbTech) {
            BaladyLicenseResult(
                licenseNumber = "4409923812",
                activityName = "مكاتب تقنية معلومات وحلول تحول رقمي (IT & Digital Hub)",
                premiseAddress = "الرياض، حي العليا، طريق الأمير محمد بن عبدالعزيز / طريق الملك فهد",
                civilDefenseApproved = true,
                status = "سارية ومعتمدة (Active Municipal License)",
                expiryDate = "1449/05/18هـ (2027-10-25)"
            )
        } else {
            BaladyLicenseResult(
                licenseNumber = cleanLic,
                activityName = "مكاتب إدارية واستشارات تقنية وفنية (Commercial Offices)",
                premiseAddress = "مجمع مركز الملك عبدالله المالي (KAFD), البرج 4.02",
                civilDefenseApproved = true,
                status = "سارية ومعتمدة (Active Municipal License)",
                expiryDate = "1448/09/20هـ (2026-12-30)"
            )
        }
    }
}
