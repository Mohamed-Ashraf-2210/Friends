package com.trrycaar.friends.data.local.dataSource

import com.trrycaar.friends.data.local.dao.FavoritePostsDao
import com.trrycaar.friends.data.util.safeDbCall

class FavoritePostsLocalDataSourceImpl(
    private val favoritePostsDao: FavoritePostsDao
) : FavoritePostsLocalDataSource {
    override suspend fun addFavorite(id: String, isSync: Boolean) {
        safeDbCall {
            favoritePostsDao.addFavorite(id, isSync)
        }
    }

    override suspend fun removeFavorite(id: String, isSync: Boolean) {
        safeDbCall {
            favoritePostsDao.removeFavorite(id, isSync)
        }
    }

    override suspend fun updateSyncFavoritePosts() {
        safeDbCall {
            favoritePostsDao.updateSyncFavoritePosts()
        }
    }

    override suspend fun deletePendingRemovedFavorites() {
        safeDbCall {
            favoritePostsDao.deleteUnsyncedUnfavoritePosts()
        }
    }

    override suspend fun getFavoritePostState(id: String): Boolean {
        return safeDbCall {
            (favoritePostsDao.getFavoritePostState(id) ?: false) == 1
        }
    }
}