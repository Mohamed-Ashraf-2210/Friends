package com.trrycaar.friends.data.util

import com.trrycaar.friends.data.mapper.mapToFriendsDataException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeDbCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw mapToFriendsDataException(e)
    }
}