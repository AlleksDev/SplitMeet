package com.coditos.splitmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.NavigationWrapper
import com.coditos.splitmeet.core.storage.TokenDataStore
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards FeatureNavGraph>

    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasToken = runBlocking {
            tokenDataStore.getToken() != null
        }
        val startDestination: Any = if (hasToken) Home else Login

        enableEdgeToEdge()
        setContent {
            SplitMeetTheme {
                NavigationWrapper(
                    navGraphs = navGraphs,
                    startDestination = startDestination
                )
            }
        }
    }
}