package com.pixel.toctalk.ui.home.fragment.account

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pixel.toctalk.data.database.UserMdb
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.base.BaseViewModel

class EditAccountViewModel : BaseViewModel() {
    val user = MutableLiveData<User>()
    val userCurrentPic = MutableLiveData<String>()
    val userNewPic = MutableLiveData<Uri>(null)
    val usernameLiveData = MutableLiveData<String>()
    val usernameError = MutableLiveData<String?>()
    val isLoading = MutableLiveData(false)
    private val _event = MutableLiveData<EditAccountEvent>()
    val event: LiveData<EditAccountEvent> = _event

    fun onSaveClick() {
        if (isLoading.value == true) return
        if (!isValid()) return
        isLoading.value = true
        if (user.value?.username != usernameLiveData.value && userNewPic.value != null) {
            UserMdb.updateUserData(
                user.value?.uid!!,
                usernameLiveData.value!!,
                userNewPic.value!!,
            ) {
                isLoading.value = false
                if (it.isSuccessful) {
                    _event.value = EditAccountEvent.SUCCESSFUL
                } else {
                    _event.value = EditAccountEvent.UNSUCCESSFUL
                }
            }
        } else if (user.value?.username != usernameLiveData.value) {
            UserMdb.updateUsernameOnly(
                user.value?.uid!!,
                usernameLiveData.value!!,
            ) {
                isLoading.value = false
                if (it.isSuccessful) {
                    _event.value = EditAccountEvent.SUCCESSFUL
                } else {
                    _event.value = EditAccountEvent.UNSUCCESSFUL
                }
            }
        } else {
            UserMdb.updateUserPicOnly(
                user.value?.uid!!,
                userNewPic.value!!,
            ) {
                isLoading.value = false
                if (it.isSuccessful) {
                    _event.value = EditAccountEvent.SUCCESSFUL
                } else {
                    _event.value = EditAccountEvent.UNSUCCESSFUL
                }
            }
        }
    }

    fun setProfilePic(imageUri: Uri?) {
        userNewPic.value = imageUri!!
    }

    private fun isValid(): Boolean {
        var isValid = true
        if (!(isValidInput(usernameLiveData.value))) isValid = false
        return isValid
    }

    fun isValidInput(input: CharSequence?): Boolean {
        var isValid = true
        if (input.isNullOrEmpty()) {
            usernameError.value = "Username required"
            isValid = false
        } else {
            usernameError.value = null
        }
        return isValid
    }
}
