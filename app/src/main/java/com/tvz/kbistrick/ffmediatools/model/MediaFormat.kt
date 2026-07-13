package com.tvz.kbistrick.ffmediatools.model

import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.FFMpegOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.JpegQualityOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.WebpQualityOption

enum class MediaFormat(
    val displayText: String,
    val fileExtension: String,
    val isVideo: Boolean,
    val optionInits: List<() -> FFMpegOption>
) {
    PNG("PNG", "png", false, emptyList()),
    JPG("JPG", "jpg", false, listOf(JpegQualityOption::init)),
    WEBP("WEBP", "webp", false, listOf(WebpQualityOption::init)),
    MP4("MP4", "mp4", true, emptyList()),
    MKV("MKV", "mkv", true, emptyList()),
    WEBM("WEBM", "webm", true, emptyList());

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
