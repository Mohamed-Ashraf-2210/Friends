package com.trrycaar.friends.data.repository

import com.trrycaar.friends.data.local.dataSource.OfflineFavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.OfflineFavoritePostEntity
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith


class OfflineFavoritePostRepositoryImplTest {
    private lateinit var offlineLocal: OfflineFavoritePostsLocalDataSource
    private lateinit var postLocal: PostLocalDataSource
    private lateinit var repository: OfflineFavoritePostRepositoryImpl

    @BeforeTest
    fun setup() {
        offlineLocal = mockk(relaxed = true)
        postLocal = mockk(relaxed = true)
        repository = OfflineFavoritePostRepositoryImpl(offlineLocal, postLocal)
    }

    @Test
    fun `addPostToOfflineFavorite saves entity successfully`() = runTest {
        val postId = "99"

        coEvery { offlineLocal.saveOfflinePostToFavorite(any()) } returns Unit

        repository.addPostToOfflineFavorite(postId)

        coVerify(exactly = 1) {
            offlineLocal.saveOfflinePostToFavorite(
                OfflineFavoritePostEntity(postId)
            )
        }
    }

    @Test
    fun `addPostToOfflineFavorite throws FriendDatabaseException`() = runTest {
        val postId = "123"

        coEvery { offlineLocal.saveOfflinePostToFavorite(any()) } throws FriendDatabaseException("")

        assertFailsWith<FriendDatabaseException> {
            repository.addPostToOfflineFavorite(postId)
        }
    }

    @Test
    fun `syncOfflineFavorites uploads posts and deletes them`() = runTest {
        val offlineList = listOf(
            OfflineFavoritePostEntity("1"),
            OfflineFavoritePostEntity("2")
        )

        coEvery { offlineLocal.getAll() } returns offlineList

        repository.syncOfflineFavorites()

        coVerify { postLocal.saveToFavorite("1") }
        coVerify { postLocal.saveToFavorite("2") }

        coVerify { offlineLocal.deleteById("1") }
        coVerify { offlineLocal.deleteById("2") }
    }

    @Test
    fun `syncOfflineFavorites throws FriendDatabaseException`() = runTest {
        coEvery { offlineLocal.getAll() } throws FriendDatabaseException("")

        assertFailsWith<FriendDatabaseException> {
            repository.syncOfflineFavorites()
        }
    }
}