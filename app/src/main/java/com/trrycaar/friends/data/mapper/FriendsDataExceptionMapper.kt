package com.trrycaar.friends.data.mapper

import android.database.CursorIndexOutOfBoundsException
import android.database.StaleDataException
import android.database.sqlite.SQLiteException
import com.trrycaar.friends.data.exception.CorruptDatabaseException
import com.trrycaar.friends.data.exception.FriendsDataException
import com.trrycaar.friends.data.exception.InternalProgrammingException
import com.trrycaar.friends.data.exception.InvalidIdException
import com.trrycaar.friends.data.exception.NoInternetDataException
import com.trrycaar.friends.data.exception.UnknownApiException
import kotlinx.io.IOException
import java.sql.SQLException

fun mapToFriendsDataException(throwable: Throwable): FriendsDataException = when (throwable) {
    is FriendsDataException -> throwable

    is SQLiteException,
    is SQLException,
    is CursorIndexOutOfBoundsException,
    is StaleDataException -> CorruptDatabaseException(throwable)

    is IllegalArgumentException,
    is IllegalStateException -> InvalidIdException(throwable)

    is NullPointerException -> InternalProgrammingException(throwable)
    is IOException -> NoInternetDataException(throwable)
    else -> UnknownApiException(throwable)
}