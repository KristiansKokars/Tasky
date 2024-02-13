package com.kristianskokars.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.feature.auth.domain.model.AuthState
import com.kristianskokars.tasky.lib.Toaster
import com.kristianskokars.tasky.lib.currentGraph
import com.kristianskokars.tasky.lib.launchImmediate
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.isInitializingApp.value }

        setContent {
            val engine = rememberAnimatedNavHostEngine()
            val navControler = engine.rememberNavController()

            // BUG: reruns every time on screen rotations, later refactor for better solution
            LaunchedEffect(key1 = Unit) {
                launchImmediate {
                    viewModel.authState.collectLatest { authState ->
                        when (authState) {
                            AuthState.RetrievingFromStorage -> {}
                            AuthState.LoggedOut -> {
                                navControler.navigate(NavGraphs.root) {
                                    launchSingleTop = true
                                    popUpTo(NavGraphs.root.route) {
                                        inclusive = true
                                    }
                                }
                                viewModel.appHasInitialized()
                            }
                            is AuthState.LoggedIn -> {
                                if (navControler.currentGraph() != NavGraphs.root) {
                                    viewModel.appHasInitialized()
                                    return@collectLatest
                                }

                                navControler.navigate(NavGraphs.appGraph) {
                                    launchSingleTop = true
                                    popUpTo(NavGraphs.root.route) {
                                        inclusive = true
                                    }
                                }
                                viewModel.appHasInitialized()
                            }
                        }
                    }
                }
            }

            ScreenSurface {
                DestinationsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navGraph = NavGraphs.root,
                    engine = engine,
                    navController = navControler
                )
            }
        }
    }
}
