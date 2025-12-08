package com.trrycaar.friends.data.network_worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NetworkWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    private val favoritePostsLocal: FavoritePostsLocalDataSource by inject()
    override suspend fun doWork(): Result {
        return try {
            syncFavoritePosts()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun syncFavoritePosts() {
        favoritePostsLocal.updateSyncFavoritePosts()
        favoritePostsLocal.deletePendingRemovedFavorites()
    }
}