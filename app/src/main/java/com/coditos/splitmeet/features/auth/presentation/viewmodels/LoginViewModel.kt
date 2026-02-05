package com.coditos.splitmeet.features.auth.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.BaseViewModel
import com.coditos.splitmeet.features.auth.domain.entities.User
import com.coditos.splitmeet.features.auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Login screen
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<User>() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun login() {
        viewModelScope.launch {
            setLoading()
            when (val result = loginUseCase(_email.value, _password.value)) {
                is NetworkResult.Success -> setSuccess(result.data)
                is NetworkResult.Error -> setError(result.message)
                is NetworkResult.Loading -> setLoading()
            }
        }
    }
}
