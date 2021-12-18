package com.boostcampai.foodlog.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.FeedbackRecyclerAdapter
import com.boostcampai.foodlog.databinding.DialogFragmentFeedbackBinding
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.viewmodel.FeedbackViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackDialogFragment(
    private val image: Bitmap,
    private val foods: List<Food>,
    val onComplete: (List<Boolean>) -> (Unit)
) :
    DialogFragment() {
    lateinit var binding: DialogFragmentFeedbackBinding
    private val viewModel: FeedbackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_feedback, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.loadFoods(foods)

        val adapter = FeedbackRecyclerAdapter(image) {
            if (it != -1)
                viewModel.applyStatus(it)
        }
        binding.rvFeedback.adapter = adapter

        viewModel.foodWithStatus.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        binding.btnSend.setOnClickListener {
            viewModel.foodWithStatus.value?.map { it.second }?.let { result -> onComplete(result) }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        requireDialog().window?.attributes = requireDialog().window?.attributes?.apply {
            width = (size.x * 0.9).toInt()
            height = (size.y * 0.7).toInt()
        }
    }
}
