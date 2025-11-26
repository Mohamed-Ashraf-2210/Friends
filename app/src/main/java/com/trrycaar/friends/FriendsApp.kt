package com.trrycaar.friends

import android.app.Application
import com.trrycaar.friends.di.friendsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FriendsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FriendsApp)
            modules(friendsModule)
        }
    }
}