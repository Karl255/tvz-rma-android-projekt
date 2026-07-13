@file:Suppress("PropertyName")

package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

abstract class EnumOption(defaultOption: Option?) : FFMpegOption {
    protected abstract val LABEL: String
    protected abstract val KEY: String
    protected abstract val OPTIONS: List<Option>

    protected var selectedOption: Option? = defaultOption

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render(onValueChanged: () -> Unit) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedOption?.display ?: "",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text(LABEL) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                OPTIONS.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.display) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                            onValueChanged()
                        }
                    )
                }
            }
        }
    }

    override fun toArgs(): List<String> {
        val selectedValue = selectedOption

        return if (selectedValue == null) emptyList()
        else listOf(KEY, selectedValue.value)
    }

    data class Option(val value: String, val display: String)
}
