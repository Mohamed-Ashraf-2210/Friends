package com.trrycaar.friends.data.util.network

import com.trrycaar.friends.domain.exception.FriendsException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> handleResponse(response: HttpResponse): T {
    return when (response.status.value) {
        in 200..299 -> response.body<T>()
        else -> throw FriendsException("Network request failed with status code: ${response.status.value}")
    }
}