package com.tvz.kbistrick.ffmediatools

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo

class AppViewModel : ViewModel() {
    var media by mutableStateOf<MediaInfo?>(MediaInfo(width = 1920, height = 1080, format = MediaFormat.MP4)) // remove mocks
        private set

    fun updateMedia(media: MediaInfo?) {
        this.media = media
    }
}
