package com.trrycaar.friends.domain.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val message: String, val cachedData: T? = null) : Result<T>()
    object Loading : Result<Nothing>()
}