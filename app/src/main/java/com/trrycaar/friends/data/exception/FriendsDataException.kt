package com.trrycaar.friends.data.exception

open class FriendsDataException : Exception()

class NoInternetDataException : FriendsDataException()
class InvalidIdDataException : FriendsDataException()
class CorruptDatabaseDataException : FriendsDataException()
class DiskAccessDataException : FriendsDataException()
class UnknownNetworkDataException : FriendsDataException()