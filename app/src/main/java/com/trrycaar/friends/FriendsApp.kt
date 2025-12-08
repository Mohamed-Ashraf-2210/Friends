package com.trrycaar.friends

import android.app.Application
import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.trrycaar.friends.data.network_worker.NetworkWorker
import com.trrycaar.friends.di.friendsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class FriendsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleNetworkWork(this)
        startKoin {
            androidLogger()
            androidContext(this@FriendsApp)
            modules(friendsModule)
        }
    }
    fun scheduleNetworkWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NetworkWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}