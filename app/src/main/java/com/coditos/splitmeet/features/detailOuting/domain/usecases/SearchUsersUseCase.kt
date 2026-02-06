package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.SearchUser
import com.coditos.splitmeet.features.detailOuting.domain.repositories.DetailOutingRepository

class SearchUsersUseCase(
    private val repository: DetailOutingRepository
) {
    suspend operator fun invoke(username: String): Result<List<SearchUser>> {
        return try {
            val users = repository.searchUsers(username)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
