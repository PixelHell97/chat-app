package com.pixel.toctalk.auth.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val username: String,
    val email: String,
) : Parcelable
