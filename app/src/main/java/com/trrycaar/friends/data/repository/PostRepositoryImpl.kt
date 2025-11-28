package com.trrycaar.friends.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.mapper.toDomain
import com.trrycaar.friends.data.mapper.toEntity
import com.trrycaar.friends.data.remote.dto.posts.PostsDto
import com.trrycaar.friends.data.util.constants.EndPoints.BASE_URL
import com.trrycaar.friends.data.util.constants.EndPoints.POSTS
import com.trrycaar.friends.data.util.network.safeApiCall
import com.trrycaar.friends.domain.entity.Post
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.base.BasePagingSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(
    private val client: HttpClient,
    private val postLocalDataSource: PostLocalDataSource
) : PostRepository {
    override fun getPostsPaging(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BasePagingSource(
                    pageSize = 30,
                    onError = { throw it },
                    getDataFromApi = { page, pageSize ->
                        val response: PostsDto = safeApiCall {
                            client.get(BASE_URL + POSTS) {
                                parameter("page", page)
                                parameter("limit", pageSize)
                            }
                        }
                        response.posts.map { it.toDomain() }
                    },
                    getDataFromDataBase = { page, pageSize ->
                        postLocalDataSource.getPosts(page, pageSize).map { it.toDomain() }
                    },
                    saveDataToDataBase = { posts ->
                        //postLocalDataSource.clearPosts()
                        postLocalDataSource.savePosts(posts.map { it.toEntity() })
                    }
                )
            }
        ).flow
    }
}