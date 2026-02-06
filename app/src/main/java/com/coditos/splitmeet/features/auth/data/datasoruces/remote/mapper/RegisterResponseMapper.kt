package com.coditos.splitmeet.features.auth.data.datasoruces.remote.mapper

import com.coditos.splitmeet.features.auth.domain.entities.RegisterResponse

fun RegisterResponse.registerToDomain(): RegisterResponse {
    return RegisterResponse(
        id = this.id,
        email = this.email,
        message = this.message
    )
}
