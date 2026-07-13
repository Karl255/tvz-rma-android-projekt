package com.tvz.kbistrick.ffmediatools.model

enum class MediaFormat(
    val displayText: String,
    val fileExtension: String?,
    val isVideo: Boolean,
    val optionInits: List<() -> FFMpegOption>
) {
    PNG("PNG", "png", false, emptyList()),
    JPG("JPG", "jpg", false, listOf(JpegQualityOption::init)),
    WEBP("WEBP", "webp", false, emptyList()),
    MP4("MP4", "mp4", true, emptyList()),
    MKV("MKV", "mkv", true, emptyList()),
    WEBM("WEBM", "webm", true, emptyList()),
    UNKNOWN_IMAGE("Unknown image", null, false, emptyList()),
    UNKNOWN_VIDEO("Unknown video", null, true, emptyList());

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
