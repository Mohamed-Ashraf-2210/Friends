package com.trrycaar.friends.data.util.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BasePagingSource<T : Any>(
    private val getDataFromApi: suspend (Int, pageSize: Int) -> List<T>,
    private val getDataFromDataBase: (suspend (Int, pageSize: Int) -> List<T>)? = null,
    private val saveDataToDataBase: (suspend (List<T>) -> Unit)? = null,
    private val onError: (Throwable) -> Unit = {},
    private val pageSize: Int = 10
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: 1
        return try {
            val data: List<T> = withContext(Dispatchers.IO) {
                try {
                    getDataFromApi(position, pageSize).also { apiData ->
                        if (apiData.isNotEmpty()) {
                            saveDataToDataBase?.invoke(apiData)
                        }
                    }.ifEmpty {
                        getDataFromDataBase?.invoke(position, pageSize) ?: emptyList()
                    }
                } catch (e: Exception) {
                    getDataFromDataBase?.invoke(position, pageSize) ?: throw e
                }
            }
            LoadResult.Page(
                data = data,
                prevKey = if (position == 1) null else position,
                nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            onError(e)
            LoadResult.Error(e)
        }
    }
}