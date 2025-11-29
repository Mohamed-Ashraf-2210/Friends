package com.trrycaar.friends.presentation.screen.home.viewModle

import androidx.paging.PagingData
import app.cash.turbine.test
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
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
    private val networkMonitor = mockk<NetworkMonitor> {
        coEvery { isConnected } returns fakeNetworkFlow
    }
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { postRepository.getPostsPaging() } returns flowOf(
            PagingData.from(listOf(dummyPost()))
        )
        homeViewModel = HomeViewModel(postRepository, networkMonitor, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `networkMonitor SHOULD emit Offline connection`() = runTest {
        homeViewModel.effect.test {
            fakeNetworkFlow.value = false
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals("Offline connection", (effect as HomeEffects.ShowMessage).message)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onPostClicked SHOULD emit navigation effect`() = runTest {
        homeViewModel.effect.test {
            homeViewModel.onPostClicked("10")

            val effect = awaitItem()
            assertEquals(HomeEffects.NavigateToPostDetails("10"), effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `networkMonitor SHOULD emit Online connection`() = runTest {
        Dispatchers.resetMain()
        val offlineFakeNetworkFlow = MutableStateFlow(false)
        val offlineNetworkMonitor = mockk<NetworkMonitor> {
            coEvery { isConnected } returns offlineFakeNetworkFlow
        }
        val offlineViewModel = HomeViewModel(postRepository, offlineNetworkMonitor, testDispatcher)
        Dispatchers.setMain(testDispatcher)

        offlineViewModel.effect.test {
            offlineFakeNetworkFlow.value = true
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals("Online connection", (effect as HomeEffects.ShowMessage).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `showMessage SHOULD emit generic ShowMessage effect`() = runTest {
        homeViewModel.effect.test {
            val testMessage = "Test Error"
            homeViewModel.showMessage(testMessage)

            val effect = awaitItem()
            assertEquals(
                "Error loading posts: $testMessage",
                (effect as HomeEffects.ShowMessage).message
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun dummyPost() = Post(
        id = "1",
        title = "Test Post",
        body = "This is a test post body.",
        isFavorite = false
    )
}