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
import com.boostcampai.foodlog.adapter.DetailDietRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentDailyBinding
import com.boostcampai.foodlog.model.DailyDietModel
import com.boostcampai.foodlog.viewmodel.DailyViewModel
import androidx.navigation.fragment.navArgs

class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private val viewModel: DailyViewModel by viewModels()
    private val navArgs: DailyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedFoods = navArgs.foods

        binding.viewModel = viewModel

        viewModel.initDailyFoods(selectedFoods.toList())
        val adapter = DailyFoodRecyclerAdapter {
            it.getValueByUnit(viewModel.unit.value ?: "kcal").toString() + viewModel.unit.value
        }
        binding.rvDaily.adapter = adapter
        adapter.submitList(viewModel.dailyFoods)

    }
}