package com.coditos.splitmeet.features.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginRequest
import com.coditos.splitmeet.features.auth.presentation.components.AuthButton
import com.coditos.splitmeet.features.auth.presentation.components.AuthErrorText
import com.coditos.splitmeet.features.auth.presentation.components.AuthPasswordField
import com.coditos.splitmeet.features.auth.presentation.components.AuthTextButton
import com.coditos.splitmeet.features.auth.presentation.components.AuthTextField
import com.coditos.splitmeet.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
            viewModel.clearState()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                keyboardType = KeyboardType.Email,
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            if (uiState.error != null) {
                AuthErrorText(error = uiState.error!!)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = "Iniciar Sesión",
                onClick = {
                    viewModel.login(
                        LoginRequest(
                            email = email,
                            password = password
                        )
                    )
                },
                isLoading = uiState.isLoading,
                enabled = email.isNotBlank() && password.isNotBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextButton(
                text = "¿No tienes cuenta? Regístrate",
                onClick = onNavigateToRegister,
                enabled = !uiState.isLoading
            )
        }
    }
}

