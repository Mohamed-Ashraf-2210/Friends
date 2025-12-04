package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository
import com.trrycaar.friends.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
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

class PostDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val postRepository = mockk<PostRepository>(relaxed = true)
    private val favoritePostRepository = mockk<OfflineFavoritePostRepository>(relaxed = true)
    private val commentRepository = mockk<CommentRepository>()
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: PostDetailsViewModel

    private val testPostId = "456"
    private val mockSavedStateHandle = SavedStateHandle(
        mapOf("postId" to testPostId)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { commentRepository.getCommentsPost(testPostId) } returns flowOf(
            PagingData.from(listOf(dummyComment("c1")))
        )
        networkMonitor = mockk {
            coEvery { isConnected } returns MutableStateFlow(true)
        }
        viewModel = PostDetailsViewModel(
            postRepository,
            favoritePostRepository,
            networkMonitor,
            commentRepository,
            mockSavedStateHandle,
            testDispatcher
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun dummyComment(
        id: String = "1",
        postId: String = "123",
        name: String = "Test Commenter",
        email: String = "test@example.com",
        body: String = "This is a test comment."
    ) = Comment(id, postId, name, email, body)

//    @Test
//    fun `addPostToFavorites SHOULD call PostRepository when online`() = runTest {
//        val onlineNetworkMonitor = mockk<NetworkMonitor> {
//            coEvery { isConnected } returns MutableStateFlow(true)
//        }
//        val onlineViewModel = PostDetailsViewModel(
//            postRepository, favoritePostRepository, onlineNetworkMonitor,
//            commentRepository, mockSavedStateHandle, testDispatcher
//        )
//
//        onlineViewModel.addPostToFavorites()
//
//        coVerify(exactly = 1) { postRepository.addToFavorite(testPostId) }
//        coVerify(exactly = 0) { favoritePostRepository.addPostToOfflineFavorite(any()) }
//    }
//    @Test
//    fun `addPostToFavorites SHOULD call OfflineFavoritePostRepository when offline`() = runTest {
//        // 1. إعداد الشبكة كـ غير متصلة
//        val offlineNetworkMonitor = mockk<NetworkMonitor> {
//            coEvery { isConnected } returns MutableStateFlow(false)
//        }
//        val offlineViewModel = PostDetailsViewModel(
//            postRepository, favoritePostRepository, offlineNetworkMonitor,
//            commentRepository, mockSavedStateHandle, testDispatcher
//        )
//
//        // 2. تنفيذ الدالة
//        offlineViewModel.addPostToFavorites()
//
//        // 3. التحقق من الاستدعاءات
//        coVerify(exactly = 0) { postRepository.addToFavorite(any()) }
//        coVerify(exactly = 1) { favoritePostRepository.addPostToOfflineFavorite(testPostId) }
//    }
//
//    @Test
//    fun `addPostToFavorites SHOULD emit ShowMessage effect`() = runTest {
//        viewModel.effect.test {
//            viewModel.addPostToFavorites()
//
//            val effect = awaitItem()
//            assertEquals(PostDetailsEffect.ShowMessage("Post added to favorites"), effect)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }



}