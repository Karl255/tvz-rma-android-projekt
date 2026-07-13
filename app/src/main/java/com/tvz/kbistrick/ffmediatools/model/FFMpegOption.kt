@file:Suppress("PropertyName")

package com.tvz.kbistrick.ffmediatools.model

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

interface FFMpegOption {
    @Composable
    fun Render()

    fun toArgs(): List<String>
}

abstract class EnumOption(defaultOption: Option?) : FFMpegOption {
    protected abstract val LABEL: String
    protected abstract val KEY: String
    protected abstract val OPTIONS: List<Option>
    protected var selectedOption: Option? = defaultOption

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
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
                        }
                    )
                }
            }
        }
    }

    override fun toArgs(): List<String> {
        val selectedValue = selectedOption

        return if (selectedValue == null) emptyList()
        else listOf("-q:v", selectedValue.value)
    }

    data class Option(val value: String, val display: String)
}

class JpegQualityOption(defaultValue: Option?) : EnumOption(defaultValue) {
    override val LABEL = "Quality"
    override val KEY = "-q:v"

    override val OPTIONS = listOf(
        Option("1", "Best (1)"),
        Option("2", "Best (2)"),
        Option("3", "Best (3)"),
        Option("4", "Best (4)"),
        Option("5", "Best (5)"),
        Option("6", "Best (6)"),
        Option("7", "Good (7)"),
        Option("8", "Good (8)"),
        Option("9", "Good (9)"),
        Option("10", "Good (10)"),
        Option("11", "Good (11)"),
        Option("12", "Good (12)"),
        Option("13", "Average (13)"),
        Option("14", "Average (14)"),
        Option("15", "Average (15)"),
        Option("16", "Average (16)"),
        Option("17", "Average (17)"),
        Option("18", "Average (18)"),
        Option("19", "Average (19)"),
        Option("20", "Bad (20)"),
        Option("21", "Bad (21)"),
        Option("22", "Bad (22)"),
        Option("23", "Bad (23)"),
        Option("24", "Bad (24)"),
        Option("25", "Bad (25)"),
        Option("26", "Worst (26)"),
        Option("27", "Worst (27)"),
        Option("28", "Worst (28)"),
        Option("29", "Worst (29)"),
        Option("30", "Worst (30)"),
        Option("31", "Worst (31)"),
    )

    companion object {
        fun init() = JpegQualityOption(null)
    }
}
