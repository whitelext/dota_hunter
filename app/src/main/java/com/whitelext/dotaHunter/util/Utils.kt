package com.whitelext.dotaHunter.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

object Utils {

    fun debounceCall(
        waitMs: Long = 300L,
        coroutineScope: CoroutineScope,
        destinationFunction: KSuspendFunction0<Unit>
    ): () -> Unit {
        var debounceJob: Job? = null
        return {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction()
            }
        }
    }

}