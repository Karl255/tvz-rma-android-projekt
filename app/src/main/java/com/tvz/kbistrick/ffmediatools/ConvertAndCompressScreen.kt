package com.tvz.kbistrick.ffmediatools

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.FFMpegOption
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import com.tvz.kbistrick.ffmediatools.ui.component.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.component.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space

val IMAGE_FORMATS = listOf(
    MediaFormat.PNG,
    MediaFormat.JPG,
    MediaFormat.WEBP,
)

val VIDEO_FORMATS = listOf(
    MediaFormat.MP4,
    MediaFormat.MKV,
    MediaFormat.WEBM,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertAndCompressScreen(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    //val context = LocalContext.current
    //val scope = rememberCoroutineScope()

    var shouldAutoPreview by remember { mutableStateOf(appViewModel.media?.isVideo?.not() ?: true) }

    var formatDropdownExpanded by remember { mutableStateOf(false) }
    var availableFormats by remember { mutableStateOf(getFormatsList(appViewModel.media?.isVideo)) }
    var selectedFormat by remember { mutableStateOf<MediaFormat?>(null) }

    var ffmpegOptions by remember { mutableStateOf<List<FFMpegOption>>(emptyList()) }

    val hasMedia = appViewModel.media != null

    LaunchedEffect(appViewModel.media) {
        val media = appViewModel.media

        if (media != null) {
            shouldAutoPreview = !media.isVideo
        }
    }

    LaunchedEffect(appViewModel.media?.isVideo) {
        availableFormats = getFormatsList(appViewModel.media?.isVideo)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(Space.M),
        modifier = modifier
            .fillMaxSize()
            .padding(Space.M)
    ) {
        Text(
            "Convert and compress screen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )

        MediaPreview(
            previewImagePath = appViewModel.processedMediaPreviewPath,
            previewedMediaSize = appViewModel.processedMediaSize,
        )

        AutoPreviewOption(shouldAutoPreview, { shouldAutoPreview = it }, enabled = hasMedia)

        if (availableFormats.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = formatDropdownExpanded,
                onExpandedChange = { formatDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedFormat?.displayText ?: "",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Ouput format") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(formatDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = hasMedia
                )

                ExposedDropdownMenu(
                    expanded = formatDropdownExpanded,
                    onDismissRequest = { formatDropdownExpanded = false }
                ) {
                    availableFormats.forEach { format ->
                        DropdownMenuItem(
                            text = { Text(format.displayText) },
                            onClick = {
                                selectedFormat = format
                                ffmpegOptions = format.optionInits.map { it() }
                                formatDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        ffmpegOptions.forEach { ffmpegOption ->
            ffmpegOption.Render()
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ConvertAndCompressScreenPreview() {
    val appViewModel = AppViewModel().apply {
        updateMedia(
            MediaInfo(
                uri = Uri.EMPTY,
                fileName = "sample.jpg",
                mimeType = "image/jpeg",
                width = 1920,
                height = 1080,
                rotation = null,
                isVideo = false,
                format = MediaFormat.JPG
            )
        )
    }
    
    AppTheme {
        AppScaffold(appViewModel) { innerPadding ->
            ConvertAndCompressScreen(AppViewModel(), Modifier.padding(innerPadding))
        }
    }
}

fun getFormatsList(isVideo: Boolean?) = when (isVideo) {
    true -> VIDEO_FORMATS
    false -> IMAGE_FORMATS
    null -> emptyList()
}
