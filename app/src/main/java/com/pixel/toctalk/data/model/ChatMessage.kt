package com.pixel.toctalk.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class ChatMessage(
    val uid: String? = null,
    val content: String? = null,
    val timestamp: Timestamp? = null,
    val sender: User? = null,
) : Parcelable {

    fun getFormattedTime(): String {
        val date = Date(timestamp?.toDate()?.time!!)
        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)
        return timeFormatter.format(date)
    }

    companion object {
        const val TIME_STAMP = "timestamp"
    }
}
