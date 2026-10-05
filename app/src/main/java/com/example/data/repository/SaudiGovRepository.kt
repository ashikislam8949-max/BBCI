package com.example.data.repository

import com.example.data.local.CompanyDossierEntity
import com.example.data.local.ComplianceItemEntity
import com.example.data.local.ConsultationEntity
import com.example.data.local.SaudiGovDao
import kotlinx.coroutines.flow.Flow

class SaudiGovRepository(private val dao: SaudiGovDao) {

    val dossiers: Flow<List<CompanyDossierEntity>> = dao.getAllDossiers()
    val complianceItems: Flow<List<ComplianceItemEntity>> = dao.getAllComplianceItems()
    val consultations: Flow<List<ConsultationEntity>> = dao.getAllConsultations()

    suspend fun getDossierById(id: Int): CompanyDossierEntity? = dao.getDossierById(id)

    suspend fun insertDossier(dossier: CompanyDossierEntity): Long = dao.insertDossier(dossier)

    suspend fun updateDossier(dossier: CompanyDossierEntity) = dao.updateDossier(dossier)

    suspend fun deleteDossierById(id: Int) = dao.deleteDossierById(id)

    suspend fun updateComplianceItem(item: ComplianceItemEntity) = dao.updateComplianceItem(item)

    suspend fun insertConsultation(consultation: ConsultationEntity): Long = dao.insertConsultation(consultation)
    suspend fun updateConsultation(consultation: ConsultationEntity) = dao.updateConsultation(consultation)
    suspend fun deleteConsultationById(id: Int) = dao.deleteConsultationById(id)
}
