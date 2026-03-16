package com.coditos.splitmeet.features.detailOuting.domain.usecases

import com.coditos.splitmeet.features.detailOuting.domain.entities.OutingQrContent
import com.coditos.splitmeet.features.detailOuting.util.OutingQrGenerator
import javax.inject.Inject

class GenerateOutingQrUseCase @Inject constructor() {
    suspend operator fun invoke(outingId: Long): Result<OutingQrContent> = try {
        val outingIdStr = outingId.toString()
        val deepLink = OutingQrGenerator.buildDeepLink(outingIdStr)
        val content = OutingQrContent(
            outingId = outingIdStr,
            deepLink = deepLink
        )
        Result.success(content)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
