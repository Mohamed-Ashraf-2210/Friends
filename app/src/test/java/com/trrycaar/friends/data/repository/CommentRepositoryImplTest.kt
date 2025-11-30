package com.trrycaar.friends.data.repository

import androidx.paging.testing.asSnapshot
import com.trrycaar.friends.data.remote.dataSource.CommentRemoteDataSource
import com.trrycaar.friends.data.remote.dto.comments.CommentDto
import com.trrycaar.friends.data.remote.dto.comments.CommentsDto
import com.trrycaar.friends.data.remote.dto.comments.Pagination
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentRepositoryImplTest {
    private lateinit var repository: CommentRepositoryImpl
    private val remoteDataSource: CommentRemoteDataSource = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = CommentRepositoryImpl(remoteDataSource)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCommentsPost returns filtered comments by postId`() = runTest {
        val postId = "1"



        coEvery { remoteDataSource.getComments(1, 10) } returns CommentsDto(
            commentDto = fakeComments(),
            pagination = Pagination(
                page = 1,
                total = 20,
                limit = 10,
                hasNext = true,
                hasPrev = false,
                totalPages = 2
            )
        )

        coEvery { remoteDataSource.getComments(2, 10) } returns CommentsDto(
            commentDto = emptyList(),
            pagination = Pagination(
                page = 2,
                total = 20,
                limit = 10,
                hasNext = false,
                hasPrev = true,
                totalPages = 2
            )
        )


        val pagingData = repository.getCommentsPost(postId).asSnapshot { scrollTo(10) }

        assertEquals(2, pagingData.size)
        assertEquals("1", pagingData[0].postId)
    }

    @Test
    fun `getCommentsPost returns empty list`() = runTest {
        val postId = "4"

        coEvery { remoteDataSource.getComments(1, 10) } returns CommentsDto(
            commentDto = fakeComments(),
            pagination = Pagination(
                page = 1,
                total = 20,
                limit = 10,
                hasNext = true,
                hasPrev = false,
                totalPages = 2
            )
        )

        coEvery { remoteDataSource.getComments(2, 10) } returns CommentsDto(
            commentDto = emptyList(),
            pagination = Pagination(
                page = 2,
                total = 20,
                limit = 10,
                hasNext = false,
                hasPrev = true,
                totalPages = 2
            )
        )


        val pagingData = repository.getCommentsPost(postId).asSnapshot { scrollTo(10) }

        assertEquals(0, pagingData.size)
    }

    private fun fakeComments() = listOf(
        CommentDto(
            id = 1,
            postId = 1,
            name = "Mohamed",
            email = "m@gmail.com",
            body = "Body 1"
        ),
        CommentDto(id = 2, postId = 1, name = "Ali", email = "a@gmail.com", body = "Body 2"),
        CommentDto(id = 3, postId = 3, name = "Sara", email = "s@gmail.com", body = "Body 3")
    )
}