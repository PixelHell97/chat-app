package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.myGroups

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.ui.base.BaseViewModel

class GroupsViewModel : BaseViewModel() {
    val option = MutableLiveData<FirestoreRecyclerOptions<Group>>()

    fun initGroupsRecyclerOption(uid: String) {
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_GROUPS)
                .whereArrayContains(Group.MEMBER_LIST_FIELD, uid)

        option.value =
            FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group::class.java)
                .build()
    }
}
