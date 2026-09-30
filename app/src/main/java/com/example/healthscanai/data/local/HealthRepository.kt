package com.example.healthscanai.data.local

class HealthRepository(private val db: AppDatabase) {
    fun vitals() = db.vitalDao().observeAll()
    fun symptoms() = db.symptomDao().observeAll()
    fun labs() = db.labDao().observeAll()
    fun findings() = db.findingDao().observeAll()

    suspend fun addVital(v: VitalMeasurement) = db.vitalDao().insert(v)
    suspend fun addSymptom(s: SymptomEvent) = db.symptomDao().insert(s)
    suspend fun addLab(l: LabResult) = db.labDao().insert(l)
    suspend fun addFinding(f: ScreeningFinding) = db.findingDao().insert(f)

    suspend fun unsyncedVitals() = db.vitalDao().unsynced()
    suspend fun unsyncedSymptoms() = db.symptomDao().unsynced()
    suspend fun unsyncedLabs() = db.labDao().unsynced()
    suspend fun unsyncedFindings() = db.findingDao().unsynced()

    suspend fun markVitals(ids: List<Long>) = db.vitalDao().markSynced(ids)
    suspend fun markSymptoms(ids: List<Long>) = db.symptomDao().markSynced(ids)
    suspend fun markLabs(ids: List<Long>) = db.labDao().markSynced(ids)
    suspend fun markFindings(ids: List<Long>) = db.findingDao().markSynced(ids)
}
