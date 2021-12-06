package com.whitelext.dotaHunter

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.FavoritesRepository
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import com.whitelext.dotaHunter.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val favoritesRepository: FavoritesRepository,
    application: Application
) :
    AndroidViewModel(application) {

    val profileData by lazy { MutableLiveData<UserProfileQuery.Player>() }
    val isFavorite = MutableLiveData(false)

    private suspend fun performGetProfile(userId: Long) {
        when (val response = profileRepository.getProfile(userId)) {
            is Resource.Success -> {
                profileData.value = response.data
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    private suspend fun checkIsInFavorites() {
        when (val favorites = favoritesRepository.getPlayers()) {
            is Resource.Success -> isFavorite.value = favorites.data.any { it.id == profileData.value?.steamAccount?.id }
            else -> isFavorite.value = false
        }
    }

    fun initUser(userId: Long) {
        viewModelScope.launch {
            performGetProfile(userId)
            checkIsInFavorites()
        }
    }

    fun changeFavorite() {
        viewModelScope.launch {
            if (isFavorite.value == false) {
                profileData.value?.steamAccount?.let {
                    favoritesRepository.addPlayer(Converter.steamAccountToPlayer(it))
                    //isFavorite.value = true
                }
            } else {
                Converter.anyToId(profileData.value?.steamAccount?.id)?.let {
                    favoritesRepository.deletePlayer(it)
                    // isFavorite.value = false
                }
            }
            checkIsInFavorites()
        }
    }
}
