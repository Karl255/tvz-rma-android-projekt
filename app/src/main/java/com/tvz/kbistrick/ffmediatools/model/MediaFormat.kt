package com.tvz.kbistrick.ffmediatools.model

enum class MediaFormat(val displayText: String, val fileExtension: String?, val isVideo: Boolean) {
    PNG("PNG", "png", false),
    JPG("JPG", "jpg", false),
    WEBP("WEBP", "webp", false),
    MP4("MP4", "mp4", true),
    MKV("MKV", "mkv", true),
    WEBM("WEBM", "webm", true),
    UNKNOWN_IMAGE("Unknown image", null, false),
    UNKNOWN_VIDEO("Unknown video", null, false);

    companion object {
        fun fromMimeType(mimeType: String?, isVideo: Boolean): MediaFormat = when (mimeType) {
            "image/png" -> PNG
            "image/jpeg" -> JPG
            "image/webp" -> WEBP
            "video/mp4" -> MP4
            "video/x-matroska" -> MKV
            "video/webm" -> WEBM
            else -> if (isVideo) UNKNOWN_VIDEO else UNKNOWN_IMAGE
        }
    }
}
