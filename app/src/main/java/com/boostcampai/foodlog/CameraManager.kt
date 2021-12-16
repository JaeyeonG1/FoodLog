package com.boostcampai.foodlog

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Surface.ROTATION_0
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class CameraManager(
    private val context: Context,
    private val cameraView: PreviewView,
    private val onCapture: (Bitmap) -> (Unit)
) {
    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture
    private lateinit var camera: Camera
    private lateinit var cameraProvider: ProcessCameraProvider

    lateinit var metrics: DisplayMetrics

    private var cameraFacingOption = CameraSelector.LENS_FACING_BACK

    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(
                cameraView.surfaceProvider
            )
        } catch (e: Exception) {
            Log.e("CameraManager", e.toString())
        }
    }

    private fun buildImageCapture() {
        val rotation = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            context.display
        } else {
            (context as Activity).windowManager.defaultDisplay
        }?.rotation ?: ROTATION_0

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(rotation)
            .setTargetResolution(Size(cameraView.width, cameraView.height))
            .build()
    }

    fun startCamera() {
        ProcessCameraProvider.getInstance(context).apply {
            addListener(
                {
                    cameraProvider = get()
                    preview = Preview.Builder()
                        .setTargetResolution(Size(cameraView.width, cameraView.height))
                        .build()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(cameraFacingOption)
                        .build()

                    metrics = DisplayMetrics().also {
                        cameraView.display.getRealMetrics(it)
                    }

                    buildImageCapture()
                    setCameraConfig(cameraProvider, cameraSelector)

                }, ContextCompat.getMainExecutor(context)
            )
        }
    }

    fun takePhoto() {
        if (!this::imageCapture.isInitialized)
            return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d("Rotation", camera.cameraInfo.sensorRotationDegrees.toString())

                    onCapture(image.toBitmap(camera.cameraInfo.sensorRotationDegrees))
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("takePhoto", "Photo Capture Failed: ${exception.message}")
                }
            })
    }

    fun ImageProxy.toBitmap(rotation: Int): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val source = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            Matrix().apply { postRotate(rotation.toFloat()) },
            true
        )
    }
}
