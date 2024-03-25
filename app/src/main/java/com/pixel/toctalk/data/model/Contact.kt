package com.pixel.toctalk.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: String? = null,
    val lastMessage: ChatMessage? = null,
    val usersID: List<String>? = null,
) : Parcelable {
    companion object {
        const val USERS_ID_FIELD = "usersID"
        const val CHAT_ID_FIELD = "id"
        const val LAST_MESSAGE_FIELD = "lastMessage"
    }
}
