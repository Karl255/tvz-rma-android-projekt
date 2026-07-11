package com.tvz.kbistrick.ffmediatools


import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space

@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier,
    onNavigateToTool: (toolName: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        horizontalArrangement = Arrangement.spacedBy(Space.M),
        verticalArrangement = Arrangement.spacedBy(Space.M),
        contentPadding = PaddingValues(Space.M),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        item {
            ToolCard(
                "Scale",
                R.drawable.pictogram_scale,
                onClick = { onNavigateToTool("scaleMedia") }
            )
        }

        item {
            ToolCard(
                "Convert and compress",
                R.drawable.pictogram_convert_and_compress,
                onClick = { onNavigateToTool("convertAndCompress") }
            )
        }
        item {
            ToolCard(
                "Crop video",
                R.drawable.pictogram_crop_video,
                onClick = { onNavigateToTool("cropVideo") }
            )
        }
    }
}

@Composable
fun ToolCard(title: String, @DrawableRes bannerResourceId: Int, onClick: () -> Unit) {
    // TODO fancier card style, text over image
    OutlinedCard(
        modifier = Modifier.clickable(true, onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(bannerResourceId),
                contentDescription = "banner image",
                modifier = Modifier.aspectRatio(4 / 3f)
            )
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Space.S)
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ToolsScreenPreview() {
    val appViewModel = AppViewModel()
    AppTheme {
        AppScaffold(appViewModel) { innerPadding ->
            ToolsScreen(onNavigateToTool = {}, modifier = Modifier.padding(innerPadding))
        }
    }
}
