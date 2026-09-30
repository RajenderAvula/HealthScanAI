package com.example.healthscanai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        VitalMeasurement::class,
        SymptomEvent::class,
        LabResult::class,
        ScreeningFinding::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vitalDao(): VitalDao
    abstract fun symptomDao(): SymptomDao
    abstract fun labDao(): LabDao
    abstract fun findingDao(): FindingDao
}
