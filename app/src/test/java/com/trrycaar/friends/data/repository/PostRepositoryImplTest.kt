package com.trrycaar.friends.data.repository

import androidx.paging.testing.asSnapshot
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.data.remote.dto.posts.Pagination
import com.trrycaar.friends.data.remote.dto.posts.PostDto
import com.trrycaar.friends.data.remote.dto.posts.PostsDto
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith


class PostRepositoryImplTest {

    private lateinit var postLocal: PostLocalDataSource
    private lateinit var postRemote: PostRemoteDataSource
    private lateinit var repository: PostRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        postLocal = mockk(relaxed = true)
        postRemote = mockk(relaxed = true)
        repository = PostRepositoryImpl(postLocal, postRemote)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPostsPaging loads from remote and saves to local`() = runTest {
        // Arrange
        val postsDto = listOf(
            PostDto(id = 1, title = "title1", body = "body1"),
            PostDto(id = 2, title = "title2", body = "body2")
        )

        coEvery { postRemote.getPosts(1, 10) } returns PostsDto(
            posts = postsDto,
            pagination = Pagination(false, false, 10, 1, 2, 1)
        )
        coEvery { postRemote.getPosts(2, 10) } returns PostsDto(
            posts = emptyList(),
            pagination = Pagination(false, false, 10, 1, 2, 1)
        )

        coEvery { postLocal.getPosts(any(), any()) } returns emptyList()

        val items = repository.getPostsPaging().asSnapshot { scrollTo(10) }


        assertEquals(2, items.size)
        assertEquals("title1", items[0].title)

        coVerify {
            postLocal.savePosts(
                match { it.size == 2 && it[0].id == "1" }
            )
        }
    }

    @Test
    fun `getFavoritePostsPaging loads from local only`() = runTest {
        // Arrange
        val favorites = listOf(
            PostEntity("10", "fav title", "fav body")
        )

        coEvery { postLocal.getFavorites(1, 10) } returns favorites
        coEvery { postLocal.getFavorites(2, 10) } returns emptyList()

        val items = repository.getFavoritePostsPaging().asSnapshot { scrollTo(10) }


        assertEquals(1, items.size)
        assertEquals("10", items[0].id)
        assertEquals("fav title", items[0].title)

        coVerify(exactly = 0) { postRemote.getPosts(any(), any()) }
    }

    @Test
    fun `addToFavorite calls local save`() = runTest {
        val postId = "55"
        coEvery { postLocal.saveToFavorite(postId) } returns Unit

        repository.addToFavorite(postId)

        coVerify { postLocal.saveToFavorite(postId) }
    }

    @Test
    fun `addToFavorite throws FriendDatabaseException`() = runTest {
        coEvery { postLocal.saveToFavorite("11") } throws FriendDatabaseException("")

        assertFailsWith<FriendDatabaseException> {
            repository.addToFavorite("11")
        }
    }
}