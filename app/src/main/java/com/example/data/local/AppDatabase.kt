package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CompanyDossierEntity::class,
        ComplianceItemEntity::class,
        ConsultationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saudiGovDao(): SaudiGovDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "saudi_gov_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.saudiGovDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: SaudiGovDao) {
                // Initial Registered Company Dossier
                val initialDossier = CompanyDossierEntity(
                    companyName = "Apex Global Cloud Solutions Ltd",
                    arabicName = "شركة آبيكس العالمية لحلول السحابة المحدودة",
                    licenseType = "Services & IT",
                    legalStructure = "Limited Liability Company (LLC)",
                    misaStatus = "Active",
                    crNumber = "1010892341",
                    capitalSar = 500000.0,
                    foreignOwnershipPercentage = 100,
                    saudizationTier = "High Green (بلاتيني / أخضر مرتفع)",
                    currentStep = 7,
                    totalEmployees = 16,
                    saudiEmployees = 6
                )
                val secondDossier = CompanyDossierEntity(
                    companyName = "Nordic Energy Systems KSA",
                    arabicName = "شركة أنظمة الطاقة الشمالية السعودية",
                    licenseType = "Industrial & Manufacturing",
                    legalStructure = "Simplified Joint Stock Company (SJSC)",
                    misaStatus = "Under Review",
                    crNumber = "Pending Issuance",
                    capitalSar = 2500000.0,
                    foreignOwnershipPercentage = 100,
                    saudizationTier = "Platinum",
                    currentStep = 3,
                    totalEmployees = 8,
                    saudiEmployees = 3
                )
                val sbtechDossier = CompanyDossierEntity(
                    companyName = "SBTech Saudi Arabia Ltd (sbtech.sa)",
                    arabicName = "شركة إس بي تك للتقنية والأعمال المحدودة",
                    licenseType = "ICT & Digital Gov Services",
                    legalStructure = "Limited Liability Company (LLC)",
                    misaStatus = "Active (100% Foreign/GCC)",
                    crNumber = "1010776412",
                    capitalSar = 1000000.0,
                    foreignOwnershipPercentage = 100,
                    saudizationTier = "Platinum (بلاتيني 42.8%)",
                    currentStep = 8,
                    totalEmployees = 28,
                    saudiEmployees = 12
                )
                dao.insertDossier(initialDossier)
                dao.insertDossier(secondDossier)
                dao.insertDossier(sbtechDossier)

                // Initial Statutory Compliance Radar Items
                val initialCompliance = listOf(
                    ComplianceItemEntity(
                        govEntity = "ZATCA",
                        title = "Monthly VAT 15% Filing & Payment",
                        description = "Submit value-added tax declaration for preceding tax period via ZATCA portal.",
                        dueDate = "Due in 14 days (End of Month)",
                        status = "PENDING_ACTION",
                        penaltyNotice = "SAR 5,000 to 25% of tax due penalty for late submission",
                        actionCategory = "Tax & Customs"
                    ),
                    ComplianceItemEntity(
                        govEntity = "ZATCA",
                        title = "Phase 2 E-Invoicing (Fatoora) Integration",
                        description = "Cryptographic stamping and API clearance integration with ZATCA platform.",
                        dueDate = "Integration Active",
                        status = "COMPLIANT",
                        penaltyNotice = "Non-compliance penalty up to SAR 50,000",
                        actionCategory = "E-Invoicing"
                    ),
                    ComplianceItemEntity(
                        govEntity = "Qiwa (MHRSD)",
                        title = "Saudization (Nitaqat) Ratio Monitoring",
                        description = "Maintain minimum 35% national workforce ratio in IT / Services sector.",
                        dueDate = "Monthly Audit (37.5% Current)",
                        status = "COMPLIANT",
                        penaltyNotice = "Downgrade to Yellow/Red stops work visa renewals",
                        actionCategory = "Labor & Saudization"
                    ),
                    ComplianceItemEntity(
                        govEntity = "GOSI",
                        title = "Social Insurance Monthly Contributions",
                        description = "Monthly occupational hazard and annuity contributions for registered staff.",
                        dueDate = "15th of each calendar month",
                        status = "PENDING_ACTION",
                        penaltyNotice = "2% per month delay fine on overdue amounts",
                        actionCategory = "Social Insurance"
                    ),
                    ComplianceItemEntity(
                        govEntity = "Ministry of Commerce",
                        title = "Commercial Registration (CR) Annual Confirmation",
                        description = "Confirm registered business activities, manager appointment, and fiscal year.",
                        dueDate = "Valid until Nov 2027",
                        status = "COMPLIANT",
                        penaltyNotice = "Suspension of bank accounts upon expiry",
                        actionCategory = "Corporate Licensing"
                    ),
                    ComplianceItemEntity(
                        govEntity = "Balady",
                        title = "Commercial Municipal Activity License",
                        description = "Commercial premise lease contract registration via Ejar & civil defense safety.",
                        dueDate = "Valid until Jan 2027",
                        status = "COMPLIANT",
                        penaltyNotice = "Closure notice if municipal license lapses",
                        actionCategory = "Municipal & Civil Defense"
                    ),
                    ComplianceItemEntity(
                        govEntity = "MISA",
                        title = "Investment License Annual Service Compliance",
                        description = "Submit annual audited financial statements and foreign investment report.",
                        dueDate = "Due in 60 days",
                        status = "UPCOMING",
                        penaltyNotice = "License review flag if delayed beyond 90 days",
                        actionCategory = "Foreign Investment"
                    )
                )
                dao.insertComplianceItems(initialCompliance)

                // Initial Consultation record
                val initialConsult = ConsultationEntity(
                    investorName = "Marcus Vance (Managing Director, Vance Venture Partners)",
                    investorEmail = "m.vance@vancevp.co.uk",
                    targetSector = "Regional Headquarters (RHQ Relocation)",
                    serviceRequested = "30-Year Corporate Tax Holiday & RHQ Relocation Package Advisory",
                    scheduledDate = "Thursday, 2:00 PM AST (Riyadh Time)",
                    advisoryNotes = "Evaluating setup in KAFD Riyadh with 15 initial executive leadership visas.",
                    status = "CONFIRMED"
                )
                dao.insertConsultation(initialConsult)
            }
        }
    }
}
