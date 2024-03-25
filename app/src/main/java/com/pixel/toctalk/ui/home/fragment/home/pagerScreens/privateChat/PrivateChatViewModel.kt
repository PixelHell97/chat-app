package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.privateChat

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.ui.base.BaseViewModel

class PrivateChatViewModel : BaseViewModel() {
    val option = MutableLiveData<FirestoreRecyclerOptions<Contact>>()

    fun getContactsRecyclerOption() {
        val myID = Firebase.auth.currentUser?.uid ?: return
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_PRIVATE_CHAT)
                .whereArrayContains(Contact.USERS_ID_FIELD, myID)

        option.value =
            FirestoreRecyclerOptions.Builder<Contact>()
                .setQuery(query, Contact::class.java)
                .build()
    }
}
