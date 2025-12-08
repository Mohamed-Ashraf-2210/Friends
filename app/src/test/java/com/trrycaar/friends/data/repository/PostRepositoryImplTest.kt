package com.trrycaar.friends.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.entity.PostEntity
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import io.mockk.every
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


class PostRepositoryImplTest {

    private lateinit var postLocal: PostLocalDataSource
    private lateinit var postRemote: PostRemoteDataSource
    private lateinit var appDatabase: FriendsDatabase
    private lateinit var repository: PostRepositoryImpl

    private val dispatcher = StandardTestDispatcher()

    private fun dummyPostEntity(
        id: String = "1",
        title: String = "Test",
        body: String = "Body"
    ) = PostEntity(id, title, body)


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        postLocal = mockk(relaxed = true)
        postRemote = mockk(relaxed = true)
        appDatabase = mockk(relaxed = true)
        repository = PostRepositoryImpl(postLocal, postRemote, appDatabase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavoritePostsPaging_returns_correct_data_from_local_source() = runTest {
        val localPosts = listOf(
            dummyPostEntity(id = "1", title = "Fav Post 1"),
            dummyPostEntity(id = "2", title = "Fav Post 2"),
            dummyPostEntity(id = "3", title = "Fav Post 3")
        )
        val fakePagingSource = fakePagingSource(localPosts)


        every { postLocal.getFavoritePosts() } returns fakePagingSource

        val pagingFlow = repository.getFavoritePostsPaging().asSnapshot()


        assertEquals(3, pagingFlow.size)
        assertEquals("Fav Post 1", pagingFlow.first().title)
        assertEquals("Fav Post 3", pagingFlow.last().title)
    }
}

fun fakePagingSource(posts: List<PostEntity>) = object : PagingSource<Int, PostEntity>() {
    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
        return LoadResult.Page(
            data = posts,
            prevKey = null,
            nextKey = null
        )
    }
}