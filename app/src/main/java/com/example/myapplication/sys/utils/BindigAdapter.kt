package com.example.myapplication.sys.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R

@BindingAdapter("loadimage")
fun AppCompatImageView.bindingImage(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        this.visibility = this.rootView.visibility
        val options: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .placeholder(R.drawable.img_4)

        Glide.with(this.context)
            .load(imageUrl)
            .apply(options)
            .into(this)
    } else {
        this.rootView.isGone
    }
}