package com.boostcampai.foodlog.view

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.boostcampai.foodlog.CameraManager
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.databinding.FragmentCameraBinding
import com.boostcampai.foodlog.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraManager: CameraManager
    private val viewModel: CameraViewModel by viewModels()

    private val cameraRequestCode = 100

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        loadingDialog = LoadingDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        createCameraManager {
            val action = CameraFragmentDirections.actionCameraFragmentToConfirmFragment(it)
            findNavController().navigate(action)
        }
        binding.cameraManager = cameraManager

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loadingDialog.show()
                if (it.resultCode == RESULT_OK && it.data != null) {
                    Log.d("result", "RESULT_OK")
                    var currentImageUri = it.data?.data
                    try {
                        currentImageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    currentImageUri
                                ).copy(Bitmap.Config.ARGB_8888, true)
                                // 여기서 bitmap 초기화
                                val action =
                                    CameraFragmentDirections.actionCameraFragmentToConfirmFragment(
                                        bitmap
                                    )
                                findNavController().navigate(action)

                            } else {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    currentImageUri
                                )
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                    .copy(Bitmap.Config.ARGB_8888, true)
                                val action =
                                    CameraFragmentDirections.actionCameraFragmentToConfirmFragment(
                                        bitmap
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("exception", "exception")
                        e.printStackTrace()
                    }
                } else if (it.resultCode == RESULT_CANCELED) {
                    Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("wrong", "")
                }
                loadingDialog.dismiss()
            }
        binding.btnGallery.setOnClickListener {
            selectImageFromGallery()
        }
    }

    override fun onResume() {
        super.onResume()

        if (cameraPermissionsGranted)
            binding.pvCamera.post { cameraManager.startCamera() }
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                100
            )
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)
    }

    private fun createCameraManager(onCapture: (Bitmap) -> Unit) {
        cameraManager = CameraManager(requireActivity(), binding.pvCamera) {
            onStartWork()
            onCapture(it)
            onFinishWork()
        }
    }

    private fun onStartWork() {
        loadingDialog.show()
        binding.btnPicture.isEnabled = false
        binding.btnGallery.isEnabled = false
    }

    private fun onFinishWork() {
        binding.btnPicture.isEnabled = true
        binding.btnGallery.isEnabled = false
        loadingDialog.dismiss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == cameraRequestCode) {
            if (cameraPermissionsGranted)
                cameraManager.startCamera()
            else
                findNavController().popBackStack()
        }
    }

    private val cameraPermissionsGranted
        get() =
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
}
