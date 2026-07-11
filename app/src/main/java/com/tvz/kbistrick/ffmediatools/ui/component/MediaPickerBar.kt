package com.tvz.kbistrick.ffmediatools.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.loadMediaInfo
import kotlinx.coroutines.launch

@Composable
fun MediaPickerBar(
    media: MediaInfo?,
    processedMediaPath: String?,
    onMediaChange: (MediaInfo?) -> Unit,
    onAllMediaClear: () -> Unit,
    onSaveProcessedMedia: (path: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                scope.launch { onMediaChange(loadMediaInfo(context, uri)) }
            }
        }

    ElevatedCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.S),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Space.S)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    media?.let { it.fileName ?: "Unnamed file" } ?: "No media selected",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (media != null) {
                    Text(
                        mediaDetailsText(media),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (media != null) {
                IconButton({ onAllMediaClear() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear selected media")
                }
            }

            FilledTonalIconButton({
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = "Pick media")
            }

            if (processedMediaPath != null) {
                FloatingActionButton({ onSaveProcessedMedia(processedMediaPath) }) {
                    Icon(Icons.Default.Save, contentDescription = "Save processed media")
                }
            }
        }
    }
}

private fun mediaDetailsText(media: MediaInfo): String {
    val dimensions =
        if (media.width != null && media.height != null) "${media.width} × ${media.height}"
        else null

    val format = media.format.displayText

    return listOfNotNull(dimensions, format).joinToString(" · ")
}

@Preview(showBackground = true)
@Composable
fun MediaPickerBarPreview() {
    val mediaPickerBar = @Composable { mediaInfo: MediaInfo?, processedMediaPath: String? ->
        MediaPickerBar(mediaInfo, processedMediaPath, {}, {}, {})
    }

    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(Space.M),
            modifier = Modifier.padding(Space.M)
        ) {
            mediaPickerBar(null, null)

            mediaPickerBar(
                MediaInfo(
                    Uri.EMPTY,
                    "holiday_video.mp4",
                    "video/mp4",
                    1920,
                    1080,
                    true,
                    MediaFormat.MP4
                ),
                null,
            )

            mediaPickerBar(
                MediaInfo(
                    Uri.EMPTY,
                    "animation.gif",
                    "image/gif",
                    480,
                    270,
                    false,
                    MediaFormat.UNKNOWN_VIDEO
                ),
                null,
            )

            mediaPickerBar(
                MediaInfo(
                    Uri.EMPTY,
                    "animation.gif",
                    "image/gif",
                    480,
                    270,
                    false,
                    MediaFormat.UNKNOWN_VIDEO
                ),
                "something",
            )
        }
    }
}
