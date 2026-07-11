package com.tvz.kbistrick.ffmediatools

import androidx.compose.runtime.staticCompositionLocalOf
import com.mzgs.ffmpegx.FFmpeg

/** Provided once near the root of the composition, in [MainActivity]. */
val LocalFFmpeg = staticCompositionLocalOf<FFmpeg> {
    error("No FFmpeg instance provided")
}
