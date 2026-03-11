package com.coditos.splitmeet.features.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Notifications
import com.coditos.splitmeet.features.notification.presentation.screens.NotificationScreen
import javax.inject.Inject

class NotificationNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Notifications> {
            NotificationScreen()
        }
    }
}
