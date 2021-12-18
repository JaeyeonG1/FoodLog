package com.boostcampai.foodlog.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.cropBitmap
import com.boostcampai.foodlog.databinding.ItemRecyclerResultFoodBinding
import com.boostcampai.foodlog.model.Food

class ResultFoodRecyclerAdapter(val image: Bitmap, val onRemove: (Int) -> Unit) :
    ListAdapter<Food, ResultFoodRecyclerAdapter.FoodViewHolder>(ResultFoodDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultFoodRecyclerAdapter.FoodViewHolder {
        return FoodViewHolder(
            ItemRecyclerResultFoodBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodViewHolder(val binding: ItemRecyclerResultFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.btnRemove.setOnClickListener { onRemove(bindingAdapterPosition) }
            binding.ivFood.setImageBitmap(image.cropBitmap(food.pos))
            binding.tvName.text = food.name
            binding.tvNutrition.text = food.kcal.toString() + "kcal"
        }
    }
}

private class ResultFoodDiffUtil : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }
}
