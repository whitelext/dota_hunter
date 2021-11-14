package com.whitelext.dotaHunter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(private val matchRepository: MatchRepository) :
    ViewModel() {

    private var matchId = 0L
    val matchData by lazy { MutableLiveData<MatchStatsQuery.Match>() }

    private suspend fun performGetMatch() {
        when (val response = matchRepository.getMatch(matchId)) {
            is Resource.Success -> {
                matchData.value = response.data
            }
            is Resource.Error -> {
                // TODO: ui notification
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    fun initMatch(matchId: Long) {
        if (matchId != 0L) {
            this.matchId = matchId
            viewModelScope.launch { performGetMatch() }
        }
    }
}