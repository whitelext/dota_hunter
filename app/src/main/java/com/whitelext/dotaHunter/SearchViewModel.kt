package com.whitelext.dotaHunter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.SearchRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    application: Application
) : AndroidViewModel(application) {

    private var searchQuery = ""
    val usersLiveData by lazy { MutableLiveData<List<UserListQuery.Player>>() }


    fun onQueryChanged(newQuery: String) {
        if (searchQuery != newQuery) {
            searchQuery = newQuery
            Utils.debounceCall(
                coroutineScope = viewModelScope,
                destinationFunction = ::performSearchUsers
            ).invoke()
        }
    }

    private suspend fun performSearchUsers() {
            val response = searchRepository.searchUsers(searchQuery)

            when (response) {
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

    override fun onCleared() {
        super.onCleared()
        // TODO: in the final make sure nothing needs to be cleaned up
    }


}