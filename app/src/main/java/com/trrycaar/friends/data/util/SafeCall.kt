package com.trrycaar.friends.data.util

import com.trrycaar.friends.data.exception.CorruptDatabaseException
import com.trrycaar.friends.data.exception.InternalProgrammingException
import com.trrycaar.friends.data.exception.InvalidIdException
import com.trrycaar.friends.data.exception.NoInternetDataException
import com.trrycaar.friends.data.exception.NotFoundDataException
import com.trrycaar.friends.data.exception.ServerErrorException
import com.trrycaar.friends.data.exception.UnknownApiException
import com.trrycaar.friends.data.exception.UnknownDatabaseException
import com.trrycaar.friends.domain.exception.FriendDatabaseException
import com.trrycaar.friends.domain.exception.FriendsException
import com.trrycaar.friends.domain.exception.NoInternetException
import com.trrycaar.friends.domain.exception.NotFoundException
import com.trrycaar.friends.domain.exception.UnknownException
import io.ktor.util.network.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeCall(execute: suspend () -> T): T {
    return try {
        execute()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw mapToDomainException(e)
    }
}

fun mapToDomainException(e: Throwable): FriendsException = when (e) {
    is UnresolvedAddressException,
    is NoInternetDataException -> NoInternetException("No Internet", e)

    is InternalProgrammingException,
    is UnknownApiException -> UnknownException(e.message, e)

    is NotFoundDataException -> NotFoundException(e.message, e)
    is CorruptDatabaseException,
    is UnknownDatabaseException -> FriendDatabaseException()

    is InvalidIdException ->
        UnknownException(e.message, e)

    is ServerErrorException ->
        UnknownException(e.message, e)

    else -> UnknownException(e.message, e)
}