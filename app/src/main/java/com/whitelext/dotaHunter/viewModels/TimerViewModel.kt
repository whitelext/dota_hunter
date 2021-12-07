package com.whitelext.dotaHunter.viewModels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.whitelext.dotaHunter.common.TimerType
import com.whitelext.dotaHunter.service.foregroundStartService
import com.whitelext.dotaHunter.service.stopService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            val timerId = intent.getStringExtra("TimerId")?.toInt()
            val remainingTime = intent.getStringExtra("CurrentTime")
            val timerType = intent.getSerializableExtra("Type") as TimerType

            if (remainingTime != null && timerId != null) {
                when (timerType) {
                    TimerType.AEGIS -> {
                        aegisTimer.value = remainingTime
                    }
                    else -> {
                        timerCountdown.value?.let {
                            val tmp = it.toMutableMap()
                            tmp[timerId] = remainingTime
                            timerCountdown.postValue(tmp)
                        } ?: run {
                            timerCountdown.value = mutableMapOf(1 to remainingTime)
                        }
                    }
                }
            }
        }
    }
    val timerCountdown = MutableLiveData<MutableMap<Int, String>>(mutableMapOf())
    val aegisTimer = MutableLiveData<String>()

    init {
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(
            mMessageReceiver, IntentFilter("TimeRemainingUpdate")
        )
    }

    fun startTimer(timerType: TimerType, id: Int) {
        getApplication<Application>().applicationContext.foregroundStartService(timerType, id)
    }

    fun stopTimer(id: Int) {
        if (amountOfActive() <= 1) {
            stopTimers()
        } else {
            getApplication<Application>().applicationContext.foregroundStartService(
                TimerType.DELETE,
                id
            )
            when (id) {
                0 -> aegisTimer.value = null
                in 1..5 -> timerCountdown.value?.remove(id)
            }
        }
    }

    fun stopTimers() {
        getApplication<Application>().applicationContext.stopService()
        timerCountdown.value = mutableMapOf()
        aegisTimer.value = null
    }

    private fun amountOfActive(): Int {
        var x = if (aegisTimer.value == null) 0 else 1
        x += timerCountdown.value?.values?.filterNotNull()?.size ?: 0
        return x
    }

    override fun onCleared() {
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(mMessageReceiver)
        super.onCleared()
    }
}
