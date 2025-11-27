package com.trrycaar.friends.di

import androidx.room.Room
import com.trrycaar.friends.data.local.FriendsDatabase
import com.trrycaar.friends.data.local.dataSource.FavoritePostsLocalDataSource
import com.trrycaar.friends.data.local.dataSource.PostLocalDataSource
import com.trrycaar.friends.data.repository.CommentRepositoryImpl
import com.trrycaar.friends.data.repository.FavoritePostRepositoryImpl
import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.constants.Constants.DATABASE_NAME
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.repository.CommentRepository
import com.trrycaar.friends.domain.repository.FavoritePostRepository
import com.trrycaar.friends.domain.repository.PostRepository
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
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FriendsDatabase>().postDao() }
    single { get<FriendsDatabase>().favoritePostsDao() }
    single { PostLocalDataSource(get()) }
    single { FavoritePostsLocalDataSource(get()) }

    single<PostRepository> { PostRepositoryImpl(get(), get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<FavoritePostRepository> { FavoritePostRepositoryImpl(get(), get()) }

    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::HomeViewModel)
    viewModelOf(::PostDetailsViewModel)
}