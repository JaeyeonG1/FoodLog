package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.DetailDietRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentDetailBinding
import com.boostcampai.foodlog.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        val adapter = DetailDietRecyclerAdapter {
            val action = DetailFragmentDirections.actionDetailFragmentToDailyFragment()
            navController.navigate(action)

        }
        binding.rvDetail.adapter = adapter

        viewModel.dietWithFoods.observe(viewLifecycleOwner, {})
        viewModel.dailyDietModels.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

    }

}
