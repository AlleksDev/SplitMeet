package com.coditos.splitmeet.features.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateOuting
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.MainScreen
import com.coditos.splitmeet.core.navigation.OutingDetail
import javax.inject.Inject

class HomeNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            MainScreen(
                onNavigateToCreateOuting = {
                    navController.navigate(CreateOuting)
                },
                onNavigateToOutingDetail = { outingId ->
                    navController.navigate(OutingDetail(outingId))
                },
                onLoggedOut = {
                    navController.navigate(Login) {
                        popUpTo(Home) { inclusive = true }
                    }
                }
            )
        }
    }
}