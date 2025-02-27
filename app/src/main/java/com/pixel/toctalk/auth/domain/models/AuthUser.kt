package com.pixel.toctalk.auth.domain.models

import android.net.Uri

data class AuthUser(
    val uid: String? = null,
    val profilePic: Uri? = null,
    val displayName: String? = null,
    val email: String? = null,
    val isEmailVerified: Boolean = false,
)
