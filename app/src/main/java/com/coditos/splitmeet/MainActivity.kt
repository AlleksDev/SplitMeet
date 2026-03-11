package com.coditos.splitmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.NavigationWrapper
import com.coditos.splitmeet.core.storage.TokenDataStore
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme
import com.coditos.splitmeet.features.auth.navigation.AuthNavGraph
import com.coditos.splitmeet.features.detailOuting.navigation.DetailOutingNavGraph
import com.coditos.splitmeet.features.home.navigation.HomeNavGraph
import com.coditos.splitmeet.features.manageOuting.navigation.ManageOutingNavGraph
import com.coditos.splitmeet.features.outing.navigation.OutingNavGraph
import com.coditos.splitmeet.features.product.navigation.ProductNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navGraphs = listOf(
            AuthNavGraph(),
            HomeNavGraph(),
            OutingNavGraph(),
            ManageOutingNavGraph(),
            DetailOutingNavGraph(),
            ProductNavGraph()
        )

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