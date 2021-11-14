package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class GetMatchApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getMatch(matchId: Long): Resource<MatchStatsQuery.Match> {

        val response = try {
            apolloClient.query(MatchStatsQuery(matchId)).await()
        } catch (e: ApolloException) {
            null
        }

        val match = response?.data?.match

        return if (match != null && !response.hasErrors()) {
            Resource.Success(match)
        } else {
            Resource.Error(response?.errors?.let {
                ResourceError.API_ERROR.apply {
                    message = it.first().message
                }
            } ?: ResourceError.UNKNOWN)
        }
    }
}
