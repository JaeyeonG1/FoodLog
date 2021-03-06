package com.boostcampai.foodlog

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Base64
import com.boostcampai.foodlog.model.Position
import java.io.ByteArrayOutputStream

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    val width = bitmap.width
    val height = bitmap.height
    val resized = Bitmap.createScaledBitmap(
        bitmap,
        width,
        height,
        false
    )
    resized.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), 0) ?: ""
}

fun Bitmap.drawBoundingBoxes(boundingBoxes: List<Position>): Bitmap {
    val bitmap = copy(config, true)
    val canvas = Canvas(bitmap)

    Paint().apply {
        color = Color.RED
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f

        boundingBoxes.forEach {
            canvas.drawRect(
                width * it.topLeftX,
                height * it.topLeftY,
                width * it.bottomRightX,
                height * it.bottomRightY,
                this
            )
        }
    }
    return bitmap
}

fun Bitmap.cropBitmap(position: Position): Bitmap? {
    val orgWidth = this.width
    val orgHeight = this.height

    val width = orgWidth * (position.bottomRightX - position.topLeftX)
    val height = orgHeight * (position.bottomRightY - position.topLeftY)

    return Bitmap.createBitmap(
        this,
        (orgWidth * position.topLeftX).toInt(),
        (orgHeight * position.topLeftY).toInt(),
        width.toInt(),
        height.toInt()
    )
}
