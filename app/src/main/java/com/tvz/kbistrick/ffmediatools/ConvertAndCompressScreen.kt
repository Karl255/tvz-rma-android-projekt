package com.tvz.kbistrick.ffmediatools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.ui.components.AutoPreviewOption
import com.tvz.kbistrick.ffmediatools.ui.components.DimensionInputField
import com.tvz.kbistrick.ffmediatools.ui.components.MediaPreview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertAndCompressScreen(modifier: Modifier = Modifier) {
    var shouldAutoPreview by remember { mutableStateOf(true) }
    var formatDropdownExpanded by remember { mutableStateOf(false) }
    var selectedFormat by remember { mutableStateOf(MediaFormat.PNG) }

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

        MediaPreview()

        AutoPreviewOption(shouldAutoPreview) { shouldAutoPreview = it }

        ExposedDropdownMenuBox(
            expanded = formatDropdownExpanded,
            onExpandedChange = { formatDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedFormat.displayText,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text("Format") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(formatDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = formatDropdownExpanded,
                onDismissRequest = { formatDropdownExpanded = false }
            ) {
                MediaFormat.entries.forEach { format ->
                    DropdownMenuItem(
                        text = { Text(format.displayText) },
                        onClick = {
                            selectedFormat = format
                            formatDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConvertAndCompressScreenPreview() {
    AppTheme {
        ConvertAndCompressScreen(Modifier)
    }
}
