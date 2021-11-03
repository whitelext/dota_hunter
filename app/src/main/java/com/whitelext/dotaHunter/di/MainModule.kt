package com.whitelext.dotaHunter.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import com.whitelext.dotaHunter.domain.repository.ProfileRepositoryImpl
import com.whitelext.dotaHunter.domain.repository.SearchRepository
import com.whitelext.dotaHunter.domain.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideSearchRepository(repositoryImp: SearchRepositoryImpl): SearchRepository {
        return repositoryImp
    }

    @Provides
    fun provideProfileRepository(repositoryImp: ProfileRepositoryImpl): ProfileRepository {
        return repositoryImp
    }

    @Provides
    fun provideNormalizedCache(): LruNormalizedCacheFactory {
        return LruNormalizedCacheFactory(
            EvictionPolicy.builder().maxSizeBytes(10 * 1024 * 1024L).build()
        )
    }

    @Provides
    fun provideSqlNormalizedCache(@ApplicationContext context: Context): SqlNormalizedCacheFactory {
        return SqlNormalizedCacheFactory(context, "apollo.db")
    }

    @Provides
    fun provideApolloClient(
        sqlNormalizedCache: SqlNormalizedCacheFactory
    ): ApolloClient {
        return ApolloClient
            .builder()
            .normalizedCache(sqlNormalizedCache)
            .defaultResponseFetcher(ApolloResponseFetchers.CACHE_AND_NETWORK)
            .serverUrl("https://api.stratz.com/graphql")
            .build()
    }

}