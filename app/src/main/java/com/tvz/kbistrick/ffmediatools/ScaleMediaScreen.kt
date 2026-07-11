package com.tvz.kbistrick.ffmediatools

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import com.tvz.kbistrick.ffmediatools.service.FFMpegService
import com.tvz.kbistrick.ffmediatools.ui.component.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.component.DimensionInputField
import com.tvz.kbistrick.ffmediatools.ui.component.ErrorDialog
import com.tvz.kbistrick.ffmediatools.ui.component.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.toggleUnit
import kotlinx.coroutines.launch

@Composable
fun ScaleMediaScreen(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var shouldAutoPreview by remember { mutableStateOf(true) }
    var linkDimensions by remember { mutableStateOf(true) }
    var width by remember { mutableStateOf(DimensionValue(100, DimensionUnit.PERCENT)) }
    var height by remember { mutableStateOf(DimensionValue(100, DimensionUnit.PERCENT)) }
    var error by remember { mutableStateOf<String?>(null) }

    val hasMedia = appViewModel.media != null
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(Space.M),
        modifier = modifier
            .fillMaxSize()
            .padding(Space.M)
    ) {
        Text(
            "Scale media screen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )

        MediaPreview()

        AutoPreviewOption(shouldAutoPreview, { shouldAutoPreview = it }, enabled = hasMedia)

        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.S),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DimensionInputField(
                value = width,
                onValueChange = {
                    if (linkDimensions) {
                        if (width.unit != it.unit) {
                            linkDimensions = false
                        } else if (it.unit == DimensionUnit.PERCENT) {
                            height = it
                        }
                    }

                    width = it
                },
                pixelsAt100Percent = appViewModel.media?.width,
                label = "Width",
                enabled = hasMedia,
                modifier = Modifier.weight(1f)
            )

            FilledIconToggleButton(
                linkDimensions,
                {
                    linkDimensions = it
                    if (linkDimensions) {
                        if (width.unit == DimensionUnit.PIXEL) {
                            width = width.toggleUnit(appViewModel.media?.width)
                        }

                        height = width
                    }
                },
                enabled = hasMedia
            ) {
                Icon(Icons.Default.Link, contentDescription = "Link dimensions")
            }

            DimensionInputField(
                value = height,
                onValueChange = { height = it },
                pixelsAt100Percent = appViewModel.media?.height,
                label = "Height",
                enabled = hasMedia && !linkDimensions,
                modifier = Modifier.weight(1f)
            )
        }

        if (hasMedia) {
            FloatingActionButton({
                val media = appViewModel.media ?: return@FloatingActionButton
                appViewModel.updateProcessedMediaPath(null)

                scope.launch {
                    try {
                        FFMpegService.runScalingJob(context, media, width, height)
                    } catch (e: Exception) {
                        Log.e("ScaleMediaScreen", e.message, e)
                        error = e.message
                    }
                }
            }) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Start process")
            }
        }

        error?.let {
            ErrorDialog(it, { error = null })
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ScaleMediaScreenPreview() {
    val appViewModel = AppViewModel().apply {
        updateMedia(
            MediaInfo(
                uri = Uri.EMPTY,
                fileName = "sample_video.mp4",
                mimeType = "video/mp4",
                width = 1920,
                height = 1080,
                isVideo = true,
                format = MediaFormat.MP4
            )
        )

        updateProcessedMediaPath("foo")
    }


    AppTheme {
        AppScaffold(appViewModel) { innerPadding ->
            ScaleMediaScreen(appViewModel, Modifier.padding(innerPadding))
        }
    }
}
