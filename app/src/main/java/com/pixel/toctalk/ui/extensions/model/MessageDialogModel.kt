package com.pixel.toctalk.ui.extensions.model

data class MessageDialogModel(
    val message: String,
    val posActionName: String? = null,
    val posAction: (() -> Unit)? = null,
    val negActionName: String? = null,
    val negAction: (() -> Unit)? = null,
    val isCancelable: Boolean = true,
)
