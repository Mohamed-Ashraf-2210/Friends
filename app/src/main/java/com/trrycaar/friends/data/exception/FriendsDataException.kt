package com.trrycaar.friends.data.exception

open class FriendsDataException : Exception()

class NoInternetDataException(override val cause: Throwable? = null) : FriendsDataException()
class InvalidIdException(override val cause: Throwable? = null) : FriendsDataException()
class CorruptDatabaseException(override val cause: Throwable? = null) : FriendsDataException()
class InternalProgrammingException(override val cause: Throwable? = null) : FriendsDataException()
class UnknownDatabaseException(override val cause: Throwable? = null) : FriendsDataException()
class UnknownApiException(override val cause: Throwable? = null) : FriendsDataException()
class ServerErrorException(override val cause: Throwable? = null) : FriendsDataException()
class NotFoundDataException(override val cause: Throwable? = null) : FriendsDataException()