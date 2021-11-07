package com.whitelext.dotaHunter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.SearchRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

    private var searchQuery = ""
    private val _usersLiveData by lazy { MutableLiveData<List<UserListQuery.Player>>() }
    val usersLiveData: LiveData<List<UserListQuery.Player>> = _usersLiveData

    fun onQueryChanged(newQuery: String) {
        if (searchQuery != newQuery) {
            searchQuery = newQuery
            if (searchQuery.isEmpty()) {
                // TODO: put favourites users in usersLiveData
                _usersLiveData.value = emptyList()
                return
            }
            Utils.debounceCall(
                coroutineScope = viewModelScope,
                destinationFunction = ::performSearchUsers
            ).invoke()
        }
    }

    private suspend fun performSearchUsers() {

        when (val response = searchRepository.searchUsers(searchQuery)) {
            is Resource.Success -> {
                _usersLiveData.value = response.data
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