package com.coditos.splitmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.NavigationWrapper
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme
import com.coditos.splitmeet.features.auth.di.AuthModule
import com.coditos.splitmeet.features.auth.navigation.AuthNavGraph
import com.coditos.splitmeet.features.detailOuting.di.DetailOutingModule
import com.coditos.splitmeet.features.detailOuting.navigation.DetailOutingNavGraph
import com.coditos.splitmeet.features.home.di.HomeModule
import com.coditos.splitmeet.features.home.navigation.HomeNavGraph
import com.coditos.splitmeet.features.outing.di.OutingModule
import com.coditos.splitmeet.features.outing.navigation.OutingNavGraph
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(this)
        val homeModule = HomeModule(appContainer)
        val authModule = AuthModule(appContainer)
        val outingModule = OutingModule(appContainer)
        val detailOutingModule = DetailOutingModule(appContainer)

        val navGraphs = listOf(
            AuthNavGraph(authModule),
            HomeNavGraph(homeModule),
            OutingNavGraph(outingModule),
            DetailOutingNavGraph(detailOutingModule)
        )

        val hasToken = runBlocking {
            appContainer.tokenDataStore.getToken() != null
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