package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.DailyFoodRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentDailyBinding
import com.boostcampai.foodlog.viewmodel.DailyViewModel

class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private val viewModel: DailyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val adapter = DailyFoodRecyclerAdapter()
        binding.rvDaily.adapter = adapter

    }
}