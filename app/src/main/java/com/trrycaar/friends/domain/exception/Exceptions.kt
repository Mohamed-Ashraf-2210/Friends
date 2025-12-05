package com.trrycaar.friends.domain.exception

open class FriendsException(message: String? = null) : Exception(message)

class NoInternetDataException(message: String? = null) : FriendsException(message)
class FriendDatabaseException(message: String? = null) : FriendsException(message)

class NotFoundException(message: String? = null) : FriendsException(message)
class UnknownException(message: String? = null) : FriendsException(message)
class AccessDeniedException(message: String? = null) : FriendsException(message)