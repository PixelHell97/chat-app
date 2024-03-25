package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.allGroups

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.ui.base.BaseViewModel

class BrowseGroupsViewModel : BaseViewModel() {
    val option = MutableLiveData<FirestoreRecyclerOptions<Group>>()

    fun initGroupsRecyclerOption() {
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_GROUPS)

        option.value =
            FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group::class.java)
                .build()
    }

    fun isGroupMember(uid: String, group: Group): Boolean {
        var isMember = true
        for (id in group.membersIdList!!) {
            if (id == uid) {
                isMember = true
                break
            } else {
                isMember = false
            }
        }
        return isMember
    }
}
