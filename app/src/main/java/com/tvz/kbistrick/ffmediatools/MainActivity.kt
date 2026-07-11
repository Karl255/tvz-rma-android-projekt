package com.tvz.kbistrick.ffmediatools

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mzgs.ffmpegx.ffmpeg
import com.tvz.kbistrick.ffmediatools.service.FFMpegJobRunningService
import com.tvz.kbistrick.ffmediatools.ui.component.MediaPickerBar
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme
import com.tvz.kbistrick.ffmediatools.ui.theme.Space
import com.tvz.kbistrick.ffmediatools.util.saveToGallery
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)

        lifecycleScope.launch {
            // automatically initialize and isntall
            ffmpeg()
        }

        setContent {
            AppTheme {
                val appViewModel: AppViewModel = viewModel()

                DisposableEffect(Unit) {
                    val receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            val action = intent?.action
                            Log.i("MainActivity", "Received broadcast: $action")
                            if (action == FFMpegJobRunningService.ACTION_JOB_FINISHED) {
                                val path = intent.getStringExtra(FFMpegJobRunningService.EXTRA_OUTPUT_PATH)
                                Log.i("MainActivity", "Job finished, output path: $path")
                                appViewModel.updateProcessedMediaPath(path)
                            }
                        }
                    }

                    val filter = IntentFilter(FFMpegJobRunningService.ACTION_JOB_FINISHED)
                    Log.i("MainActivity", "Registering receiver for ${FFMpegJobRunningService.ACTION_JOB_FINISHED}")
                    
                    // Use applicationContext for a more stable registration
                    applicationContext.registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)

                    onDispose {
                        Log.i("MainActivity", "Unregistering receiver")
                        applicationContext.unregisterReceiver(receiver)
                    }
                }

                AppScaffold(appViewModel) { innerPadding ->
                    Navigation(appViewModel, Modifier.padding(innerPadding))
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
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        snackbarHost = {
            MediaPickerBar(
                media = appViewModel.media,
                processedMediaPath = appViewModel.processedMediaPath,
                onMediaChange = appViewModel::updateMedia,
                onAllMediaClear = appViewModel::clearAllMedia,
                onSaveProcessedMedia = { path -> saveToGallery(context, path) },
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
