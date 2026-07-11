package com.tvz.kbistrick.ffmediatools

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.ui.components.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.components.DimensionInputField
import com.tvz.kbistrick.ffmediatools.ui.components.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.utils.toggleUnit

@Composable
fun CropVideoScreen(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    var shouldAutoPreview by remember {
        mutableStateOf(
            appViewModel.media?.format?.isVideo?.not() ?: true
        )
    }
    var linkOffsets by remember { mutableStateOf(false) }
    var top by remember { mutableStateOf(DimensionValue(0, DimensionUnit.PERCENT)) }
    var bottom by remember { mutableStateOf(DimensionValue(0, DimensionUnit.PERCENT)) }
    var left by remember { mutableStateOf(DimensionValue(0, DimensionUnit.PERCENT)) }
    var right by remember { mutableStateOf(DimensionValue(0, DimensionUnit.PERCENT)) }

    val hasMedia = appViewModel.media != null

    fun setAllOffsets(value: DimensionValue) {
        top = value
        bottom = value
        left = value
        right = value
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

        MediaPreview()

        AutoPreviewOption(shouldAutoPreview, { shouldAutoPreview = it }, enabled = hasMedia)

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
                    pixelsAt100Percent = appViewModel.media?.height,
                    label = "Top",
                    enabled = hasMedia
                )

                DimensionInputField(
                    value = left,
                    onValueChange = { left = it },
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
                    pixelsAt100Percent = appViewModel.media?.height,
                    label = "Bottom",
                    enabled = hasMedia && !linkOffsets,
                )

                DimensionInputField(
                    value = right,
                    onValueChange = { right = it },
                    pixelsAt100Percent = appViewModel.media?.width,
                    label = "Right",
                    enabled = hasMedia && !linkOffsets,
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun CropVideoScreenPreview() {
    AppTheme {
        CropVideoScreen(AppViewModel(), Modifier)
    }
}
