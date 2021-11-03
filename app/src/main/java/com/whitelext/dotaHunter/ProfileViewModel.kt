package com.whitelext.dotaHunter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private var searchQuery = 0L
    val usersLiveData by lazy { MutableLiveData<UserProfileQuery.Player>() }

    private suspend fun performGetProfile() {

        when (val response = profileRepository.getProfile(searchQuery)) {
            is Resource.Success -> {
                usersLiveData.value = response.data
            }
            is Resource.Error -> {
                // TODO: ui notification
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

}