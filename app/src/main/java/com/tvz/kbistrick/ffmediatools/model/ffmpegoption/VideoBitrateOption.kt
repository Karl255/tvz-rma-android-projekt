@file:Suppress("PrivatePropertyName")

package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

val BITRATE_REGEX = Regex("^[1-9][0-9]*(.[0-9]+)?[kKmM]?$")

class VideoBitrateOption(defaultValue: String) : FFMpegOption {
    private val LABEL: String = "Video bitrate"
    private val KEY: String = "-b:v"

    private var value: String = defaultValue

    @Composable
    override fun Render(onValueChanged: () -> Unit) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                onValueChanged()
            },
            label = { Text(LABEL) },
            supportingText = { Text("Example: 8M, 1.5M, 150k, 30000") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    override fun isValid() = BITRATE_REGEX.matches(value)

    override fun toArgs() = listOf(KEY, value)

    companion object {
        fun init() = VideoBitrateOption("4M")
    }
}