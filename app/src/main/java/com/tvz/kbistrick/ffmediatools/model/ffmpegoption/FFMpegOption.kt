package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

import androidx.compose.runtime.Composable

interface FFMpegOption {
    @Composable
    fun Render(onValueChanged: () -> Unit)

    fun isValid(): Boolean = true

    fun toArgs(): List<String>
}
