package com.tvz.kbistrick.ffmediatools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mzgs.ffmpegx.FFmpeg
import com.tvz.kbistrick.ffmediatools.ui.components.MediaPickerBar
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var ffmpeg: FFmpeg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ffmpeg = FFmpeg.initialize(this)

        lifecycleScope.launch {
            if (!ffmpeg.isInstalled()) {
                ffmpeg.install()
            }
        }

        setContent {
            CompositionLocalProvider(LocalFFmpeg provides ffmpeg) {
                AppTheme {
                    val appViewModel: AppViewModel = viewModel()

                    AppScaffold(appViewModel) { innerPadding ->
                        Navigation(appViewModel, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun AppScaffold(
    appViewModel: AppViewModel,
    content: @Composable ((PaddingValues) -> Unit)
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        snackbarHost = {
            MediaPickerBar(
                media = appViewModel.media,
                onMediaChange = appViewModel::updateMedia,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = Space.M)
                    .padding(bottom = Space.M)
            )
        },
        content = content,
    )
}

@Composable
fun Navigation(appViewModel: AppViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "tools") {
        composable("tools") {
            ToolsScreen(modifier, onNavigateToTool = { toolName -> navController.navigate("tools/${toolName}") })
        }

        composable("tools/scaleMedia") {
            ScaleMediaScreen(appViewModel, modifier)
        }

        composable("tools/convertAndCompress") {
            ConvertAndCompressScreen(appViewModel, modifier)
        }

        composable("tools/cropVideo") {
            CropVideoScreen(appViewModel, modifier)
        }
    }
}
