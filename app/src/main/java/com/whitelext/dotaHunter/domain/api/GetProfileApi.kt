package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class GetProfileApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getProfile(steamAccountIdQuery: Long): Resource<UserProfileQuery.Player> {

        val response = try {
            apolloClient.query(UserProfileQuery(steamAccountIdQuery)).await()
        } catch (e: ApolloException) {
            return Resource.Error(
                ResourceError.API_ERROR.apply {
                    message = "Server error"
                }
            )
        }

        val player = response.data?.player

        return if (player != null && !response.hasErrors()) {
            Resource.Success(player)
        } else {
            Resource.Error(
                response.errors?.let {
                    ResourceError.API_ERROR.apply {
                        message = it.first().message
                    }
                } ?: ResourceError.UNKNOWN
            )
        }
    }
}
