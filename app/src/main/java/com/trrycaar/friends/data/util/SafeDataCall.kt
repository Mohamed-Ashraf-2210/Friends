package com.trrycaar.friends.data.util

import android.database.CursorIndexOutOfBoundsException
import android.database.StaleDataException
import android.database.sqlite.SQLiteException
import com.trrycaar.friends.data.exception.CorruptDatabaseDataException
import com.trrycaar.friends.data.exception.DiskAccessDataException
import com.trrycaar.friends.data.exception.FriendsDataException
import com.trrycaar.friends.data.exception.InvalidIdDataException
import com.trrycaar.friends.data.exception.UnknownNetworkDataException
import kotlinx.io.IOException
import java.sql.SQLException

suspend fun <T> safeDbCall(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: Exception) {
        throw mapToMediaException(e)
    }
}


private fun mapToMediaException(throwable: Throwable): FriendsDataException = when (throwable) {
    is SQLiteException,
    is SQLException,
    is CursorIndexOutOfBoundsException,
    is StaleDataException -> CorruptDatabaseDataException()

    is IOException -> DiskAccessDataException()
    is IllegalArgumentException,
    is IllegalStateException -> InvalidIdDataException()

    is NullPointerException -> CorruptDatabaseDataException()
    else -> UnknownNetworkDataException()
}