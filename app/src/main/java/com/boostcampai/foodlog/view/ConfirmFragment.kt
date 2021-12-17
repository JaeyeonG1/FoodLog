package com.boostcampai.foodlog.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.convertBitmapToBase64
import com.boostcampai.foodlog.databinding.FragmentConfirmBinding
import com.boostcampai.foodlog.model.BoundingBox
import com.boostcampai.foodlog.viewmodel.ConfirmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding
    private val viewModel: ConfirmViewModel by viewModels()
    private val navArgs: ConfirmFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.inferenceResult.observe(viewLifecycleOwner, {})
        viewModel.boundingBoxes.observe(viewLifecycleOwner, {
            binding.ivResult.setImageBitmap(navArgs.bitmap.drawBoundingBoxes(it))
        })

        navArgs.bitmap.let {
            binding.ivResult.setImageBitmap(it)
            viewModel.imgBase64 = convertBitmapToBase64(it)
        }

        binding.btnTest.setOnClickListener {
            viewModel.inferenceFromBitmap()
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveResult()
        }
    }

    private fun Bitmap.drawBoundingBoxes(boundingBoxes: List<BoundingBox>): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            color = Color.RED
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 5f

            boundingBoxes.forEach {
                canvas.drawRect(
                    width * it.left,
                    height * it.top,
                    width * it.right,
                    height * it.bottom,
                    this
                )
            }
        }

        return bitmap
    }
}
