package com.pixel.toctalk.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: String? = null,
    val lastMessage: ChatMessage? = null,
    val lastTimestamp: Timestamp? = null,
    val usersID: List<String>? = null,
) : Parcelable {
    companion object {
        const val USERS_ID_FIELD = "usersID"
        const val CHAT_ID_FIELD = "id"
        const val LAST_MESSAGE_FIELD = "lastMessage"
        const val LAST_TIMESTAMP_FIELD = "lastTimestamp"
    }
}
