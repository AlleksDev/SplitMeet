package com.coditos.splitmeet.features.group.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.CreateGroup
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.GroupDetail
import com.coditos.splitmeet.features.group.presentation.screens.CreateGroupScreen
import com.coditos.splitmeet.features.group.presentation.screens.GroupDetailScreen
import javax.inject.Inject

class GroupNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<CreateGroup> {
            CreateGroupScreen(
                onGroupCreated = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        navGraphBuilder.composable<GroupDetail> {
            GroupDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
