package com.boostcampai.foodlog.view

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.DailyFoodRecyclerAdapter
import com.boostcampai.foodlog.cropBitmap
import com.boostcampai.foodlog.databinding.FragmentDailyBinding
import com.boostcampai.foodlog.model.Position
import com.boostcampai.foodlog.viewmodel.DailyViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.io.File
import java.security.MessageDigest


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
        viewModel.dailyFoods = mutableListOf()

        viewModel.initDailyFoods(selectedFoods.toList())
        val adapter = DailyFoodRecyclerAdapter({ food ->
            food.getValueByUnit(viewModel.unit.value ?: "kcal").toString() + viewModel.unit.value
        }, { uri, pos, view -> uriStringToBitmap(uri, pos, view) }, requireContext())
        binding.rvDaily.adapter = adapter
        Log.d("Result", viewModel.dailyFoods.size.toString())
        adapter.submitList(viewModel.dailyFoods)
    }

    private fun uriStringToBitmap(uriString: String, position: Position, view: ImageView) {
        val uri = Uri.parse(uriString)

        Glide.with(requireContext())
            .load(uri)
            .transform(PositionCropper(position))
            .into(view)
    }
}

class PositionCropper(val position: Position) : BitmapTransformation() {
    override fun transform(
        pool: BitmapPool,
        original: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? = original.cropBitmap(position)

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}
