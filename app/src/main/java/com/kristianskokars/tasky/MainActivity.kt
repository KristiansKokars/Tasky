package com.kristianskokars.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.feature.NavGraphs
import com.kristianskokars.tasky.feature.auth.data.model.AuthState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authState by viewModel.authState.collectAsStateWithLifecycle()
            val engine = rememberNavHostEngine()
            val navControler = engine.rememberNavController()

            LaunchedEffect(key1 = authState) {
                when (authState) {
                    AuthState.RetrievingFromStorage -> {}
                    AuthState.LoggedOut -> navControler.navigate(NavGraphs.root)
                    is AuthState.LoggedIn -> navControler.navigate(NavGraphs.appGraph)
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
