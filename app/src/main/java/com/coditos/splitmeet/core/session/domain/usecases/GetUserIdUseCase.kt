package com.coditos.splitmeet.core.session.domain.usecases

import android.util.Base64
import com.coditos.splitmeet.core.storage.TokenDataStore
import org.json.JSONObject
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class GetUserIdUseCase @Inject constructor(
    private val tokenDataStore: TokenDataStore
) {
    suspend operator fun invoke(): Int? {
        val token = tokenDataStore.getToken() ?: return null
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            
            if (json.has("user_id")) {
                json.getInt("user_id")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
