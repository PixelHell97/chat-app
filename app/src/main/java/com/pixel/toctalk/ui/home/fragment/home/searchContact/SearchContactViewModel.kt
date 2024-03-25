package com.pixel.toctalk.ui.home.fragment.home.searchContact

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.base.BaseViewModel

class SearchContactViewModel : BaseViewModel() {
    val option = MutableLiveData<FirestoreRecyclerOptions<User>>()
    val contactUsername = MutableLiveData<String>()
    val contactUsernameError = MutableLiveData<String?>()
    val isLoading = MutableLiveData(false)
    val state = MutableLiveData<SearchContactState>()

    fun searchContact() {
        if (isLoading.value == true) return
        if (!isValidInput()) return
        isLoading.value = true
        getContactRecyclerOption(contactUsername.value)
    }

    private fun getContactRecyclerOption(username: String?) {
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_USERS)
                .whereEqualTo(User.USERNAME_FIELD, username)

        option.value =
            FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()
        isLoading.value = false
    }

    fun createChatInDB(user: User) {
        val myID = Firebase.auth.currentUser?.uid ?: return
        val newContact = Contact(
            usersID = listOf(myID, user.uid!!),
        )
        MyDatabase
            .createChat(newContact) {
                if (it.isSuccessful) {
                    // TODO: Send the new contact with id
                    state.value = SearchContactState.NavToChat(newContact)
                }
            }
    }

    private fun isValidInput(): Boolean {
        var isValid = true
        if (contactUsername.value.isNullOrEmpty()) {
            contactUsernameError.value = "Enter contact username"
            isValid = false
        } else {
            contactUsernameError.value = null
        }
        return isValid
    }
}
