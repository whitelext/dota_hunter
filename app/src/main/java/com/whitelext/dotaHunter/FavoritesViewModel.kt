package com.whitelext.dotaHunter

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.model.FavoritePlayer
import com.whitelext.dotaHunter.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    application: Application
) : AndroidViewModel(application) {

    val favoriteList by lazy { MutableLiveData<List<FavoritePlayer>>() }

    fun initialize() {
        Log.d("QWERT", "initialize")
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            when (val response = favoritesRepository.getPlayers()) {
                is Resource.Success -> {
                    favoriteList.value = response.data
                }
                is Resource.Error -> {
                    Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(getApplication(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
