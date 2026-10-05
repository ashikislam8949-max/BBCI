package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SaudiGovDao {
    // Dossiers
    @Query("SELECT * FROM company_dossiers ORDER BY timestamp DESC")
    fun getAllDossiers(): Flow<List<CompanyDossierEntity>>

    @Query("SELECT * FROM company_dossiers WHERE id = :id")
    suspend fun getDossierById(id: Int): CompanyDossierEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDossier(dossier: CompanyDossierEntity): Long

    @Update
    suspend fun updateDossier(dossier: CompanyDossierEntity)

    @Query("DELETE FROM company_dossiers WHERE id = :id")
    suspend fun deleteDossierById(id: Int)

    // Compliance
    @Query("SELECT * FROM compliance_items ORDER BY id ASC")
    fun getAllComplianceItems(): Flow<List<ComplianceItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplianceItems(items: List<ComplianceItemEntity>)

    @Update
    suspend fun updateComplianceItem(item: ComplianceItemEntity)

    // Consultations
    @Query("SELECT * FROM consultation_records ORDER BY timestamp DESC")
    fun getAllConsultations(): Flow<List<ConsultationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsultation(consultation: ConsultationEntity): Long

    @Update
    suspend fun updateConsultation(consultation: ConsultationEntity)

    @Query("DELETE FROM consultation_records WHERE id = :id")
    suspend fun deleteConsultationById(id: Int)
}
