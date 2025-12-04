package com.trrycaar.friends.presentation.screen.favoritePosts.viewModel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.screen.helper.collectForTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class FavoritePostsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val postRepository = mockk<PostRepository>()
    private lateinit var viewModel: FavoritePostsViewModel

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
        isFavorite: Boolean = true
    ) = Post(id, title, body, isFavorite)

    private fun createViewModelWithPosts(posts: List<Post> = listOf(dummyPost())) {
        coEvery { postRepository.getFavoritePostsPaging() } returns flowOf(
            PagingData.from(posts)
        )
        viewModel = FavoritePostsViewModel(postRepository, testDispatcher)
    }

    @Test
    fun `onPostClicked SHOULD emit navigation effect with correct postId`() = runTest {
        createViewModelWithPosts()

        viewModel.effect.test {
            viewModel.onPostClicked("123")

            val effect = awaitItem()
            assertEquals(
                FavoritePostsEffects.NavigateToPostDetails("123"),
                effect
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `favoritePostsPaging SHOULD emit empty PagingData initially`() = runTest {
        createViewModelWithPosts(emptyList())
        viewModel.favoritePostsPaging.test {
            val pagingData = awaitItem()
            assertEquals(0, pagingData.collectForTest().size)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `favoritePostsPaging SHOULD return success`() = runTest {
        createViewModelWithPosts()
        viewModel.favoritePostsPaging.test {
            skipItems(1)
            val pagingData = awaitItem()

            val items = pagingData.collectForTest()

            assertEquals(1, items.size)
        }
    }

    @Test
    fun `toUiState SHOULD convert Post to PostUiState correctly`() = runTest {
        val post = Post(
            id = "456",
            title = "Favorite Title",
            body = "Favorite Body",
            isFavorite = true
        )

        val uiState = post.toUiState()

        assertEquals("456", uiState.id)
        assertEquals("Favorite Title", uiState.title)
        assertEquals("Favorite Body", uiState.body)
    }

    @Test
    fun `initial state SHOULD be FavoritePostsUiState with default values`() = runTest {
        createViewModelWithPosts()

        val initialState = viewModel.state.value

        // Verify initial state is as expected
        assertEquals(FavoritePostsUiState(), initialState)
    }

    @Test
    fun `multiple onPostClicked calls SHOULD emit multiple navigation effects`() = runTest {
        createViewModelWithPosts()

        viewModel.effect.test {
            viewModel.onPostClicked("1")
            assertEquals(
                FavoritePostsEffects.NavigateToPostDetails("1"),
                awaitItem()
            )

            viewModel.onPostClicked("2")
            assertEquals(
                FavoritePostsEffects.NavigateToPostDetails("2"),
                awaitItem()
            )

            viewModel.onPostClicked("3")
            assertEquals(
                FavoritePostsEffects.NavigateToPostDetails("3"),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}