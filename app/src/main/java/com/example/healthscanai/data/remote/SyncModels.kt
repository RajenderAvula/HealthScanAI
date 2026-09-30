package com.example.healthscanai.data.remote

import com.example.healthscanai.data.local.*
import kotlinx.serialization.Serializable

@Serializable
data class SyncPayload(
    val deviceId: String,
    val sentAt: Long,
    val vitals: List<VitalDto>,
    val symptoms: List<SymptomDto>,
    val labs: List<LabDto>,
    val findings: List<FindingDto>
)

@Serializable
data class VitalDto(
    val id: Long,
    val type: String,
    val value1: Double,
    val value2: Double?,
    val unit: String,
    val source: String,
    val recordedAt: Long
)

@Serializable
data class SymptomDto(
    val id: Long,
    val bodyArea: String,
    val symptom: String,
    val severity: Int,
    val durationDays: Int,
    val notes: String,
    val recordedAt: Long
)

@Serializable
data class LabDto(
    val id: Long,
    val testName: String,
    val value: String,
    val unit: String,
    val referenceRange: String,
    val testDate: Long,
    val sourceDocument: String
)

@Serializable
data class FindingDto(
    val id: Long,
    val category: String,
    val title: String,
    val description: String,
    val level: String,
    val confidence: Double?,
    val recommendation: String,
    val createdAt: Long
)

fun VitalMeasurement.toDto() = VitalDto(id, type, value1, value2, unit, source, recordedAt)
fun SymptomEvent.toDto() = SymptomDto(id, bodyArea, symptom, severity, durationDays, notes, recordedAt)
fun LabResult.toDto() = LabDto(id, testName, value, unit, referenceRange, testDate, sourceDocument)
fun ScreeningFinding.toDto() = FindingDto(id, category, title, description, level, confidence, recommendation, createdAt)
