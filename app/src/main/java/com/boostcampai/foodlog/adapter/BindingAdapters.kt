package com.boostcampai.foodlog.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.cropBitmap
import com.boostcampai.foodlog.model.Position
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@BindingAdapter(value = ["base_unit", "context"], requireAll = true)
fun buildBadgeText(textView: TextView, unit: String, context: Context) {
    textView.text = context.getString(R.string.badge_string, unit)
}

@BindingAdapter(value = ["current", "goal", "unit", "context"], requireAll = true)
fun buildHomeText(textView: TextView, current: Int, goal: Int, unit: String, context: Context) {
    textView.text = context.getString(R.string.home_string, current, goal, unit)
}

@BindingAdapter(value = ["uri", "pos"], requireAll = true)
fun loadImage(imageView: ImageView, uri: String?, pos: Position) {
    Log.d("BindingAdapterPos", pos.toString())
    uri ?: return
    var bitmap: Bitmap? = null
    CoroutineScope(Dispatchers.IO).launch {
        if (URLUtil.isValidUrl(uri))
            bitmap = Glide.with(imageView.context)
            .asBitmap()
            .load(uri)
            .submit()
            .get()
        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(imageView.context)
                .load(bitmap?.cropBitmap(pos))
                .error(R.drawable.ic_baseline_settings_24)
                .into(imageView)
        }
    }

    imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
}
