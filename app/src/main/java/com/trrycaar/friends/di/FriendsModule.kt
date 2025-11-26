package com.trrycaar.friends.di

import androidx.room.Room
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.constants.Constants.DATABASE_NAME
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val friendsModule = module {
    single<HttpClient> { buildApiClient() }

    single {
        Room.databaseBuilder(androidContext(), FriendsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FriendsDatabase>().postDao() }
    single { PostLocalDataSource(get()) }

    single<PostRepository> { PostRepositoryImpl(get(), get()) }

    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::HomeViewModel)
}