package com.tvz.kbistrick.ffmediatools

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import com.tvz.kbistrick.ffmediatools.model.NullableDimensionValue
import com.tvz.kbistrick.ffmediatools.service.FFMpegService
import com.tvz.kbistrick.ffmediatools.ui.component.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.component.DimensionInputField
import com.tvz.kbistrick.ffmediatools.ui.component.ErrorDialog
import com.tvz.kbistrick.ffmediatools.ui.component.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.debounced
import com.tvz.kbistrick.ffmediatools.util.toggleUnit

@Composable
fun CropVideoScreen(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /*
    var shouldAutoPreview by remember {
        mutableStateOf(
            appViewModel.media?.isVideo?.not() ?: true
        )
    }
    */

    var linkOffsets by remember { mutableStateOf(false) }
    var top by remember { mutableStateOf(NullableDimensionValue(0, DimensionUnit.PERCENT)) }
    var bottom by remember { mutableStateOf(NullableDimensionValue(0, DimensionUnit.PERCENT)) }
    var left by remember { mutableStateOf(NullableDimensionValue(0, DimensionUnit.PERCENT)) }
    var right by remember { mutableStateOf(NullableDimensionValue(0, DimensionUnit.PERCENT)) }
    var error by remember { mutableStateOf<String?>(null) }

    val hasMedia = appViewModel.media != null

    val runTool = debounced(ms = 1000, coroutineScope = scope) {
        val media = appViewModel.media ?: return@debounced
        val top = top.coalesce() ?: return@debounced
        val bottom = bottom.coalesce() ?: return@debounced
        val left = left.coalesce() ?: return@debounced
        val right = right.coalesce() ?: return@debounced

        try {
            FFMpegService.runCropVideoJob(
                context = context,
                mediaInfo = media,
                top = top,
                bottom = bottom,
                left = left,
                right = right
            )
        } catch (e: Exception) {
            Log.e("CropVideoScreen", e.message, e)
            error = e.message
        }
    }

    DisposableEffect(Unit) {
        appViewModel.updatePickerLimitation(ActivityResultContracts.PickVisualMedia.VideoOnly)

        onDispose {
            appViewModel.updatePickerLimitation(null)
        }
    }

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

        MediaPreview(
            previewImagePath = appViewModel.processedMediaPreviewPath,
            previewedMediaSize = appViewModel.processedMediaSize,
        )

        //AutoPreviewOption(shouldAutoPreview, { shouldAutoPreview = it }, enabled = hasMedia)

        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.S),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                DimensionInputField(
                    value = top,
                    onValueChange = {
                        if (linkOffsets) {
                            if (top.unit != it.unit) {
                                linkOffsets = false
                            } else if (it.unit == DimensionUnit.PERCENT) {
                                bottom = it
                                left = it
                                right = it
                            }
                        }

                        top = it
                    },
                    minValue = 0,
                    pixelsAt100Percent = appViewModel.media?.height,
                    label = "Top",
                    enabled = hasMedia
                )

                DimensionInputField(
                    value = left,
                    onValueChange = { left = it },
                    minValue = 0,
                    pixelsAt100Percent = appViewModel.media?.width,
                    label = "Left",
                    enabled = hasMedia && !linkOffsets,
                )
            }

            FilledIconToggleButton(
                linkOffsets,
                {
                    linkOffsets = it
                    if (linkOffsets) {
                        if (top.unit == DimensionUnit.PIXEL) {
                            top = top.toggleUnit(appViewModel.media?.width)
                        }

                        bottom = top
                        left = top
                        right = top
                    }
                },
                enabled = hasMedia
            ) {
                Icon(Icons.Default.Link, contentDescription = "Link dimensions")
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                DimensionInputField(
                    value = bottom,
                    onValueChange = { bottom = it },
                    minValue = 0,
                    pixelsAt100Percent = appViewModel.media?.height,
                    label = "Bottom",
                    enabled = hasMedia && !linkOffsets,
                )

                DimensionInputField(
                    value = right,
                    onValueChange = { right = it },
                    minValue = 0,
                    pixelsAt100Percent = appViewModel.media?.width,
                    label = "Right",
                    enabled = hasMedia && !linkOffsets,
                )
            }
        }

        if (hasMedia) {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Start process") },
                text = { Text("Preview") },
                onClick = runTool,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

    error?.let {
        ErrorDialog(it, { error = null })
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun CropVideoScreenPreview() {
    val appViewModel = AppViewModel().apply {
        updateMedia(
            MediaInfo(
                uri = Uri.EMPTY,
                fileName = "sample_video.mp4",
                mimeType = "video/mp4",
                width = 1920,
                height = 1080,
                rotation = null,
                isVideo = true,
                format = MediaFormat.MP4
            )
        )
    }
    AppTheme {
        AppScaffold(appViewModel) { innerPadding ->
            CropVideoScreen(AppViewModel(), Modifier.padding(innerPadding))
        }
    }
}
