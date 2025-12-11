package com.trrycaar.friends.domain

import kotlinx.coroutines.flow.StateFlow

interface NetworkObserver {
    val isConnected: StateFlow<Boolean>
}