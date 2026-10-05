package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_dossiers")
data class CompanyDossierEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyName: String,
    val arabicName: String,
    val licenseType: String, // Services, Industrial, Trading, RHQ, Entrepreneurship
    val legalStructure: String, // LLC, SJSC, Branch of Foreign Company
    val misaStatus: String, // Draft, In Review, Approved, Active
    val crNumber: String, // 1010xxxxxx or "Under Issuance"
    val capitalSar: Double,
    val foreignOwnershipPercentage: Int, // e.g. 100
    val saudizationTier: String, // Platinum, High Green, Medium Green, Red
    val currentStep: Int, // 1 to 7
    val totalEmployees: Int = 10,
    val saudiEmployees: Int = 4,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "compliance_items")
data class ComplianceItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int = 0,
    val govEntity: String, // MISA, MC, ZATCA, GOSI, Qiwa, Balady, Muqeem
    val title: String,
    val description: String,
    val dueDate: String,
    val status: String, // COMPLIANT, PENDING_ACTION, OVERDUE, UPCOMING
    val penaltyNotice: String,
    val actionCategory: String
)

@Entity(tableName = "consultation_records")
data class ConsultationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val investorName: String,
    val investorEmail: String,
    val targetSector: String,
    val serviceRequested: String,
    val scheduledDate: String,
    val advisoryNotes: String,
    val status: String, // CONFIRMED, COMPLETED, IN_REVIEW
    val timestamp: Long = System.currentTimeMillis()
)
