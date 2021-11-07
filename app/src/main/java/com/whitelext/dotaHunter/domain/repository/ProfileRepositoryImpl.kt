package com.whitelext.dotaHunter.domain.repository

import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.api.GetProfileApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(private val apiService: GetProfileApi) :
    ProfileRepository {

    override suspend fun getProfile(query: Long): Resource<UserProfileQuery.Player> =
        apiService.getProfile(query)
}