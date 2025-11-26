package com.trrycaar.friends.di

import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.repository.PostRepository
import com.trrycaar.friends.presentation.screen.home.viewModle.HomeViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val friendsModule = module {
    single<HttpClient> { buildApiClient() }

    single<PostRepository> { PostRepositoryImpl(get()) }

    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::HomeViewModel)
}