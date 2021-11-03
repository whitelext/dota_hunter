package com.whitelext.dotaHunter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private var getProfileQuery = 0L
    val profileData by lazy { MutableLiveData<UserProfileQuery.Player>() }

    private suspend fun performGetProfile() {

        when (val response = profileRepository.getProfile(getProfileQuery)) {
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

    fun onQueryChanged(newQuery: Long) {
        if (getProfileQuery != newQuery) {
            getProfileQuery = newQuery
            if (getProfileQuery == 0L) {
                return
            }
            Utils.debounceCall(
                coroutineScope = viewModelScope,
                destinationFunction = ::performGetProfile
            ).invoke()
        }
    }
}