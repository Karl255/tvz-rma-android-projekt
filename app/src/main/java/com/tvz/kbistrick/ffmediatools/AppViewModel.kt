package com.tvz.kbistrick.ffmediatools

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tvz.kbistrick.ffmediatools.model.MediaInfo

class AppViewModel : ViewModel() {
    var pickerLimitation by mutableStateOf<VisualMediaType?>(null)
        private set

    var media by mutableStateOf<MediaInfo?>(null)
        private set

    var processedMediaPath by mutableStateOf<String?>(null)
        private set

    var processedMediaSize by mutableStateOf<Pair<Int, Int>?>(null)
        private set

    var processedMediaPreviewPath by mutableStateOf<String?>(null)
        private set

    fun updatePickerLimitation(vidualMediaType: VisualMediaType?) {
        pickerLimitation = vidualMediaType
    }

    fun updateMedia(mediaInfo: MediaInfo?) {
        media = mediaInfo
        Log.d("AppViewModel", "Updated media: $mediaInfo")
    }

    fun updateProcessedMediaPath(path: String?) {
        processedMediaPath = path
        Log.d("AppViewModel", "Updated processedMediaPath: $path")
    }

    fun updateProcessedMediaSize(width: Int, height: Int) {
        processedMediaSize = width to height
        Log.d("AppViewModel", "Updated processedMediaSize: $width x $height")
    }

    fun updateProcessedMediaPreviewPath(path: String?) {
        processedMediaPreviewPath = path
        Log.d("AppViewModel", "Updated processedMediaPreviewPath: $path")
    }

    fun clearAllMedia() {
        media = null
        processedMediaPath = null
        processedMediaSize = null
        processedMediaPreviewPath = null

    }
}
