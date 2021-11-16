package com.whitelext.dotaHunter

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    application: Application
) :
    AndroidViewModel(application) {

    val matchData by lazy { MutableLiveData<MatchStatsQuery.Match>() }

    private suspend fun performGetMatch(matchId: Long) {
        when (val response = matchRepository.getMatch(matchId)) {
            is Resource.Success -> {
                matchData.value = response.data
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
        viewModelScope.launch { performGetMatch(matchId) }
    }
}
