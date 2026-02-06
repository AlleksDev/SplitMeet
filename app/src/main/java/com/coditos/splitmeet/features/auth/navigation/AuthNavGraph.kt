package com.coditos.splitmeet.features.auth.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.coditos.splitmeet.core.navigation.FeatureNavGraph
import com.coditos.splitmeet.core.navigation.Home
import com.coditos.splitmeet.core.navigation.Login
import com.coditos.splitmeet.core.navigation.Register
import com.coditos.splitmeet.features.auth.di.AuthModule
import com.coditos.splitmeet.features.auth.presentation.screens.LoginScreen
import com.coditos.splitmeet.features.auth.presentation.screens.RegisterScreen
import com.coditos.splitmeet.features.auth.presentation.viewmodels.AuthViewModel

class AuthNavGraph(
    private val authModule: AuthModule
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Login> {
            val viewModel: AuthViewModel = viewModel(
                factory = authModule.provideAuthViewModelFactory()
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Register)
                }
            )
        }

        navGraphBuilder.composable<Register> {
            val viewModel: AuthViewModel = viewModel(
                factory = authModule.provideAuthViewModelFactory()
            )
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
