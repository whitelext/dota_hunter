package com.whitelext.dotaHunter.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.whitelext.dotaHunter.domain.AppDatabase
import com.whitelext.dotaHunter.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideSearchRepository(repositoryImpl: SearchRepositoryImpl): SearchRepository {
        return repositoryImpl
    }

    @Provides
    fun provideProfileRepository(repositoryImpl: ProfileRepositoryImpl): ProfileRepository {
        return repositoryImpl
    }

    @Provides
    fun provideItemsRepository(repositoryImpl: ItemsRepositoryImpl): ItemsRepository {
        return repositoryImpl
    }

    @Provides
    fun provideMatchRepository(repositoryImpl: MatchRepositoryImpl): MatchRepository {
        return repositoryImpl
    }

    @Provides
    fun provideFavoritesRepository(repositoryImpl: FavoritesRepositoryImpl): FavoritesRepository {
        return repositoryImpl
    }

    @Provides
    fun provideMetaRepository(repositoryImpl: MetaRepositoryImpl): MetaRepository {
        return repositoryImpl
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
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor()).build()
            )
            .build()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getAppDataBase(context)
    }

}
