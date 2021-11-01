package com.whitelext.dotaHunter.domain.repository

import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource

interface SearchRepository {
    suspend fun searchUsers(query: String): Resource<List<UserListQuery.Player>>
}