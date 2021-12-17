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
import com.boostcampai.foodlog.model.BoundingBox
import com.boostcampai.foodlog.model.InferenceResult
import com.boostcampai.foodlog.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private val navArgs: ResultFragmentArgs by navArgs()

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

        val adapter = ResultMultiViewRecyclerAdapter()
        binding.rvResultRoot.adapter = adapter
        adapter.unit = viewModel.unit.value ?: "kcal"
        adapter.goal = viewModel.goal.value ?: 2000

        val foods = navArgs.inferenceResult.foods.map { it.convertToFood(0) }
        val header = ResultItem.Header(
            InferenceResult(
                navArgs.inferenceResult.date.split(" ")[0],
                navArgs.inferenceResult.date.split(" ")[1],
                navArgs.inferenceResult.status,
                foods
            )
        )

        adapter.submitList(
            listOf(
                header,
                ResultItem.Image(navArgs.bitmap.drawBoundingBoxes(navArgs.inferenceResult.foods.map {
                    BoundingBox(
                        it.pos[0],
                        it.pos[1],
                        it.pos[2],
                        it.pos[3]
                    )
                })),
                ResultItem.Foods(foods, navArgs.bitmap)
            )
        )
    }
}
