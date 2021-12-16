package com.boostcampai.foodlog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boostcampai.foodlog.databinding.ItemRecyclerDetailDietBinding
import com.boostcampai.foodlog.model.DailyDietModel
import com.boostcampai.foodlog.model.Food

class DetailDietRecyclerAdapter(val onClick: (String) -> Unit, val calcNutrition: (List<Food>) -> String) :
    ListAdapter<DailyDietModel, RecyclerView.ViewHolder>(DetailDietDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DailyDietViewHolder(
            ItemRecyclerDetailDietBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as DailyDietViewHolder).bind(item)
    }

    inner class DailyDietViewHolder(private val binding: ItemRecyclerDetailDietBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyDietModel) {
            binding.item = item
            binding.setOnClickListener {
                onClick(item.date)
                Log.d("adapter date", item.date)
            }
            binding.tvValue.text = calcNutrition(item.foods)
        }
    }

}

private class DetailDietDiffUtil : DiffUtil.ItemCallback<DailyDietModel>() {
    override fun areItemsTheSame(oldItem: DailyDietModel, newItem: DailyDietModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DailyDietModel, newItem: DailyDietModel): Boolean {
        return oldItem == newItem
    }
}
