package com.coditos.splitmeet.features.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.User
import com.coditos.splitmeet.features.auth.presentation.components.AuthButton
import com.coditos.splitmeet.features.auth.presentation.components.AuthErrorText
import com.coditos.splitmeet.features.auth.presentation.components.AuthPasswordField
import com.coditos.splitmeet.features.auth.presentation.components.AuthTextButton
import com.coditos.splitmeet.features.auth.presentation.components.AuthTextField
import com.coditos.splitmeet.features.auth.presentation.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordsMatch = password == confirmPassword
    val isFormValid = username.isNotBlank() &&
            name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            passwordsMatch

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario",
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre completo",
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                keyboardType = KeyboardType.Email,
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Teléfono",
                keyboardType = KeyboardType.Phone,
                enabled = !uiState.isLoading,
                isError = uiState.error != null
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                enabled = !uiState.isLoading,
                isError = uiState.error != null || (!passwordsMatch && confirmPassword.isNotBlank())
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthPasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                enabled = !uiState.isLoading,
                isError = !passwordsMatch && confirmPassword.isNotBlank()
            )

            if (!passwordsMatch && confirmPassword.isNotBlank()) {
                AuthErrorText(error = "Las contraseñas no coinciden")
            }

            if (uiState.error != null) {
                AuthErrorText(error = uiState.error!!)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = "Registrarse",
                onClick = {
                    viewModel.register(
                        User(
                            username = username,
                            name = name,
                            email = email,
                            phone = phone,
                            password = password
                        )
                    )
                },
                isLoading = uiState.isLoading,
                enabled = isFormValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextButton(
                text = "¿Ya tienes cuenta? Inicia sesión",
                onClick = onNavigateToLogin,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
