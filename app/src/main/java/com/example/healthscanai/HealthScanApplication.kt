package com.example.healthscanai

import android.app.Application
import androidx.room.Room
import com.example.healthscanai.data.local.AppDatabase
import com.example.healthscanai.data.local.HealthRepository
import com.example.healthscanai.data.remote.SyncApi
import com.example.healthscanai.data.remote.SyncRepository
import com.example.healthscanai.sync.SyncScheduler

class HealthScanApplication : Application() {
    lateinit var database: AppDatabase
    lateinit var repository: HealthRepository
    lateinit var syncRepository: SyncRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "healthscan.db"
        ).fallbackToDestructiveMigration().build()

        repository = HealthRepository(database)

        val api = SyncApi.create()
        syncRepository = SyncRepository(this, repository, api)

        SyncScheduler.schedule(this)
    }
}
