package com.boostcampai.foodlog.adapter

import android.content.Context
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boostcampai.foodlog.R

@BindingAdapter(value = ["base_unit", "context"], requireAll = true)
fun buildBadgeText(textView: TextView, unit: String, context: Context) {
    textView.text = context.getString(R.string.badge_string, unit)
}

@BindingAdapter(value = ["current", "goal", "unit", "context"], requireAll = true)
fun buildHomeText(textView: TextView, current: Int, goal: Int, unit:String, context: Context) {
    textView.text = context.getString(R.string.home_string, current, goal, unit)
}
