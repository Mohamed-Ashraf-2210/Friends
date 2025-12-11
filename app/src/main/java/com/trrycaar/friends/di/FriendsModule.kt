package com.trrycaar.friends.di

import androidx.room.Room
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSourceImpl
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSourceImpl
import com.trrycaar.friends.data.remote.dataSource.CommentRemoteDataSource
import com.trrycaar.friends.data.remote.dataSource.CommentRemoteDataSourceImpl
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSource
import com.trrycaar.friends.data.remote.dataSource.PostRemoteDataSourceImpl
import com.trrycaar.friends.data.repository.CommentRepositoryImpl
import com.trrycaar.friends.data.repository.FavoritePostsRepositoryImpl
import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.constants.Constants.DATABASE_NAME
import com.trrycaar.friends.data.util.network.NetworkMonitor
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.NetworkObserver
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.FavoritePostsRepository
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

val friendsModule = module {
    single<HttpClient> { buildApiClient() }

    single {
        Room.databaseBuilder(androidContext(), FriendsDatabase::class.java, DATABASE_NAME)
            .build()
    }
    single { get<FriendsDatabase>().postDao() }
    single { get<FriendsDatabase>().favoritePostsDao() }
    single { get<FriendsDatabase>().remoteKeysDao() }
    single<PostLocalDataSource> { PostLocalDataSourceImpl(get()) }
    single<FavoritePostsLocalDataSource> { FavoritePostsLocalDataSourceImpl(get()) }
    single<PostRemoteDataSource> { PostRemoteDataSourceImpl(get()) }
    single<CommentRemoteDataSource> { CommentRemoteDataSourceImpl(get()) }

    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single<FavoritePostsRepository> { FavoritePostsRepositoryImpl(get(), get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }

    single<NetworkObserver> { NetworkMonitor(androidContext()) }

    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::HomeViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::FavoritePostsViewModel)
}