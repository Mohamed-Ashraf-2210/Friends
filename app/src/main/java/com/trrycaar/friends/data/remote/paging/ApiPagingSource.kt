package com.trrycaar.friends.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ApiPagingSource<T : Any>(
    private val getDataFromApi: suspend (Int, pageSize: Int) -> List<T>,
    private val pageSize: Int = 10
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val data = getDataFromApi(page, pageSize)
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}