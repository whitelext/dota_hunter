package com.whitelext.dotaHunter.domain.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.MetaListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.common.ResourceError
import javax.inject.Inject

class GetMetaApi @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getMeta(): Resource<MetaListQuery.Data> {

        val response = try {
            apolloClient.query(MetaListQuery()).await()
        } catch (e: ApolloException) {
            return Resource.Error(
                ResourceError.API_ERROR.apply {
                    message = "Server error"
                }
            )
        }

        val meta = response.data

        return if (meta != null && !response.hasErrors()) {
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
