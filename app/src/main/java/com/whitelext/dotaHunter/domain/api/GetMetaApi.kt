package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.MetaListQuery
import com.example.type.RankBracketHeroTimeDetail
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class GetMetaApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getMeta(bracketId: Int): Resource<MetaListQuery.Data> {

        println(bracketId)
        val rankBracketHero = when (bracketId) {
            1 -> RankBracketHeroTimeDetail.HERALD_GUARDIAN
            2 -> RankBracketHeroTimeDetail.CRUSADER_ARCHON
            3 -> RankBracketHeroTimeDetail.LEGEND_ANCIENT
            else -> RankBracketHeroTimeDetail.DIVINE_IMMORTAL
        }

        val response = try {
            apolloClient.query(MetaListQuery(rankBracketHero.toInput()))
                /*.toBuilder()
                .responseFetcher(ApolloResponseFetchers.NETWORK_ONLY)
                .build()*/
                .await()
        } catch (e: ApolloException) {
            return Resource.Error(
                ResourceError.API_ERROR.apply {
                    message = "Server error"
                }
            )
        }

        val meta = response.data
        return if (meta != null && !response.hasErrors()) {
            println(rankBracketHero)
            println("Success")
            Resource.Success(meta)
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
