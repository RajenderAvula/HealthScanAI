package com.example.healthscanai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VitalMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val value1: Double,
    val value2: Double? = null,
    val unit: String,
    val source: String = "manual",
    val recordedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)

@Entity
data class SymptomEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bodyArea: String,
    val symptom: String,
    val severity: Int,
    val durationDays: Int,
    val notes: String = "",
    val recordedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)

@Entity
data class LabResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testName: String,
    val value: String,
    val unit: String = "",
    val referenceRange: String = "",
    val testDate: Long = System.currentTimeMillis(),
    val sourceDocument: String = "",
    val synced: Boolean = false
)

@Entity
data class ScreeningFinding(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val confidence: Double? = null,
    val recommendation: String,
    val createdAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)
