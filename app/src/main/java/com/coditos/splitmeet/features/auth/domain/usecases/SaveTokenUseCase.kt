package com.coditos.splitmeet.features.auth.domain.usecases

import com.coditos.splitmeet.core.storage.TokenDataStore

class SaveTokenUseCase(
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(token: String) {
        tokenDataStore.saveToken(token)
    }
}
