package com.pixel.toctalk.data.model

import android.os.Parcelable
import com.pixel.toctalk.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String? = null,
    var profilePic: String? = Constants.DEFAULT_USER_URI,
    val username: String? = null,
    val email: String? = null,
) : Parcelable {
    companion object {
        const val UID = "uid"
        const val USERNAME_FIELD = "username"
    }
}
