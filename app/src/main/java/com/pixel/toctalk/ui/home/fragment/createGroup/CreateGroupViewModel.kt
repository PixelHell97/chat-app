package com.pixel.toctalk.ui.home.fragment.createGroup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.ui.base.BaseViewModel
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel

class CreateGroupViewModel : BaseViewModel() {
    val groupPicLiveData = MutableLiveData<Uri>(null)
    val groupNameLiveData = MutableLiveData<String>()
    val groupNameError = MutableLiveData<String?>()
    val groupDescLiveData = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    val state = MutableLiveData<CreateGroupStates>()

    fun createGroup() {
        if (isLoading.value == true) return
        if (!isValid()) return
        isLoading.value = true
        val user = Firebase.auth.currentUser ?: return
        val newGroup = Group(
            name = groupNameLiveData.value,
            description = groupDescLiveData.value,
            ownerID = user.uid,
            membersIdList = listOf(user.uid),
        )
        createGroupInDB(newGroup)
    }

    private fun createGroupInDB(newGroup: Group) {
        MyDatabase
            .createGroup(newGroup) { task ->
                if (task.isSuccessful) {
                    if (groupPicLiveData.value != null) {
                        uploadGroupPic(task.result.id, groupPicLiveData.value, newGroup)
                    } else {
                        updateGroupInDB(task.result.id, null, newGroup)
                    }
                } else {
                    isLoading.value = false
                    _messageDialogModelDialog.value = MessageDialogModel(
                        message = task.exception?.localizedMessage
                            ?: "Unable to create group\nPlease try again",
                    )
                }
            }
    }

    private fun uploadGroupPic(id: String, imageUri: Uri?, newGroup: Group) {
        MyDatabase.uploadGroupPic(id, imageUri) { task ->
            updateGroupInDB(id, task.result.toString(), newGroup)
        }
    }

    private fun updateGroupInDB(id: String, imageUrl: String?, newGroup: Group) {
        if (imageUrl != null) {
            MyDatabase
                .updateGroup(id, imageUrl) { task ->
                    if (task.isSuccessful) {
                        isLoading.value = false
                        val updatedGroup = newGroup.copy(id = id, groupPic = imageUrl)
                        state.value = CreateGroupStates.GroupCreated(updatedGroup)
                    } else {
                        isLoading.value = false
                        _messageDialogModelDialog.value = MessageDialogModel(
                            message = task.exception?.localizedMessage
                                ?: "Please try again",
                        )
                    }
                }
        } else {
            MyDatabase
                .updateGroup(id, newGroup.groupPic!!) { task ->
                    if (task.isSuccessful) {
                        isLoading.value = false
                        val updatedGroup = newGroup.copy(id = id)
                        state.value = CreateGroupStates.GroupCreated(updatedGroup)
                    } else {
                        isLoading.value = false
                        _messageDialogModelDialog.value = MessageDialogModel(
                            message = task.exception?.localizedMessage
                                ?: "Please try again",
                        )
                    }
                }
        }
    }

    private fun isValid(): Boolean {
        var isValid = true
        if (!(isValidGroupName(groupNameLiveData.value))) isValid = false
        return isValid
    }

    fun isValidGroupName(name: CharSequence?): Boolean {
        var isValid = true
        if (name.isNullOrEmpty()) {
            groupNameError.value = "Group name required"
            isValid = false
        } else {
            groupNameError.value = null
        }
        return isValid
    }

    fun setGroupPic(imageUri: Uri) {
        groupPicLiveData.value = imageUri
    }
}
