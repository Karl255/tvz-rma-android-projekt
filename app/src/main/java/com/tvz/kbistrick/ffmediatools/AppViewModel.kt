package com.tvz.kbistrick.ffmediatools

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tvz.kbistrick.ffmediatools.model.MediaInfo

class AppViewModel : ViewModel() {
    var media by mutableStateOf<MediaInfo?>(null)
        private set

    var processedMediaPath by mutableStateOf<String?>(null)
        private set

    fun updateMedia(mediaInfo: MediaInfo?) {
        media = mediaInfo
        Log.d("AppViewModel", "Updated media: $mediaInfo")
    }

    fun updateProcessedMediaPath(path: String?) {
        processedMediaPath = path
        Log.d("AppViewModel", "Updated processedMediaPath: $path")
    }

    fun clearAllMedia() {
        updateMedia(null)
        updateProcessedMediaPath(null)
    }
}
