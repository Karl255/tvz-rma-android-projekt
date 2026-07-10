package com.tvz.kbistrick.ffmediatools.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tvz.kbistrick.ffmediatools.model.NumberUnit
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space

@Composable
fun DimensionInputField(
    modifier: Modifier = Modifier,
    value: Int = 100,
    onValueChange: (Int) -> Unit = {},
    minValue: Int = 0,
    maxValue: Int? = null,
    unit: NumberUnit = NumberUnit.PERCENT,
    onNumberUnitChange: (NumberUnit) -> Unit = {},
    label: String? = null,
) {
    OutlinedTextField(
        value.toString(),
        onValueChange = {
            val parsed = if (it.isBlank()) 0 else it.trim().toIntOrNull()
            parsed?.let { p ->
                onValueChange(p.coerceIn(minValue, maxValue ?: Int.MAX_VALUE))
            }
        },
        label = label?.let { { Text(it) } },
        suffix = {
            Text(
                unit.displayText,
                modifier = Modifier.clickable {
                    onNumberUnitChange(
                        when (unit) {
                            NumberUnit.PIXEL -> NumberUnit.PERCENT
                            NumberUnit.PERCENT -> NumberUnit.PIXEL
                        }
                    )
                }
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier.padding(bottom = 5.dp), // so the input field is properly vertically centered with the label peeking out the top
    )
}

@Composable
@Preview
fun DimensionInputFieldPreview() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(Space.M),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(Space.M)
        ) {
            DimensionInputField(value = 100, label = "Label")
            DimensionInputField(value = 123, label = "Label")
        }
    }
}