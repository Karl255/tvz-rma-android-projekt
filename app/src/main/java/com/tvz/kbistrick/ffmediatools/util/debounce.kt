package com.tvz.kbistrick.ffmediatools.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun debounced(
    ms: Long,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    func: () -> Unit,
): () -> Unit {
    return remember {
        var job: Job? = null

        {
            job?.cancel()

            job = coroutineScope.launch {
                delay(ms.milliseconds)
                func()
            }
        }
    }
}