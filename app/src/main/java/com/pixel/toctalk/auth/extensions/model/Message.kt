package com.pixel.toctalk.auth.extensions.model

data class Message(
    val title: String,
    val message: String,
    val posActionName: String? = null,
    val posAction: (() -> Unit)? = null,
    val negActionName: String? = null,
    val negAction: (() -> Unit)? = null,
    val isCancelable: Boolean = true,
)
