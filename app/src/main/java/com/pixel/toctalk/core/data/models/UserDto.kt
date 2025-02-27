package com.pixel.toctalk.core.data.models

import com.google.firebase.Timestamp

data class UserDto(
    val uid: String,
    val displayName: String,
    val emailAddress: String,
    val phoneNumber: String?,
    val profilePic: String?,
    val bio: String?,
    val friendsList: List<String>?,
    val friendsRequestList: List<String>?,
    val isOnline: Boolean,
    val isEmailVerified: Boolean,
    val isPhoneNumberVerified: Boolean,
    val createdAt: Timestamp,
)
