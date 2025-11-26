package com.trrycaar.friends.domain.exception

open class FriendsException(message: String? = null) : Exception(message)

class FriendNetworkException(message: String? = null) : FriendsException(message)
class FriendDatabaseException(message: String? = null) : FriendsException(message)