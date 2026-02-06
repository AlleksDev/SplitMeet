package com.coditos.splitmeet.features.auth.data.datasoruces.remote.mapper

import com.coditos.splitmeet.features.auth.data.datasoruces.remote.model.LoginResponseDto
import com.coditos.splitmeet.features.auth.domain.entities.LoginResponse

fun LoginResponseDto.loginToDomain(): LoginResponse {
    return LoginResponse(
        message = this.message,
        token = this.token
    )
}