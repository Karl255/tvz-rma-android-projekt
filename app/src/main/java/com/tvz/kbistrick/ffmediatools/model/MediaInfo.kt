package com.tvz.kbistrick.ffmediatools.model

import android.net.Uri

data class MediaInfo(
    val uri: Uri,
    val fileName: String,
    val mimeType: String?,
    val width: Int?,
    val height: Int?,
    val rotation: Int?,
    val isVideo: Boolean,
    val format: MediaFormat,
)
