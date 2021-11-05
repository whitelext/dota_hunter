package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.ItemsListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class GetItemsApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getItems(): Resource<List<ItemsListQuery.Item>> {

        val response = try {
            apolloClient.query(ItemsListQuery()).await()
        } catch (e: ApolloException) {
            null
        }

        val constants = response?.data?.constants?.items?.filterNotNull()

        return if (constants != null && !response.hasErrors()) {
            Resource.Success(constants)
        } else {
            Resource.Error(response?.errors?.let {
                ResourceError.API_ERROR.apply {
                    message = it.first().message
                }
            } ?: ResourceError.UNKNOWN)
        }
    }
}