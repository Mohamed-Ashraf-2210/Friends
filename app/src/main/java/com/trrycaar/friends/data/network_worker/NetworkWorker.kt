package com.trrycaar.friends.data.network_worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource

class NetworkWorker(
    context: Context,
    params: WorkerParameters,
    private val postLocal: PostLocalDataSource
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            postLocal.updateSyncFavoritePosts()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}