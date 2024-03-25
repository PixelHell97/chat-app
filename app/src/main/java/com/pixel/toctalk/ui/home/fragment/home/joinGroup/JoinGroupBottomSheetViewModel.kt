package com.pixel.toctalk.ui.home.fragment.home.joinGroup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.Group

class JoinGroupBottomSheetViewModel : ViewModel() {
    val state = MutableLiveData<JoinGroupState>()
    fun addMemberToGroup(group: Group) {
        val user = Firebase.auth.currentUser ?: return
        val newGroupMemberList = group.membersIdList?.toMutableList()
        newGroupMemberList?.add(user.uid)
        MyDatabase.addMemberToGroup(group.id!!, newGroupMemberList) { task ->
            if (task.isSuccessful) {
                state.value = JoinGroupState.NavigateToGroupChat(group)
            } else {
                // 
            }
        }
    }
}
