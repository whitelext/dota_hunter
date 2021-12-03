package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    application: Application
) : AndroidViewModel(application) {

    val profileData by lazy { MutableLiveData<UserProfileQuery.Player>() }

    private suspend fun performGetProfile(userId: Long) {
        when (val response = profileRepository.getProfile(userId)) {
            is Resource.Success -> {
                profileData.postValue(response.data)
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    fun initUser(userId: Long) {
        Utils.asyncCall(
            coroutineScope = viewModelScope,
            destinationFunction = ::performGetProfile,
            argument = userId
        ).invoke()
    }
}
