package com.coditos.splitmeet.core.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(
    navGraphs: List<FeatureNavGraph>,
    startDestination: Any = Login
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        Log.d("NavigationWrapper", "Registering graphs")
        navGraphs.forEach { graph ->
            graph.registerGraph(this, navController)
        }
    }
}