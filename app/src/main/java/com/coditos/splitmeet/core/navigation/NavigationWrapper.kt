package com.coditos.splitmeet.core.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper(
    navGraphs: List<FeatureNavGraph>
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        Log.d("NavigationWrapper", "Registering graphs")
        navGraphs.forEach { graph ->
            graph.registerGraph(this, navController)
        }
    }
}