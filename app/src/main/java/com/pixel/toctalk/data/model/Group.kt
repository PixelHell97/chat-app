package com.pixel.toctalk.data.model

import android.os.Parcelable
import com.pixel.toctalk.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: String? = null,
    val groupPic: String? = Constants.DEFAULT_GROUP_URI,
    val name: String? = null,
    val description: String? = null,
    val ownerID: String? = null,
    val membersIdList: List<String>? = null,
) : Parcelable {

    fun getMembersCount(): Int = membersIdList?.size ?: 0

    companion object {
        const val UID_FIELD = "id"
        const val GROUP_PIC_FIELD = "groupPic"
        const val MEMBER_LIST_FIELD = "membersIdList"
    }
}
