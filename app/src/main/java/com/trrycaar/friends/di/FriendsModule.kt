package com.trrycaar.friends.di

import androidx.room.Room
import com.trrycaar.friends.core.network.NetworkMonitor
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.OfflineFavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.repository.CommentRepositoryImpl
import com.trrycaar.friends.data.repository.FavoritePostRepositoryImpl
import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.constants.Constants.DATABASE_NAME
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.screen.favoritePosts.viewModel.FavoritePostsViewModel
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeViewModel
import com.trrycaar.friends.presentation.screen.postDetails.viewModel.PostDetailsViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.onClose

val friendsModule = module {
    single<HttpClient> { buildApiClient() }

    single {
        Room.databaseBuilder(androidContext(), FriendsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FriendsDatabase>().postDao() }
    single { get<FriendsDatabase>().favoritePostsDao() }
    single { get<FriendsDatabase>().offlineFavoritePostDao() }
    single { PostLocalDataSource(get()) }
    single { FavoritePostsLocalDataSource(get()) }
    single { OfflineFavoritePostsLocalDataSource(get()) }

    single<PostRepository> { PostRepositoryImpl(get(), get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<FavoritePostRepository> { FavoritePostRepositoryImpl(get(), get(), get()) }

    single { NetworkMonitor(get(), get()) } onClose { it?.unregister() }

    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::HomeViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::FavoritePostsViewModel)
}