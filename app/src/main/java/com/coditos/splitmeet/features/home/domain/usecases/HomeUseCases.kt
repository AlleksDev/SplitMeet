package com.coditos.splitmeet.features.home.domain.usecases

data class HomeUseCases(
    val getOutings: GetOutingsUseCase,
    val syncOutings: SyncOutingsUseCase
)
