package com.coditos.splitmeet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
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
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards FeatureNavGraph>

    private val sessionGateViewModel: SessionGateViewModel by viewModels()

    // Launcher for POST_NOTIFICATIONS permission (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Whether granted or not, the app continues working */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()

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

    /**
     * On Android 13+ (API 33), we need to request POST_NOTIFICATIONS at runtime
     * for push notifications to be displayed.
     */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(permission)
            }
        }
    }
}