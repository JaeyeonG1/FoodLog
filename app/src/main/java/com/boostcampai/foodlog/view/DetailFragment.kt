package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.DetailDietRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentDetailBinding
import com.boostcampai.foodlog.model.DailyDietModel
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Nutrition
import com.boostcampai.foodlog.model.Position
import com.boostcampai.foodlog.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        val adapter = DetailDietRecyclerAdapter()
        binding.rvDetail.adapter = adapter
        adapter.submitList(
            mutableListOf(
                DailyDietModel(
                    "12월 14일",
                    3200,
                    listOf(
                        Food(0, 0, 0, "김치찌개",
                            Position(0f, 0f, 0f, 0f),
                            Nutrition("kcal", 170f)
                        ),
                        Food(0, 0, 0, "꽁치조림",
                            Position(0f, 0f, 0f, 0f),
                            Nutrition("kcal", 300f)
                        )
                    )
                )
            )
        )
    }

}
