package com.trrycaar.friends.presentation.screen.helper

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.yield

suspend fun <T : Any> PagingData<T>.collectForTest(): List<T> {
    val differ = AsyncPagingDataDiffer(
        diffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem == newItem
        },
        updateCallback = NoopListCallback,
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Default
    )

    differ.submitData(this)
    repeat(5) { yield() }
    return differ.snapshot().items
}

private object NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
    override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
    override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
}