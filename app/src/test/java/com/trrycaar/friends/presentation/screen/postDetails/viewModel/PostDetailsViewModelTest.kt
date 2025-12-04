package com.trrycaar.friends.presentation.screen.postDetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.domain.entity.Comment
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.OfflineFavoritePostRepository
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
        networkMonitor = mockk {
            coEvery { isConnected } returns MutableStateFlow(true)
        }
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

    private fun createViewModelWithComments(comments: List<Comment> = listOf(dummyComment())) {
        coEvery { commentRepository.getCommentsPost(any()) } returns flowOf(PagingData.from(comments))
        viewModel = PostDetailsViewModel(
            postRepository,
            favoritePostRepository,
            networkMonitor,
            commentRepository,
            mockSavedStateHandle,
            testDispatcher
        )
    }

    @Test
    fun `commentsPaging SHOULD emit empty PagingData initially`() = runTest {
        createViewModelWithComments(emptyList())
        viewModel.commentsPaging.test {
            val pagingData = awaitItem()
            assertEquals(0, pagingData.collectForTest().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `commentsPaging SHOULD return success`() = runTest {
        createViewModelWithComments()
        advanceUntilIdle()
        viewModel.commentsPaging.test {
            skipItems(1)
            val pagingData = awaitItem()

            val items = pagingData.collectForTest()

            assertEquals(1, items.size)
            assertEquals("1", items[0].id)
            assertEquals("Test Commenter", items[0].name)
        }
    }
}