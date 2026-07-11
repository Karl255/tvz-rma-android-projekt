package com.tvz.kbistrick.ffmediatools.ui.component

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
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.toggleUnit

@Composable
fun DimensionInputField(
    modifier: Modifier = Modifier,
    value: DimensionValue = DimensionValue(100, DimensionUnit.PERCENT),
    onValueChange: (DimensionValue) -> Unit = {},
    minValue: Int = 1,
    maxValue: Int? = null,
    pixelsAt100Percent: Int? = null,
    label: String? = null,
    enabled: Boolean = true,
) {
    OutlinedTextField(
        value.number.toString(),
        onValueChange = {
            val parsed =
                if (it.isBlank()) 0
                else it.trim().toIntOrNull() ?: 0

            onValueChange(
                value.copy(number = parsed.coerceIn(minValue, maxValue ?: Int.MAX_VALUE))
            )

        },
        label = label?.let { { Text(it) } },
        suffix = {
            Text(
                value.unit.displayText,
                modifier = Modifier.clickable {
                    onValueChange(value.toggleUnit(pixelsAt100Percent))
                }
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        enabled = enabled,
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
            DimensionInputField(value = DimensionValue(100, DimensionUnit.PERCENT), label = "Label")
            DimensionInputField(value = DimensionValue(720, DimensionUnit.PIXEL), label = "Label")
        }
    }
}