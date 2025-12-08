package com.trrycaar.friends.data.util

import android.database.CursorIndexOutOfBoundsException
import android.database.StaleDataException
import android.database.sqlite.SQLiteException
import com.trrycaar.friends.data.exception.CorruptDatabaseException
import com.trrycaar.friends.data.exception.DiskAccessException
import com.trrycaar.friends.data.exception.FriendsDataException
import com.trrycaar.friends.data.exception.InternalProgrammingException
import com.trrycaar.friends.data.exception.InvalidIdException
import com.trrycaar.friends.data.exception.UnknownDatabaseException
import kotlinx.io.IOException
import java.sql.SQLException
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


private fun mapToFriendsDataException(throwable: Throwable): FriendsDataException =
    when (throwable) {
        is SQLiteException,
        is SQLException,
        is CursorIndexOutOfBoundsException,
        is StaleDataException -> CorruptDatabaseException(throwable)

        is IOException -> DiskAccessException(throwable)
        is IllegalArgumentException,
        is IllegalStateException -> InvalidIdException(throwable)

        is NullPointerException -> InternalProgrammingException(throwable)
        else -> UnknownDatabaseException()
    }