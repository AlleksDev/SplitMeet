package com.coditos.splitmeet.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Generic ViewModelFactory for creating ViewModels with dependencies
 * Use this pattern to inject use cases into ViewModels
 */
class ViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}

/**
 * Inline function to create ViewModelFactory more easily
 */
inline fun <reified VM : ViewModel> viewModelFactory(
    noinline creator: () -> VM
): ViewModelProvider.Factory = ViewModelFactory(creator)
