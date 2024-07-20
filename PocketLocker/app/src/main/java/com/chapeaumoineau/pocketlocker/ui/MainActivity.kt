package com.chapeaumoineau.pocketlocker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chapeaumoineau.pocketlocker.feature.albums.AlbumScreen
import com.chapeaumoineau.pocketlocker.feature.albums.AlbumViewModel
import com.chapeaumoineau.pocketlocker.feature.content.ContentScreen
import com.chapeaumoineau.pocketlocker.feature.content.ContentViewModel
import com.chapeaumoineau.pocketlocker.feature.home_lock.HomeLockScreen
import com.chapeaumoineau.pocketlocker.feature.home_lock.HomeLockViewModel
import com.chapeaumoineau.pocketlocker.ui.theme.PocketLockerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            PocketLockerTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeLock.route
                    ) {
                        composable(Screen.HomeLock.route) {
                            val viewModel = hiltViewModel<HomeLockViewModel>()
                            val state by viewModel.state.collectAsState()
                            HomeLockScreen(
                                navController = navController,
                                state = state,
                                onEvent = viewModel::onEvent,
                                uiEvent = viewModel.eventFlow
                            )
                        }
                        composable(Screen.HomeAlbum.route) {
                            val viewModel = hiltViewModel<AlbumViewModel>()
                            val state by viewModel.state.collectAsState()
                            AlbumScreen(
                                navController = navController,
                                state = state,
                                onEvent = viewModel::onEvent,
                                uiEvent = viewModel.eventFlow
                            )
                        }
                        composable(Screen.AlbumContent.route + "?albumId={albumId}&key={key}",
                            arguments = listOf(
                                navArgument(name = "albumId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "key") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                                )) {
                            val viewModel = hiltViewModel<ContentViewModel>()
                            val state by viewModel.state.collectAsState()
                            ContentScreen(
                                navController = navController,
                                state = state,
                                onEvent = viewModel::onEvent,
                                uiEvent = viewModel.eventFlow
                            )
                        }
                    }
                }
            }
        }
    }
}