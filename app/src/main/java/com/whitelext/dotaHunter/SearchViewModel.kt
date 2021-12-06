package com.whitelext.dotaHunter

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
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

    val userInput by lazy { MutableLiveData("") }
    private val _usersLiveData by lazy { MutableLiveData<List<UserListQuery.Player>>() }
    val usersLiveData: LiveData<List<UserListQuery.Player>> = _usersLiveData

    fun clearInput() {
        userInput.value = ""
    }

    fun onQueryChanged() {
        if (userInput.value.isNullOrEmpty()) {
            _usersLiveData.value = emptyList()
            return
        }
        Utils.asyncCall(
            coroutineScope = viewModelScope,
            destinationFunction = ::performSearchUsers
        ).invoke()
    }

    private suspend fun performSearchUsers() {

        when (val response = searchRepository.searchUsers(userInput.value ?: "")) {
            is Resource.Success -> {
                _usersLiveData.value =
                    response.data.filter { it.lastMatchDateTime != null }.sortedByDescending {
                        it.seasonRank.toString().toLongOrNull()
                    }
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
                Toast.makeText(getApplication(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
