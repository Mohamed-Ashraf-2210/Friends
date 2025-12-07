package com.trrycaar.friends.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.util.network.NetworkMonitor
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.data.remote.dto.posts.Pagination
import com.trrycaar.friends.data.remote.dto.posts.PostDto
import com.trrycaar.friends.data.remote.dto.posts.PostsDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class PostRepositoryImplTest {

    private lateinit var postLocal: PostLocalDataSource
    private lateinit var postRemote: PostRemoteDataSource
    private lateinit var appDatabase: FriendsDatabase
    private lateinit var repository: PostRepositoryImpl
    private lateinit var networkMonitor: NetworkMonitor

    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        networkMonitor = mockk {
            coEvery { isConnected } returns MutableStateFlow(true)
        }
        postLocal = mockk(relaxed = true)
        postRemote = mockk(relaxed = true)
        appDatabase = mockk(relaxed = true)
        repository = PostRepositoryImpl(postLocal, postRemote, appDatabase, networkMonitor)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `getPostsPaging loads from remote and saves to local`() = runTest {
//        // Arrange
//        val postsDto = listOf(
//            PostDto(id = 1, title = "title1", body = "body1"),
//            PostDto(id = 2, title = "title2", body = "body2")
//        )
//
//        coEvery { postRemote.getPosts(1, 10) } returns PostsDto(
//            posts = postsDto,
//            pagination = Pagination(
//                hasNext = true,
//                hasPrev = false,
//                limit = 10,
//                total = 2,
//                totalPages = 1,
//                page = 1
//            )
//        )
//
//        // page 2 empty
//        coEvery { postRemote.getPosts(2, 10) } returns PostsDto(
//            posts = emptyList(),
//            pagination = Pagination(
//                hasNext = false,
//                hasPrev = true,
//                limit = 10,
//                total = 2,
//                totalPages = 1,
//                page = 2
//            )
//        )
//
//        // Fake local DB after saving
//        coEvery { postLocal.getPosts() } returns FakePostPagingSource(
//            data = listOf(
//                PostEntity("1", "title1", "body1"),
//                PostEntity("2", "title2", "body2")
//            )
//        )
//
//        val items = repository.getPostsPaging().asSnapshot {
//            scrollTo(20)
//        }
//
//        // Assert
//        assertEquals(2, items.size)
//        assertEquals("title1", items[0].title)
//
//        coVerify {
//            postLocal.savePosts(
//                match { it.size == 2 && it[0].id == "1" }
//            )
//        }
//    }


//    @Test
//    fun `getFavoritePostsPaging loads from local only`() = runTest {
//        // Arrange
//        val favorites = listOf(
//            PostEntity("10", "fav title", "fav body")
//        )
//
//        coEvery { postLocal.getFavorites(1, 10) } returns favorites
//        coEvery { postLocal.getFavorites(2, 10) } returns emptyList()
//
//        val items = repository.getFavoritePostsPaging().asSnapshot { scrollTo(10) }
//
//
//        assertEquals(1, items.size)
//        assertEquals("10", items[0].id)
//        assertEquals("fav title", items[0].title)
//
//        coVerify(exactly = 0) { postRemote.getPosts(any(), any()) }
//    }
//
//    @Test
//    fun `saveToFavorite calls local save`() = runTest {
//        val postId = "55"
//        val isFavorite = true
//
//        coEvery { postLocal.saveToFavorite(postId, isFavorite) } returns Unit
//
//        repository.saveToFavorite(postId, isFavorite)
//
//        coVerify { postLocal.saveToFavorite(postId, isFavorite) }
//    }
//
//    @Test
//    fun `saveToFavorite throws FriendDatabaseException`() = runTest {
//        coEvery { postLocal.saveToFavorite("11", true) } throws FriendDatabaseException("")
//
//        assertFailsWith<FriendDatabaseException> {
//            repository.saveToFavorite("11", true)
//        }
//    }
}

class FakePostPagingSource(
    private val data: List<PostEntity>
) : PagingSource<Int, PostEntity>() {
    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? = null


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
        )
    }
}