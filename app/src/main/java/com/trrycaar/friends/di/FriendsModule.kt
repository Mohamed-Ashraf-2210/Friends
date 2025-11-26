package com.trrycaar.friends.di

import com.trrycaar.friends.data.repository.PostRepositoryImpl
import com.trrycaar.friends.data.util.network.buildApiClient
import com.trrycaar.friends.domain.repository.PostRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val friendsModule = module {
    single<HttpClient> { buildApiClient() }

    single<PostRepository> { PostRepositoryImpl(get()) }
}