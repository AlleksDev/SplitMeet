package com.coditos.splitmeet.features.outing.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.coditos.splitmeet.core.ui.BaseViewModel
import com.coditos.splitmeet.features.outing.domain.entities.Outing
import com.coditos.splitmeet.features.outing.domain.usecases.GetOutingByIdUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel for Outing detail screen
 */
class OutingDetailViewModel(
    private val getOutingByIdUseCase: GetOutingByIdUseCase
) : BaseViewModel<Outing>() {

    fun loadOuting(id: String) {
        viewModelScope.launch {
            setLoading()
            when (val result = getOutingByIdUseCase(id)) {
                is NetworkResult.Success -> setSuccess(result.data)
                is NetworkResult.Error -> setError(result.message)
                is NetworkResult.Loading -> setLoading()
            }
        }
    }

    fun retry(id: String) {
        loadOuting(id)
    }
}
