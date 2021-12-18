package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.ResultItem
import com.boostcampai.foodlog.adapter.ResultMultiViewRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentResultBinding
import com.boostcampai.foodlog.drawBoundingBoxes
import com.boostcampai.foodlog.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private val navArgs: ResultFragmentArgs by navArgs()
    private lateinit var adapter: ResultMultiViewRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.loadInferenceResult(navArgs.inferenceResult)

        adapter = ResultMultiViewRecyclerAdapter { pos -> viewModel.removeFood(pos) }
        setRecyclerAdapter()
        initObservers()
    }

    private fun setRecyclerAdapter() {
        binding.rvResultRoot.adapter = adapter
        adapter.unit = viewModel.unit.value ?: "kcal"
        adapter.goal = viewModel.goal.value ?: 2000

        val header = viewModel.inferenceResult.value?.let { ResultItem.Header(it) }
        val foods = viewModel.inferenceResult.value?.foods ?: listOf()

        adapter.submitList(
            listOf(
                header,
                ResultItem.Image(navArgs.bitmap.drawBoundingBoxes(foods.map { it.pos })),
                ResultItem.Foods(foods, navArgs.bitmap)
            )
        )
    }

    private fun initObservers() {
        viewModel.goal.observe(viewLifecycleOwner, { setRecyclerAdapter() })
        viewModel.unit.observe(viewLifecycleOwner, { setRecyclerAdapter() })
        viewModel.inferenceResult.observe(viewLifecycleOwner, { setRecyclerAdapter() })
    }
}
