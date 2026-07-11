package com.tvz.kbistrick.ffmediatools

import androidx.compose.runtime.staticCompositionLocalOf
import com.mzgs.ffmpegx.FFmpeg

val LocalMainActivity = staticCompositionLocalOf<MainActivity> {
    error("No MainActivity instance provided")
}
