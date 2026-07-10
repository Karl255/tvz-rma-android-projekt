package com.tvz.kbistrick.ffmediatools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import com.tvz.kbistrick.ffmediatools.model.NumberUnit
import com.tvz.kbistrick.ffmediatools.ui.components.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.components.DimensionInputField
import com.tvz.kbistrick.ffmediatools.ui.components.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space

@Composable
fun ScaleMediaScreen(modifier: Modifier = Modifier) {
    var shouldAutoPreview by remember { mutableStateOf(true) }
    var linkWidthHeight by remember { mutableStateOf(true) }
    var width by remember { mutableStateOf(100) }
    var height by remember { mutableStateOf(100) }
    var widthUnit by remember { mutableStateOf(NumberUnit.PERCENT) }
    var heightUnit by remember { mutableStateOf(NumberUnit.PERCENT) }

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

        AutoPreviewOption(shouldAutoPreview) { shouldAutoPreview = it }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.S),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DimensionInputField(
                value = width,
                onValueChange = {
                    width = it

                    if (linkWidthHeight) {
                        height = it
                    }
                },
                unit = widthUnit,
                onNumberUnitChange = {
                    widthUnit = it

                    if (linkWidthHeight) {
                        heightUnit = it
                    }
                },
                label = "Width",
                modifier = Modifier.weight(1f)
            )

            IconToggleButton(linkWidthHeight, {
                linkWidthHeight = it
                if (linkWidthHeight) {
                    height = width
                    heightUnit = widthUnit
                }
            }) {
                Icon(Icons.Default.Link, contentDescription = "Link dimensions")
            }

            DimensionInputField(
                value = height,
                onValueChange = {
                    height = it

                    if (linkWidthHeight) {
                        width = it
                    }
                },
                unit = heightUnit,
                onNumberUnitChange = {
                    heightUnit = it

                    if (linkWidthHeight) {
                        widthUnit = it
                    }
                },
                label = "Height",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScaleMediaScreenPreview() {
    AppTheme {
        ScaleMediaScreen(Modifier)
    }
}
