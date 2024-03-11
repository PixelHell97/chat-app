package com.pixel.toctalk.auth

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorMessage")
fun setErrorOnTextInputLayout(
    layout: TextInputLayout,
    errorMessage: String?,
) {
    layout.error = errorMessage
}
