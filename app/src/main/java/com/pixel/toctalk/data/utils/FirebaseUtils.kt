package com.pixel.toctalk.data.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseUtils {
    suspend fun getCurrentUserID(): String? =
        Firebase.auth.currentUser?.uid

    suspend fun isLoggedIn(): Boolean {
        return (getCurrentUserID() != null)
    }
}
