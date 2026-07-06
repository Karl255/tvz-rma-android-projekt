package com.tvz.kbistrick.ffmediatools

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme

@Composable
fun ScaleMediaScreen(modifier: Modifier = Modifier) {
    Text("Scale media screen", modifier = modifier.fillMaxSize())
}


@Preview(showBackground = true)
@Composable
fun ScaleMediaScreenPreview() {
    AppTheme(dynamicColor = false) {
        ScaleMediaScreen(Modifier)
    }
}
