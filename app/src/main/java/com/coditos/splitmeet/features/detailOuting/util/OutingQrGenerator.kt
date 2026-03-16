package com.coditos.splitmeet.features.detailOuting.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object OutingQrGenerator {
    private const val BASE_URL = "https://frimeet.fun"
    private const val OUTING_PATH = "/outings"
    private const val JOIN_SUFFIX = "/join"
    private const val MARGIN = 1

    fun buildDeepLink(outingId: String): String {
        return "$BASE_URL$OUTING_PATH/$outingId$JOIN_SUFFIX"
    }

    fun generateBitmap(content: String, sizePx: Int = 512): Bitmap {
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
            EncodeHintType.MARGIN to MARGIN
        )

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            sizePx,
            sizePx,
            hints
        )

        return createBitmapFromBitMatrix(bitMatrix)
    }

    private fun createBitmapFromBitMatrix(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }

        return bitmap
    }
}
