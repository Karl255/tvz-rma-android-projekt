package com.tvz.kbistrick.ffmediatools

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tvz.kbistrick.ffmediatools.model.MediaInfo

class AppViewModel : ViewModel() {
    var media by mutableStateOf<MediaInfo?>(null)
        private set

    fun updateMedia(media: MediaInfo?) {
        this.media = media
    }
}
