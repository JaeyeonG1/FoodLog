package com.boostcampai.foodlog.view

import android.os.Bundle
import android.util.Log
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
import com.boostcampai.foodlog.viewmodel.ConfirmViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
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

        navArgs.bitmap.let {
            val base64 = convertBitmapToBase64(it)
            Log.d("Convert", base64.length.toString())
            viewModel.inferenceFromBitmap(base64)
            binding.ivResult.setImageBitmap(it)
        }

        viewModel.inferenceResult.observe(viewLifecycleOwner, {
            binding.tvTest.text = it.toString()
        })

        binding.btnSave.setOnClickListener {
            viewModel.saveResult()
        }
    }

}
