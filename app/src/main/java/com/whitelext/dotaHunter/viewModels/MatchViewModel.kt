package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.MatchRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    application: Application
) : AndroidViewModel(application) {

    val matchData by lazy { MutableLiveData<MatchStatsQuery.Match>() }
    var loading = false

    private suspend fun performGetMatch(matchId: Long) {
        when (val response = matchRepository.getMatch(matchId)) {
            is Resource.Success -> {
                loading = true
                matchData.postValue(response.data)
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    fun initMatch(matchId: Long) {
        Utils.asyncCall(
            waitMs = 400L,
            coroutineScope = viewModelScope,
            destinationFunction = ::performGetMatch,
            argument = matchId
        ).invoke()
    }
}
