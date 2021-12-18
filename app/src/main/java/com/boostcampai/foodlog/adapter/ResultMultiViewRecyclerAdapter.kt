package com.boostcampai.foodlog.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.databinding.ItemRecyclerResultFoodsBinding
import com.boostcampai.foodlog.databinding.ItemRecyclerResultHeaderBinding
import com.boostcampai.foodlog.databinding.ItemRecyclerResultImageBinding
import com.boostcampai.foodlog.model.Food
import com.boostcampai.foodlog.model.InferenceResult
import com.boostcampai.foodlog.model.subUnits
import com.boostcampai.foodlog.model.total

class ResultMultiViewRecyclerAdapter(val onRemove: (Int) -> Unit) :
    ListAdapter<ResultItem, RecyclerView.ViewHolder>(ResultDiffUtil()) {
    var goal = 2000
    var unit = "kcal"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ResultViewType.values()[viewType]) {
            ResultViewType.HEADER -> {
                ResultHeaderViewHolder(
                    ItemRecyclerResultHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ResultViewType.IMAGE -> {
                ResultImageViewHolder(
                    ItemRecyclerResultImageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ResultViewType.FOODS -> {
                ResultFoodsViewHolder(
                    ItemRecyclerResultFoodsBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is ResultItem.Header -> {
                (holder as ResultHeaderViewHolder).bind(getItem(position) as ResultItem.Header)
            }
            is ResultItem.Image -> {
                (holder as ResultImageViewHolder).bind(getItem(position) as ResultItem.Image)
            }
            is ResultItem.Foods -> {
                (holder as ResultFoodsViewHolder).bind(getItem(position) as ResultItem.Foods)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ResultItem.Header -> 0
            is ResultItem.Image -> 1
            is ResultItem.Foods -> 2
        }
    }

    inner class ResultHeaderViewHolder(val binding: ItemRecyclerResultHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultItem.Header) {
            binding.item = result.result
            "${result.result.foods.total(unit).toInt()} $unit / $goal $unit"
                .also { binding.tvNutritionMain.text = it }

            result.result.foods.subUnits(unit).also { trp ->
                binding.tvNutritionSub1Title.text = trp.first[0]
                "${trp.second[0].toInt()}${trp.third[0]}".also { binding.tvNutritionSub1.text = it }
                binding.tvNutritionSub2Title.text = trp.first[1]
                "${trp.second[1].toInt()}${trp.third[1]}".also { binding.tvNutritionSub2.text = it }
                binding.tvNutritionSub3Title.text = trp.first[2]
                "${trp.second[2].toInt()}${trp.third[2]}".also { binding.tvNutritionSub3.text = it }
                binding.tvNutritionSub4Title.text = trp.first[3]
                "${trp.second[3].toInt()}${trp.third[3]}".also { binding.tvNutritionSub4.text = it }
            }
        }
    }

    inner class ResultImageViewHolder(val binding: ItemRecyclerResultImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ResultItem.Image) {
            binding.ivResult.setImageBitmap(image.bitmap)
        }
    }

    inner class ResultFoodsViewHolder(val binding: ItemRecyclerResultFoodsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var adapter: ResultFoodRecyclerAdapter
        fun bind(foods: ResultItem.Foods) {
            adapter = ResultFoodRecyclerAdapter(foods.image, onRemove)
            binding.rvFoods.adapter = adapter
            adapter.submitList(foods.foods)
        }
    }
}

private class ResultDiffUtil : DiffUtil.ItemCallback<ResultItem>() {
    override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
        return oldItem == newItem
    }
}

sealed class ResultItem {
    data class Header(val result: InferenceResult) : ResultItem()
    data class Image(val bitmap: Bitmap) : ResultItem()
    data class Foods(val foods: List<Food>, val image: Bitmap) : ResultItem()
}

enum class ResultViewType {
    HEADER, IMAGE, FOODS
}
