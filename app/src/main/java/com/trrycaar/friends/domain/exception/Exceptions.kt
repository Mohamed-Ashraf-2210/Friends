package com.trrycaar.friends.domain.exception

open class FriendsException(message: String? = null, override val cause: Throwable? = null) :
    Exception(message)

class NoInternetException(message: String = "No Internet", override val cause: Throwable? = null) :
    FriendsException(message, cause)

class FriendDatabaseException(message: String? = null, override val cause: Throwable? = null) :
    FriendsException(message, cause)

class NotFoundException(message: String? = null, override val cause: Throwable? = null) :
    FriendsException(message, cause)

class UnknownException(message: String? = null, override val cause: Throwable? = null) :
    FriendsException(message, cause)