package com.trrycaar.friends.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E>(
    initialState: S,
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _state = MutableStateFlow<S>(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    fun updateState(updater: S.() -> S) {
        _state.update(updater)
    }

    fun emitEffect(effect: E) {
        viewModelScope.launch(
            context = defaultDispatcher
        ) {
            _effect.emit(effect)
        }
    }

    private fun createExceptionHandler(onError: (Throwable) -> Unit) =
        CoroutineExceptionHandler { _, throwable ->
            onError(throwable)
        }

    fun <S> tryToExecute(
        onStart: () -> Unit = {},
        block: suspend () -> S,
        onSuccess: (S) -> Unit = {},
        onError: (exception: Throwable) -> Unit = {},
        dispatcher: CoroutineDispatcher = defaultDispatcher
    ) {
        onStart()
        val handler = createExceptionHandler(onError)
        viewModelScope.launch(dispatcher + handler) {
            try {
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}