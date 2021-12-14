package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.repository.MetaRepository
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

@HiltViewModel
class MetaViewModel @Inject constructor(
    private val metaRepository: MetaRepository,
    application: Application
) : AndroidViewModel(application) {

    init {
        getMeta()
    }

    companion object {

        const val CHANGE_FROM_PICK = 0L
        const val CHANGE_FROM_WIN = 1L

        private const val PICK_RATE_BY_DESCENDING = 0L
        private const val PICK_RATE_BY_ASCENDING = 1L
        private const val WIN_RATE_BY_DESCENDING = 2L
        private const val WIN_RATE_BY_ASCENDING = 3L

        private val pickRateDescendingComparator =
            compareByDescending<HeroInfo> { it.picks }
        private val pickRateAscendingComparator =
            compareBy<HeroInfo> { it.picks }
        private val winRateDescendingComparator =
            compareByDescending<HeroInfo> { it.wins }
        private val winRateAscendingComparator =
            compareBy<HeroInfo> { it.wins }
    }

    var sortingState = PICK_RATE_BY_DESCENDING

    private var bracketId = 1

    private val _heroMap = ConcurrentHashMap<String, HeroNames>()
    val heroMap
        get() = _heroMap.toMap()

    private val _metaList by lazy { MutableLiveData(mutableListOf<HeroInfo>()) }
    val metaList by lazy { MutableLiveData(listOf<HeroInfo>()) }

    var picksSum = AtomicLong(0)

    private fun sortMeta() {
        metaList.postValue(
            when (sortingState) {
                PICK_RATE_BY_DESCENDING -> _metaList.value?.sortedWith(
                    pickRateDescendingComparator
                )
                PICK_RATE_BY_ASCENDING -> _metaList.value?.sortedWith(
                    pickRateAscendingComparator
                )
                WIN_RATE_BY_DESCENDING -> _metaList.value?.sortedWith(
                    winRateDescendingComparator
                )
                WIN_RATE_BY_ASCENDING -> _metaList.value?.sortedWith(winRateAscendingComparator)
                else -> _metaList.value ?: listOf()
            }
        )
    }

    fun changeState(changeFlag: Long) {
        sortingState = when (sortingState to changeFlag) {
            (PICK_RATE_BY_DESCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_ASCENDING
            (PICK_RATE_BY_ASCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_DESCENDING
            (WIN_RATE_BY_DESCENDING to CHANGE_FROM_PICK),
            (WIN_RATE_BY_ASCENDING to CHANGE_FROM_PICK) -> PICK_RATE_BY_DESCENDING
            (PICK_RATE_BY_DESCENDING to CHANGE_FROM_WIN),
            (PICK_RATE_BY_ASCENDING to CHANGE_FROM_WIN) -> WIN_RATE_BY_DESCENDING
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

    fun onBracketChange(value: Int) {
        if (bracketId != value) {
            bracketId = value
            println("Value changed to $value")
            getMeta()
        }
    }

    private suspend fun performGetMeta() {
        when (val response = metaRepository.getMeta(bracketId)) {
            is Resource.Success -> {
                withContext(Dispatchers.Default) {
                    response.data.constants?.heroes?.forEach { hero ->
                        _heroMap[hero?.id.toString()] =
                            HeroNames(
                                hero?.shortName ?: "",
                                hero?.displayName ?: ""
                            )
                    }
                    delay(0)
                    var threadLocalSum = 0L
                    val tempList = mutableListOf<HeroInfo>()
                    response.data.heroStats?.stats?.forEach { hero ->
                        delay(0)
                        hero?.heroId?.let { id ->
                            val heroPicks: Long =
                                ((hero.events?.get(0)?.matchCount ?: 0L) as BigDecimal).toLong()
                            val heroWins: Double =
                                ((hero.events?.get(0)?.wins ?: 0.0) as BigDecimal).toDouble()
                            threadLocalSum += heroPicks
                            tempList.add(
                                HeroInfo(
                                    id = (id as BigDecimal).toInt(),
                                    picks = heroPicks,
                                    wins = (heroWins * 100).toLong()
                                )
                            )
                        }
                    }
                    delay(0)
                    picksSum = AtomicLong(threadLocalSum)
                    synchronized(_metaList) {
                        _metaList.postValue(tempList)
                    }
                }
                withContext(Dispatchers.Main) {
                    sortMeta()
                }
            }
            is Resource.Error -> {
                Toast.makeText(getApplication(), response.error.message, Toast.LENGTH_SHORT).show()
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
