package com.tvz.kbistrick.ffmediatools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tvz.kbistrick.ffmediatools.ui.theme.AppTheme

/*
NOTES

layout components:
- (Lazy)Column, (Lazy)Row
- FlowColumn, FlowRow -> wrapping
- Box
- LazyVertical(Staggered)Grid, LazyHorizontal(Staggered)Grid
- VerticalPager, HorizontalPager
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "tools") {
        composable("tools") {
            ToolsScreen(modifier, onNavigateToTool = { toolName -> navController.navigate("tools/${toolName}") })
        }

        composable("tools/scaleMedia") {
            ScaleMediaScreen(modifier)
        }

        composable("tools/cropVideo") {
            CropVideoScreen(modifier)
        }
    }
}
