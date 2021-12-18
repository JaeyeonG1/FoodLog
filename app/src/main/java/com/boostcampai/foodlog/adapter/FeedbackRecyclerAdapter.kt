package com.boostcampai.foodlog.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.cropBitmap
import com.boostcampai.foodlog.databinding.ItemRecyclerFeedbackBinding
import com.boostcampai.foodlog.model.Food

class FeedbackRecyclerAdapter(val image: Bitmap, val onClick: (Int) -> (Unit)) :
    ListAdapter<Pair<Food, Boolean>, FeedbackRecyclerAdapter.FoodImageViewHolder>(FeedbackDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodImageViewHolder {
        return FoodImageViewHolder(
            ItemRecyclerFeedbackBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodImageViewHolder(val binding: ItemRecyclerFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<Food, Boolean>) {
            if (!item.second)
                binding.ivFeedbackItem.setBackgroundColor(Color.parseColor("#DE635F"))
            else
                binding.ivFeedbackItem.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.ivFeedbackItem.setImageBitmap(image.cropBitmap(item.first.pos))
            binding.root.setOnClickListener {
                onClick(bindingAdapterPosition)
            }
        }
    }
}

private class FeedbackDiffUtil : DiffUtil.ItemCallback<Pair<Food, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<Food, Boolean>,
        newItem: Pair<Food, Boolean>
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Pair<Food, Boolean>,
        newItem: Pair<Food, Boolean>
    ): Boolean {
        return oldItem == newItem
    }
}
