package com.example.healthscanai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalDao {
    @Insert suspend fun insert(item: VitalMeasurement)
    @Query("SELECT * FROM VitalMeasurement ORDER BY recordedAt DESC")
    fun observeAll(): Flow<List<VitalMeasurement>>
    @Query("SELECT * FROM VitalMeasurement WHERE synced = 0")
    suspend fun unsynced(): List<VitalMeasurement>
    @Query("UPDATE VitalMeasurement SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)
}

@Dao
interface SymptomDao {
    @Insert suspend fun insert(item: SymptomEvent)
    @Query("SELECT * FROM SymptomEvent ORDER BY recordedAt DESC")
    fun observeAll(): Flow<List<SymptomEvent>>
    @Query("SELECT * FROM SymptomEvent WHERE synced = 0")
    suspend fun unsynced(): List<SymptomEvent>
    @Query("UPDATE SymptomEvent SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)
}

@Dao
interface LabDao {
    @Insert suspend fun insert(item: LabResult)
    @Query("SELECT * FROM LabResult ORDER BY testDate DESC")
    fun observeAll(): Flow<List<LabResult>>
    @Query("SELECT * FROM LabResult WHERE synced = 0")
    suspend fun unsynced(): List<LabResult>
    @Query("UPDATE LabResult SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)
}

@Dao
interface FindingDao {
    @Insert suspend fun insert(item: ScreeningFinding)
    @Query("SELECT * FROM ScreeningFinding ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ScreeningFinding>>
    @Query("SELECT * FROM ScreeningFinding WHERE synced = 0")
    suspend fun unsynced(): List<ScreeningFinding>
    @Query("UPDATE ScreeningFinding SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)
}
