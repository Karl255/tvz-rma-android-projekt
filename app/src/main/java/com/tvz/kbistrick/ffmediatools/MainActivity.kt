package com.tvz.kbistrick.ffmediatools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tvz.kbistrick.ffmediatools.ui.theme.FFMediaToolsTheme

/*
layout components:
- (Lazy)Column, (Lazy)Row
- FlowColumn, FlowRow -> wrapping
- Box
- LazyVertical(Staggered)Grid, LazyHorizontal(Staggered)Grid
- VerticalPager, HorizontalPager

*/

val SPACE_M = 16.dp
val SPACE_S = 8.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FFMediaToolsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        horizontalArrangement = Arrangement.spacedBy(SPACE_M),
        verticalArrangement = Arrangement.spacedBy(SPACE_M),
        contentPadding = PaddingValues(SPACE_M),
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEBEBEB))
    ) {
        item { ToolCard("Scale") }
        item { ToolCard("Convert and compress") }
        item { ToolCard("Crop video") }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FFMediaToolsTheme {
        MainScreen()
    }
}

@Composable
fun ToolCard(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(SPACE_S))
            .background(Color.White)
    ) {
        Text(
            "image placeholder",
            textAlign = TextAlign.Center,
            modifier = Modifier.aspectRatio(16 / 9f).background(Color(0xFFFFE8B0))
        )
        Text(
            title,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(SPACE_S)
        )
    }
}
