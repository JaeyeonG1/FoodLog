package com.boostcampai.foodlog.view

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.boostcampai.foodlog.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context) {
    lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setCancelable(false)
    }
}
