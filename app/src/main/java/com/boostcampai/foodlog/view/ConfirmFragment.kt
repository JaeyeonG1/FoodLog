package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.databinding.FragmentConfirmBinding
import com.boostcampai.foodlog.viewmodel.ConfirmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding
    private val viewModel: ConfirmViewModel by viewModels()

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

        viewModel.inferenceResult.observe(viewLifecycleOwner, {
            binding.tvTest.text = it.toString()
        })

        binding.btnTest.setOnClickListener {
            viewModel.inferenceResult()
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveResult()
        }
    }

}
