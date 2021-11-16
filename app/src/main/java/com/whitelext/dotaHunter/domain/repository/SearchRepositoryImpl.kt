package com.whitelext.dotaHunter.domain.repository

import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.api.SearchApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(private val apiService: SearchApi) :
    SearchRepository {

    override suspend fun searchUsers(query: String): Resource<List<UserListQuery.Player>> {
        return apiService.getUsers(query)
    }
}
