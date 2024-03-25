package com.pixel.toctalk.ui.home.fragment.groupDetails

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.base.BaseViewModel

class GroupDetailsViewModel : BaseViewModel() {
    val option = MutableLiveData<FirestoreRecyclerOptions<User>>()

    fun getGroupMembersOption(group: Group) {
        val query = Firebase
            .firestore
            .collection(Constants.COLLECTION_USERS)
            .whereIn(User.UID, group.membersIdList!!)

        option.value =
            FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()
    }
}
