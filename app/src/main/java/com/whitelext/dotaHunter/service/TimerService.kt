package com.whitelext.dotaHunter.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.whitelext.dotaHunter.R
import com.whitelext.dotaHunter.common.TimerType
import com.whitelext.dotaHunter.view.screens.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val tag = "SERVICE"
const val foreground_id = 100

class TimerService : Service() {

    private var aegisTimer: CountDownTimer? = null

    private val jobList = mutableListOf<Job>()

    private fun buildScope(job: Job): CoroutineScope {
        return CoroutineScope(Dispatchers.Default + job)
    }

    private fun getScope(): CoroutineScope {
        val job = Job()
        jobList.add(job)
        return buildScope(job)
    }

    private var timers = mutableListOf<CountDownTimer?>(null, null, null, null, null)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = createNotificationChannel()
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("DotaHunter")
                .setContentText("Timers are running")
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(foreground_id, notification)
        }

        val timerType = (intent?.getSerializableExtra("Type") ?: TimerType.AEGIS) as TimerType
        val id = intent?.getIntExtra("Id", -1) ?: -1
        if (id != -1) {
            when (timerType) {
                TimerType.DELETE -> {
                    deleteTimer(id)
                }
                else -> {
                    someTask(timerType, id)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timers.forEach {
            it?.cancel()
        }
        aegisTimer?.cancel()

        aegisTimer = null
        timers = mutableListOf(null, null, null, null, null)

        jobList.forEach {
            it.cancel()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun deleteTimer(id: Int) {
        when (id) {
            0 -> {
                aegisTimer?.cancel()
                aegisTimer = null
            }
            in 1..5 -> {
                timers[id - 1]?.cancel()
                timers[id - 1] = null
            }
            else -> {
                throw IllegalArgumentException("There are only 6 timers with indices from 0 to 5")
            }
        }
    }

    private fun someTask(timerType: TimerType, id: Int) {
        val timer = object : CountDownTimer(timerType.length * 1000L, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                sendMessageToActivity(
                    id,
                    "seconds remaining: " + millisUntilFinished / 1000,
                    timerType
                )
            }

            override fun onFinish() {
                sendMessageToActivity(id, "finished", timerType)
            }
        }

        if (id != 0 && timers[id - 1] == null) {
            timers[id - 1] = timer
            getScope().launch(Dispatchers.Default) {
                timer.start()
            }
        } else if (aegisTimer == null && id == 0) {
            aegisTimer = timer
            getScope().launch(Dispatchers.Default) {
                timer.start()
            }
        }
    }

    private fun sendMessageToActivity(timerId: Int, time: String, timerType: TimerType) {
        val intent = Intent("TimeRemainingUpdate")
        intent.putExtra("TimerId", timerId.toString())
        intent.putExtra("CurrentTime", time)
        intent.putExtra("Type", timerType)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String = "timer_service",
        channelName: String = tag
    ): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }
}
