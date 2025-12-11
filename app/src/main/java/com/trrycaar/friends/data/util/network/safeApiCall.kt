package com.trrycaar.friends.data.util.network

import android.util.Log
import com.trrycaar.friends.data.exception.NotFoundDataException
import com.trrycaar.friends.data.exception.ServerErrorException
import com.trrycaar.friends.data.exception.UnknownApiException
import com.trrycaar.friends.data.mapper.mapToFriendsDataException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeApiCall(
    block: suspend () -> HttpResponse
): T {
    val response = try {
        block()
    } catch (e: Exception) {
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