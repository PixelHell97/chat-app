package com.pixel.toctalk.ui.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorMessage")
fun setErrorOnTextInputLayout(
    layout: TextInputLayout,
    errorMessage: String?,
) {
    layout.error = errorMessage
}

@BindingAdapter("imageUrl")
fun loadImageFromUrl(
    imageView: ImageView,
    imageUrl: String?,
) {
    Glide
        .with(imageView)
        .load(imageUrl)
        .into(imageView)
}
