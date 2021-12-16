package com.boostcampai.foodlog

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    val width = bitmap.width
    val height = bitmap.height
    val resized = Bitmap.createScaledBitmap(
        bitmap,
        width.toInt(),
        height.toInt(),
        false
    )
    resized.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), 0) ?: ""
}
