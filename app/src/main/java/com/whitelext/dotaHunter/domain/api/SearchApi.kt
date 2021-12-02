package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class SearchApi @Inject constructor(private val apolloClient: ApolloClient) {

    private fun UserListQuery.ProPlayer.toTypical(): UserListQuery.Player {
        val (__typename, id, avatar, name, seasonRank, lastMatchDateTime) = this
        return UserListQuery.Player(
            __typename,
            id,
            avatar,
            name,
            seasonRank,
            lastMatchDateTime
        )
    }

    suspend fun getUsers(userNameQuery: String): Resource<List<UserListQuery.Player>> {
        val response = try {
            apolloClient.query(UserListQuery(userNameQuery)).await()
        } catch (e: ApolloException) {
            return Resource.Error(
                ResourceError.API_ERROR.apply {
                    message = "Server error"
                }
            )
        }

        val users = response?.data?.stratz?.search?.players?.filterNotNull()?.toMutableList()
        val proUsers =
            response?.data?.stratz?.search?.proPlayers?.mapNotNull { it?.toTypical() }

        val concat = users?.let { if (proUsers != null) it.apply { addAll(proUsers) } else it }

        return if (concat != null && !response.hasErrors()) {
            Resource.Success(concat)
        } else {
            Resource.Error(
                response?.errors?.let {
                    ResourceError.API_ERROR.apply {
                        message = it.first().message
                    }
                } ?: ResourceError.NO_INTERNET
            )
        }
    }
}
