package com.coditos.splitmeet.features.auth.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.BaseViewModel
import com.coditos.splitmeet.features.auth.domain.entities.User
import com.coditos.splitmeet.features.auth.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Register screen
 */
class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<User>() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun register() {
        viewModelScope.launch {
            setLoading()
            when (val result = registerUseCase(
                _name.value,
                _email.value,
                _password.value,
                _confirmPassword.value
            )) {
                is NetworkResult.Success -> setSuccess(result.data)
                is NetworkResult.Error -> setError(result.message)
                is NetworkResult.Loading -> setLoading()
            }
        }
    }
}
