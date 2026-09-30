package com.example.healthscanai.data.remote

import android.content.Context
import com.example.healthscanai.data.local.HealthRepository
import java.util.UUID

class SyncRepository(
    context: Context,
    private val repository: HealthRepository,
    private val api: SyncApi
) {
    private val prefs = context.getSharedPreferences("sync", Context.MODE_PRIVATE)

    fun enabled(): Boolean = prefs.getBoolean("enabled", false)
    fun baseUrl(): String = prefs.getString("baseUrl", "") ?: ""

    fun configure(baseUrl: String, enabled: Boolean) {
        prefs.edit()
            .putString("baseUrl", baseUrl.trimEnd('/'))
            .putBoolean("enabled", enabled)
            .apply()
    }

    suspend fun sync(): Result<Unit> {
        if (!enabled()) return Result.success(Unit)
        val base = baseUrl()
        if (base.isBlank()) return Result.failure(IllegalStateException("Sync URL is empty"))

        return runCatching {
            val payload = SyncPayload(
                deviceId = deviceId(),
                sentAt = System.currentTimeMillis(),
                vitals = repository.unsyncedVitals().map { it.toDto() },
                symptoms = repository.unsyncedSymptoms().map { it.toDto() },
                labs = repository.unsyncedLabs().map { it.toDto() },
                findings = repository.unsyncedFindings().map { it.toDto() }
            )
            val response = api.sync("$base/v1/sync", payload)
            check(response.isSuccessful) { "Server returned ${response.code()}" }

            repository.markVitals(payload.vitals.map { it.id })
            repository.markSymptoms(payload.symptoms.map { it.id })
            repository.markLabs(payload.labs.map { it.id })
            repository.markFindings(payload.findings.map { it.id })
        }
    }

    private fun deviceId(): String {
        val existing = prefs.getString("deviceId", null)
        if (existing != null) return existing
        val id = UUID.randomUUID().toString()
        prefs.edit().putString("deviceId", id).apply()
        return id
    }
}
