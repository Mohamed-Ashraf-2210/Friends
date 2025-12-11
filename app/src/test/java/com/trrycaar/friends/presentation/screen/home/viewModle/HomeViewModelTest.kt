package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.paging.PagingData
import app.cash.turbine.test
import com.trrycaar.friends.domain.NetworkObserver
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.screen.helper.collectForTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val postRepository = mockk<PostRepository>()
    private val fakeNetworkFlow = MutableStateFlow(true)
    private val networkObserver = mockk<NetworkObserver> {
        coEvery { isConnected } returns fakeNetworkFlow
    }
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun dummyPost(
        id: String = "1",
        title: String = "Test Post",
        body: String = "This is a test post body.",
        isFavorite: Boolean = false
    ) = Post(id, title, body, isFavorite)

    private fun createViewModelWithPosts(posts: List<Post> = listOf(dummyPost())) {
        coEvery { postRepository.getPostsPaging() } returns flowOf(PagingData.from(posts))
        homeViewModel = HomeViewModel(postRepository, networkObserver, testDispatcher)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `networkMonitor SHOULD emit Offline connection`() = runTest {
        fakeNetworkFlow.value = false
        createViewModelWithPosts()
        homeViewModel.effect.test {
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals("Offline connection", (effect as HomeEffects.ShowMessage).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `networkObserver SHOULD emit Online connection`() = runTest {
        coEvery { postRepository.getPostsPaging() } returns flowOf(PagingData.empty())
        val offlineFakeNetworkFlow = MutableStateFlow(false)
        val offlineNetworkObserver = mockk<NetworkObserver> {
            coEvery { isConnected } returns offlineFakeNetworkFlow
        }

        offlineFakeNetworkFlow.value = true

        val offlineViewModel = HomeViewModel(postRepository, offlineNetworkObserver, testDispatcher)

        offlineViewModel.effect.test {
            advanceUntilIdle()
            val effect = awaitItem()
            assertEquals("Online connection", (effect as HomeEffects.ShowMessage).message)
        }
    }

    @Test
    fun `onPostClicked SHOULD emit navigation effect`() = runTest {
        createViewModelWithPosts()
        homeViewModel.effect.test {
            homeViewModel.onPostClicked("10")

            val effect = awaitItem()
            assertEquals(HomeEffects.NavigateToPostDetails("10"), effect)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `postsPaging SHOULD emit empty PagingData initially`() = runTest {
        createViewModelWithPosts(emptyList())
        homeViewModel.postsPaging.test {
            skipItems(1)
            val pagingData = awaitItem()
            assertEquals(0, pagingData.collectForTest().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `postsPaging SHOULD return success`() = runTest {
        createViewModelWithPosts()
        advanceUntilIdle()
        homeViewModel.postsPaging.test {
            skipItems(1)
            val pagingData = awaitItem()

            val items = pagingData.collectForTest()

            assertEquals(1, items.size)
            assertEquals("1", items[0].id)
            assertEquals("Test Post", items[0].title)
        }
    }

    @Test
    fun `showToastErrorMessage SHOULD emit error message`() = runTest {
        createViewModelWithPosts()

        val error = Throwable("Error!!")

        homeViewModel.effect.test {
            homeViewModel.showToastErrorMessage(error)

            val effect = awaitItem()
            assertEquals("Error!!", (effect as HomeEffects.ShowMessage).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toUiState SHOULD convert Post to PostUiState correctly`() = runTest {
        val post = Post(
            id = "123",
            title = "Test Title",
            body = "Test Body",
            isFavorite = true
        )

        val uiState = post.toUiState()

        assertEquals("123", uiState.id)
        assertEquals("Test Title", uiState.title)
        assertEquals("Test Body", uiState.body)
    }
}