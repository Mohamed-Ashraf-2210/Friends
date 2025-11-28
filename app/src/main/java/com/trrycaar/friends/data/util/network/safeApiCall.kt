package com.trrycaar.friends.data.util.network

import android.util.Log
import com.trrycaar.friends.domain.exception.FriendNetworkException
import com.trrycaar.friends.domain.exception.FriendsException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> HttpResponse
): T {
    val response = runCatching { block() }
        .getOrElse { e ->
            Log.e("SAFE_API_CALL", "Network error: ${e.message}")
            when (e) {
                is CancellationException -> throw e
                is UnresolvedAddressException -> throw FriendNetworkException(e.message.orEmpty())
                else -> throw FriendsException(e.message.orEmpty())
            }
        }
    return handleResponse(response)
}

suspend inline fun <reified T> handleResponse(response: HttpResponse): T {
    return when (response.status.value) {
        in 200..299 -> response.body<T>()
        else -> throw FriendsException("Network request failed with status code: ${response.status.value}")
    }
}