package com.tvz.kbistrick.ffmediatools.model

enum class MediaFormat(val displayText: String, val fileExtension: String, val isVideo: Boolean) {
    PNG("PNG", "png", false),
    JPG("JPG", "jpg", false),
    WEBP("WEBP", "webp", false),
    MP4("MP4", "mp4", true),
    MKV("MKV", "mkv", true),
    WEBM("WEBM", "webm", true)
}
