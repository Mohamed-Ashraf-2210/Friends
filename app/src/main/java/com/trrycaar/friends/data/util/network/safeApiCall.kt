package com.trrycaar.friends.data.util.network

import android.util.Log
import com.trrycaar.friends.data.exception.FriendsDataException
import com.trrycaar.friends.data.exception.NoInternetDataException
import com.trrycaar.friends.data.exception.UnknownNetworkDataException
import com.trrycaar.friends.domain.exception.UnknownException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException

suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> HttpResponse
): T {
    val response = runCatching { block() }
        .getOrElse { e ->
            Log.e("SAFE_API_CALL", "Network error: ${e.message}")
            throw mapToFriendsDataException(e)
        }
    return handleResponse(response)
}

suspend inline fun <reified T> handleResponse(response: HttpResponse): T {
    return when (response.status.value) {
        in 200..299 -> response.body<T>()
        else -> throw UnknownException("Network request failed with status code: ${response.status.value}")
    }
}

fun mapToFriendsDataException(e: Throwable): FriendsDataException = when (e) {
    is FriendsDataException -> e
    is IOException -> NoInternetDataException()
    else -> UnknownNetworkDataException()
}