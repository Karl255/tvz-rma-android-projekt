package com.tvz.kbistrick.ffmediatools.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.toAspectRatio

@Composable
fun MediaPreview(previewImagePath: String?, previewedMediaSize: Pair<Int, Int>?, infoText: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            if (previewImagePath != null) {
                val bitmap = remember(previewImagePath) {
                    BitmapFactory.decodeFile(previewImagePath)
                }

                val previewModifier = Modifier
                    .let {
                        if (previewedMediaSize != null) it.aspectRatio(previewedMediaSize.toAspectRatio())
                        else it
                    }
                    .heightIn(max = 300.dp)
                    .fillMaxWidth()

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Media preview",
                        contentScale = ContentScale.Fit,
                        modifier = previewModifier,
                    )
                } else {
                    Box(
                        modifier = previewModifier,
                    ) {
                        Text(
                            "Preview error",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        if (infoText != null) {
            Text(
                infoText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color(0x80000000))
                    .padding(horizontal = Space.S)
            )
        }
    }
}

@Composable
@Preview
fun MediaPreviewPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(Space.M)
        ) {
            MediaPreview("path", Pair(1920, 1080), infoText = "1920x1080")
        }
    }
}