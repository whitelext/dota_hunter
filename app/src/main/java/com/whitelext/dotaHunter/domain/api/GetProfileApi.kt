package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fetcher.ApolloResponseFetchers
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetProfileApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getProfile(steamAccountIdQuery: Long): Resource.Success<UserProfileQuery.Player> {

        val response = try {
            throw ApolloException("null")
            /*apolloClient.query(UserProfileQuery(steamAccountIdQuery)).responseFetcher(
                ApolloResponseFetchers.NETWORK_FIRST
            ).await()*/
        } catch (e: ApolloException) {
            apolloClient.apolloStore.read(UserProfileQuery(steamAccountIdQuery)).execute()
        }

        println(response)

        /*val player = response.data?.player

        return if (player != null && !response.hasErrors()) {
            Resource.Success(player)
        } else {
            Resource.Error(response?.errors?.let { SearchApiError(it.first()) } ?: ResourceError())
        }*/
        return Resource.Success(response.player!!)

    }
}