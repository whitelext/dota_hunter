package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.MetaRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class MetaViewModel @Inject constructor(
    private val metaRepository: MetaRepository,
    application: Application
) : AndroidViewModel(application) {

    init {
        getMeta()
    }

    // Sorting states
    private val PICK_RATE_BY_DESCENDING = 0L
    private val PICK_RATE_BY_ASCENDING = 1L
    private val WIN_RATE_BY_DESCENDING = 2L
    private val WIN_RATE_BY_ASCENDING = 3L

    // Changing flags
    val CHANGE_FROM_PICK = 0L
    val CHANGE_FROM_WIN = 1L

    // Comparators
    private val pickRateDescendingComparator =
        compareByDescending<HeroInfo> { it.picks }
    private val pickRateAscendingComparator =
        compareBy<HeroInfo> { it.picks }
    private val winRateDescendingComparator =
        compareByDescending<HeroInfo> { (it.wins.toDouble() / it.picks.toDouble()) }
    private val winRateAscendingComparator =
        compareBy<HeroInfo> { (it.wins.toDouble() / it.picks.toDouble()) }

    var sortingState = PICK_RATE_BY_DESCENDING

    // id -> (shortName, displayName)
    private val _heroMap = ConcurrentHashMap<String, HeroNames>()
    val heroMap
        get() = _heroMap.toMap()

    private val _metaList by lazy { MutableLiveData(mutableListOf<HeroInfo>()) }
    val metaList by lazy { MutableLiveData(listOf<HeroInfo>()) }

    var picksSum = 0L

    private fun sortMeta() {
        metaList.postValue(
            when (sortingState) {
                PICK_RATE_BY_DESCENDING -> _metaList.value?.sortedWith(pickRateDescendingComparator)
                PICK_RATE_BY_ASCENDING -> _metaList.value?.sortedWith(pickRateAscendingComparator)
                WIN_RATE_BY_DESCENDING -> _metaList.value?.sortedWith(winRateDescendingComparator)
                WIN_RATE_BY_ASCENDING -> _metaList.value?.sortedWith(winRateAscendingComparator)
                else -> _metaList.value ?: listOf()
            }
        )
    }

    fun changeState(changeFlag: Long) {
        sortingState = when (sortingState to changeFlag) {
            (PICK_RATE_BY_DESCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_ASCENDING
            (PICK_RATE_BY_ASCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_DESCENDING
            (WIN_RATE_BY_DESCENDING to CHANGE_FROM_PICK), (WIN_RATE_BY_ASCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_DESCENDING
            (PICK_RATE_BY_DESCENDING to CHANGE_FROM_WIN), (PICK_RATE_BY_ASCENDING to CHANGE_FROM_WIN) -> WIN_RATE_BY_DESCENDING
            (WIN_RATE_BY_ASCENDING to CHANGE_FROM_WIN) -> WIN_RATE_BY_DESCENDING
            (WIN_RATE_BY_DESCENDING to CHANGE_FROM_WIN) -> WIN_RATE_BY_ASCENDING
            else -> PICK_RATE_BY_DESCENDING
        }
        sortMeta()
    }

    private fun getMeta() {
        Utils.asyncCall(
            coroutineScope = viewModelScope,
            destinationFunction = ::performGetMeta
        ).invoke()
    }

    private suspend fun performGetMeta() {
        when (val response = metaRepository.getMeta()) {
            is Resource.Success -> {
                withContext(Dispatchers.Default) {
                    response.data.constants?.heroes?.forEach { hero ->
                        _heroMap[hero?.id.toString()] =
                            HeroNames(
                                hero?.shortName ?: "",
                                hero?.displayName ?: ""
                            )
                    }

                    val tempList = mutableListOf<HeroInfo>()
                    response.data.heroStats?.metaTrend?.forEach { hero ->
                        hero?.heroId?.let { id ->
                            val heroPicks = hero.pick?.sumOf { (it as BigDecimal).toLong() } ?: 0L
                            val heroWins = hero.win?.sumOf { (it as BigDecimal).toLong() } ?: 0L
                            picksSum += heroPicks
                            tempList.add(
                                HeroInfo(
                                    id = id,
                                    picks = heroPicks,
                                    wins = heroWins
                                )
                            )
                        }
                    }
                    _metaList.postValue(tempList)
                }
                sortMeta()
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // TODO: same ui notification
            }
        }
    }

    data class HeroNames(
        val shortName: String,
        val displayName: String
    )

    data class HeroInfo(
        val id: Int,
        val picks: Long,
        val wins: Long
    )
}
