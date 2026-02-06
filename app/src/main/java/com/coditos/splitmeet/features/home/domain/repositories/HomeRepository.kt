package com.coditos.splitmeet.features.home.domain.repositories

import com.coditos.splitmeet.features.home.domain.entities.Outing


interface HomeRepository {
    suspend fun getOutings(): List<Outing>
}
