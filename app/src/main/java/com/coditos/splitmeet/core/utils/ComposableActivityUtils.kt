package com.coditos.splitmeet.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity

/**
 * Obtiene la FragmentActivity actual de forma segura en un Composable.
 * Esta función busca la activity atravesando la jerarquía de contextos.
 */
@Composable
fun getFragmentActivity(): FragmentActivity? {
    val context = LocalContext.current
    return context.findFragmentActivity()
}
