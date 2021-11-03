package com.whitelext.dotaHunter.di

import com.apollographql.apollo.ApolloClient
import com.whitelext.dotaHunter.domain.repository.SearchRepository
import com.whitelext.dotaHunter.domain.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideSearchRepository(repositoryImp: SearchRepositoryImpl): SearchRepository {
        return repositoryImp
    }

    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient
            .builder()
            .serverUrl("https://api.stratz.com/graphql")
            .build()
    }

}