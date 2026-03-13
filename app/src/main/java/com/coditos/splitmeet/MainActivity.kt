package com.coditos.splitmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.NavigationWrapper
import com.coditos.splitmeet.core.session.domain.model.AppStartDestination
import com.coditos.splitmeet.core.session.presentation.SessionGateState
import com.coditos.splitmeet.core.session.presentation.SessionGateViewModel
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards FeatureNavGraph>

    private val sessionGateViewModel: SessionGateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SplitMeetTheme {
                val uiState by sessionGateViewModel.uiState.collectAsStateWithLifecycle()

                when (val state = uiState) {
                    SessionGateState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is SessionGateState.Ready -> {
                        val startDestination = when (state.destination) {
                            AppStartDestination.HOME -> Home
                            AppStartDestination.LOGIN -> Login
                        }

                        NavigationWrapper(
                            navGraphs = navGraphs,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}