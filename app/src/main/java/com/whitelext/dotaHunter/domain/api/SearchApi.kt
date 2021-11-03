package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class SearchApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getUsers(userNameQuery: String): Resource<List<UserListQuery.Player>> {
        val response = try {
            apolloClient.query(UserListQuery(userNameQuery)).await()
        } catch (e: ApolloException) {
            null
        }

        val users = response?.data?.stratz?.search?.players?.filterNotNull()

        return if (users != null && !response.hasErrors()) {
            Resource.Success(users)
        } else {
            Resource.Error(response?.errors?.let {
                ResourceError.API_ERROR.apply {
                    message = it.first().message
                }
            } ?: ResourceError.UNKNOWN)
        }

    }
}