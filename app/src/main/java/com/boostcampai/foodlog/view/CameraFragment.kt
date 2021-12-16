package com.boostcampai.foodlog.view

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
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

    private fun createCameraManager(onCapture: (Bitmap) -> Unit) {
        cameraManager = CameraManager(requireActivity(), binding.pvCamera) {
            onCapture(it)
        }
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
