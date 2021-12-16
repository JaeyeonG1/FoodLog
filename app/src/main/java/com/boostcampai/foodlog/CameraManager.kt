package com.boostcampai.foodlog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
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
        imageCapture = ImageCapture.Builder()
            .setTargetResolution(Size(metrics.widthPixels, metrics.heightPixels))
            .build()
    }

    fun startCamera() {
        ProcessCameraProvider.getInstance(context).apply {
            addListener(
                {
                    cameraProvider = get()
                    preview = Preview.Builder().build()

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
                    onCapture(image.toBitmap())
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("takePhoto", "Photo Capture Failed: ${exception.message}")
                }
            })
    }

    fun ImageProxy.toBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
