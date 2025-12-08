package com.trrycaar.friends.data.util.network

import android.util.Log
import com.trrycaar.friends.data.exception.FriendsDataException
import com.trrycaar.friends.data.exception.NoInternetDataException
import com.trrycaar.friends.data.exception.NotFoundDataException
import com.trrycaar.friends.data.exception.ServerErrorException
import com.trrycaar.friends.data.exception.UnknownApiException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException
import kotlin.coroutines.cancellation.CancellationException

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
        404 -> throw NotFoundDataException()
        in 500..599 -> throw ServerErrorException()
        else -> throw UnknownApiException()
    }
}
fun mapToFriendsDataException(e: Throwable): FriendsDataException = when (e) {
    is FriendsDataException -> e
    is IOException -> NoInternetDataException(e)
    is CancellationException -> throw e
    else -> UnknownApiException(e)
}