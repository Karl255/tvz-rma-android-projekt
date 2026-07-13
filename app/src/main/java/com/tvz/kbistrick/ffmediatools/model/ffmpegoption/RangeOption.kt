@file:Suppress("PropertyName")

package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

abstract class RangeOption(defaultValue: Int?) : FFMpegOption {
    protected abstract val LABEL: String
    protected abstract val KEY: String
    protected abstract val MIN: Int
    protected abstract val MAX: Int
    protected abstract val REQUIRED: Boolean

    protected var value: Int? = defaultValue

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render(onValueChanged: () -> Unit) {
        OutlinedTextField(
            value = value?.toString() ?: "",
            onValueChange = {
                value = it.toIntOrNull()?.coerceIn(MIN, MAX)
                onValueChanged()
            },
            label = { Text(LABEL) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        )
    }

    override fun isValid() = !REQUIRED || value != null

    override fun toArgs(): List<String> {
        val value = value

        return if (value == null) emptyList()
        else listOf(KEY, value.toString())
    }
}
