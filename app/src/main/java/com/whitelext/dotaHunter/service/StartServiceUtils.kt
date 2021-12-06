package com.whitelext.dotaHunter.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.whitelext.dotaHunter.common.TimerType

fun Context.foregroundStartService(timerType: TimerType, id: Int) {
    val intent = Intent(this, TimerService::class.java)
    intent.putExtra("Type", timerType)
    intent.putExtra("Id", id)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.startForegroundService(intent)
    } else {
        this.startService(intent)
    }
}

fun Context.stopService() {
    stopService(Intent(this, TimerService::class.java))
}
