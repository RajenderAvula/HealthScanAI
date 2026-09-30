package com.example.healthscanai.sync

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class SyncScheduler {
    companion object {
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<HealthSyncWorker>(
                6, TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "healthscan-online-sync",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
