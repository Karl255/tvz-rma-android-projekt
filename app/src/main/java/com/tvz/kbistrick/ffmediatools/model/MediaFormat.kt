package com.tvz.kbistrick.ffmediatools.model

import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.FFMpegOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.JpegQualityOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.WebpQualityOption

enum class MediaFormat(
    val displayText: String,
    val fileExtension: String,
    val isVideo: Boolean,
    val optionInits: List<() -> FFMpegOption> = emptyList(),
) {
    PNG("PNG", "png", false),
    JPG("JPG", "jpg", false, listOf(JpegQualityOption::init)),
    WEBP("WEBP", "webp", false, listOf(WebpQualityOption::init)),
    MP4("MP4", "mp4", true),
    MKV("MKV", "mkv", true),
    WEBM("WEBM", "webm", true);

    companion object {
        fun fromMimeType(mimeType: String?): MediaFormat? = when (mimeType) {
            "image/png" -> PNG
            "image/jpeg" -> JPG
            "image/webp" -> WEBP
            "video/mp4" -> MP4
            "video/x-matroska" -> MKV
            "video/webm" -> WEBM
            else -> null
        }
    }
}
