package com.whitelext.dotaHunter.domain.repository

import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource

interface ProfileRepository {
    suspend fun getProfile(query: Long): Resource<UserProfileQuery.Player>
}
