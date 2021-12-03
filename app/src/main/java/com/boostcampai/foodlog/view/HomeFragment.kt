package com.boostcampai.foodlog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.HomeDietRecyclerAdapter
import com.boostcampai.foodlog.databinding.FragmentHomeBinding
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.Nutrition
import com.boostcampai.foodlog.model.Position
import com.boostcampai.foodlog.model.TodayDietModel
import com.boostcampai.foodlog.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        val adapter = HomeDietRecyclerAdapter()
        binding.rvDiet.adapter = adapter
        adapter.submitList(
            mutableListOf(
                TodayDietModel(
                    "07:20",
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
                ),
                TodayDietModel(
                    "12:30",
                    listOf(
                        Food(0, 0, 0, "된장찌개",
                            Position(0f, 0f, 0f, 0f),
                            Nutrition("kcal", 500.1111f)
                        ),
                        Food(0, 0, 0, "갈비찜",
                            Position(0f, 0f, 0f, 0f),
                            Nutrition("kcal", 300.0f)
                        )
                    )
                )
            )
        )
    }
}
