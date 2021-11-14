package com.whitelext.dotaHunter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private var userId = 0L
    val profileData by lazy { MutableLiveData<UserProfileQuery.Player>() }

    private suspend fun performGetProfile() {

        when (val response = profileRepository.getProfile(userId)) {
            is Resource.Success -> {
                profileData.value = response.data
            }
            is Resource.Error -> {
                // TODO: ui notification
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    fun initUser(userId: Long) {
        if (userId != 0L) {
            this.userId = userId
            viewModelScope.launch { performGetProfile() }
        }
    }
}