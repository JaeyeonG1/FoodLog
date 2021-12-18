package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.convertBitmapToBase64
import com.boostcampai.foodlog.databinding.FragmentConfirmBinding
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

        navArgs.bitmap.let {
            binding.ivConfirm.setImageBitmap(it)
            viewModel.imgBase64 = convertBitmapToBase64(it)
        }

        binding.btnTest.setOnClickListener {
            viewModel.inferenceFromBitmap { diet ->
                val action = ConfirmFragmentDirections.actionConfirmFragmentToResultFragment(
                    navArgs.bitmap, diet
                )
                findNavController().navigate(action)
            }
        }

        binding.btnReset.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
