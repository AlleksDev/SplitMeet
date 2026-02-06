package com.coditos.splitmeet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coditos.splitmeet.core.di.AppContainer
import com.coditos.splitmeet.core.navigation.NavigationWrapper
import com.coditos.splitmeet.core.ui.theme.SplitMeetTheme
import com.coditos.splitmeet.features.home.navigation.HomeNavGraph
import com.coditos.splitmeet.features.home.di.HomeModule
import com.coditos.splitmeet.features.home.presentation.screens.HomeScreen
class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(this)
        val homeModule  = HomeModule(appContainer)

        val navGraphs = listOf(
            HomeNavGraph(homeModule)
        )

        enableEdgeToEdge()
        setContent {
            SplitMeetTheme {
                NavigationWrapper(navGraphs)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SplitMeetTheme {
        Greeting("Android")
    }
}