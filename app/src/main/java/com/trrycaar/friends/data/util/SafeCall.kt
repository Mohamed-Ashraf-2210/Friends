package com.trrycaar.friends.data.util

import com.trrycaar.friends.data.exception.CorruptDatabaseDataException
import com.trrycaar.friends.data.exception.DiskAccessDataException
import com.trrycaar.friends.domain.exception.AccessDeniedException
import com.trrycaar.friends.domain.exception.FriendsException
import com.trrycaar.friends.domain.exception.NoInternetDataException
import com.trrycaar.friends.domain.exception.UnknownException
import io.ktor.util.network.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeCall(execute: suspend () -> T): T {
    return try {
        execute()
    } catch (e: Exception) {
        throw mapToDomainException(e)
    }
}

fun mapToDomainException(e: Throwable): FriendsException = when (e) {
    is CancellationException -> UnknownException(e.message)
    is UnresolvedAddressException -> NoInternetDataException(e.message.orEmpty())
    is CorruptDatabaseDataException,
    is DiskAccessDataException -> AccessDeniedException()

    else -> UnknownException(e.message)
}