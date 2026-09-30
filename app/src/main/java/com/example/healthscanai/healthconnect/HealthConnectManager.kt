package com.example.healthscanai.healthconnect

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

class HealthConnectManager(context: Context) {

    private val client: HealthConnectClient? =
        if (HealthConnectClient.getSdkStatus(context) ==
            HealthConnectClient.SDK_AVAILABLE) {
            HealthConnectClient.getOrCreate(context)
        } else null

    val permissions = setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class)
    )

    suspend fun readRecentHeartRates(): List<HeartRateRecord> {
        val c = client ?: return emptyList()
        val now = Instant.now()
        val start = now.minusSeconds(7 * 24 * 60 * 60)
        return c.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(start, now)
            )
        ).records
    }
}
