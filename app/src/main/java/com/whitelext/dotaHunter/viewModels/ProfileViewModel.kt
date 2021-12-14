package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.model.FavoritePlayer
import com.whitelext.dotaHunter.domain.repository.FavoritesRepository
import com.whitelext.dotaHunter.domain.repository.ProfileRepository
import com.whitelext.dotaHunter.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val favoritesRepository: FavoritesRepository,
    application: Application
) : AndroidViewModel(application) {

    val profileData by lazy { MutableLiveData<UserProfileQuery.Player>() }
    val isFavorite = MutableLiveData(false)
    var loading = false

    private suspend fun performGetProfile(userId: Long) {
        when (val response = profileRepository.getProfile(userId)) {
            is Resource.Success -> {
                profileData.postValue(response.data)
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun checkIsInFavorites() {
        favoritesRepository.getPlayers { onCheckIsInFavoritesResult(it) }
    }

    private fun onCheckIsInFavoritesResult(result: Resource<List<FavoritePlayer>>) {
        when (result) {
            is Resource.Success -> {
                val profileId = Converter.anyToLong(profileData.value?.steamAccount?.id)
                isFavorite.value = result.data.any { it.id == profileId }
            }
            else -> isFavorite.value = false
        }
    }

    private fun onChangeFavoritesResult(success: Boolean) {
        if (success) {
            viewModelScope.launch {
                checkIsInFavorites()
            }
        }
    }

    fun initUser(userId: Long) {
        viewModelScope.launch {
            delay(400L)
            loading = true
            performGetProfile(userId)
            checkIsInFavorites()
        }
    }

    fun changeFavorite() {
        viewModelScope.launch {
            if (isFavorite.value == false) {
                profileData.value?.steamAccount?.let {
                    favoritesRepository.addPlayer(Converter.steamAccountToPlayer(it)) { success ->
                        onChangeFavoritesResult(
                            success
                        )
                    }
                }
            } else {
                Converter.anyToLong(profileData.value?.steamAccount?.id)?.let {
                    favoritesRepository.deletePlayer(it) { success ->
                        onChangeFavoritesResult(
                            success
                        )
                    }
                }
            }
        }
    }
}
