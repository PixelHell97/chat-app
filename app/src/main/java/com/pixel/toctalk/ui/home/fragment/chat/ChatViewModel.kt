package com.pixel.toctalk.ui.home.fragment.chat

import androidx.lifecycle.MutableLiveData
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.ui.base.BaseViewModel

class ChatViewModel : BaseViewModel() {
    val messageContent = MutableLiveData<String>()
    val option = MutableLiveData<FirestoreRecyclerOptions<ChatMessage>>()
    val contact = MutableLiveData<Contact>()
    val group = MutableLiveData<Group>()
    val messageState = MutableLiveData<MessageState>()

    fun send() {
        if (!isValidMessage()) return
        when (messageState.value) {
            MessageState.CONTACT -> {
                sendMessageInContact(contact.value)
            }

            MessageState.GROUP -> {
                sendMessageInGroup(group.value)
            }

            null -> return
        }
    }

    private fun sendMessageInContact(contact: Contact?) {
        val currentUser = Firebase.auth.currentUser ?: return
        MyDatabase.getUser(currentUser.uid) { task ->
            if (task.isSuccessful) {
                val user = task.result.toObject(User::class.java)
                val message = ChatMessage(
                    content = messageContent.value,
                    sender = user,
                    timestamp = Timestamp.now(),
                )
                MyDatabase.sendMessage(
                    contact?.id,
                    Constants.COLLECTION_PRIVATE_CHAT,
                    message,
                ) { messageTask ->
                    if (messageTask.isSuccessful) {
                        messageContent.value = ""
                        updateMessageWithId(
                            messageTask.result.id,
                            contact?.id!!,
                            Constants.COLLECTION_PRIVATE_CHAT,
                        )
                    }
                }
            }
        }
    }

    private fun sendMessageInGroup(group: Group?) {
        val currentUser = Firebase.auth.currentUser ?: return
        MyDatabase.getUser(currentUser.uid) { task ->
            if (task.isSuccessful) {
                val user = task.result.toObject(User::class.java)
                val message = ChatMessage(
                    content = messageContent.value,
                    sender = user,
                    timestamp = Timestamp.now(),
                )
                MyDatabase.sendMessage(
                    group?.id,
                    Constants.COLLECTION_GROUPS,
                    message,
                ) { messageTask ->
                    if (messageTask.isSuccessful) {
                        messageContent.value = ""
                        updateMessageWithId(
                            messageTask.result.id,
                            group?.id!!,
                            Constants.COLLECTION_GROUPS,
                        )
                    }
                }
            }
        }
    }

    private fun updateMessageWithId(id: String, docID: String, collection: String) {
        MyDatabase.updateMessage(id, docID, collection) { task ->
            if (task.isSuccessful) {
                MyDatabase.updateLastMessage(id, docID, collection)
            }
        }
    }

    fun initGroupChatFireStoreOption() {
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_GROUPS)
                .document(group.value?.id!!)
                .collection(Constants.COLLECTION_MESSAGES)
                .orderBy(ChatMessage.TIME_STAMP, Query.Direction.DESCENDING)

        option.value =
            FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage::class.java)
                .build()
    }

    fun initContactChatFireStoreOption() {
        val query =
            Firebase
                .firestore
                .collection(Constants.COLLECTION_PRIVATE_CHAT)
                .document(contact.value?.id!!)
                .collection(Constants.COLLECTION_MESSAGES)
                .orderBy(ChatMessage.TIME_STAMP, Query.Direction.DESCENDING)

        option.value =
            FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage::class.java)
                .build()
    }

    private fun isValidMessage(): Boolean {
        var isValid = true
        if (messageContent.value.isNullOrEmpty()) isValid = false
        return isValid
    }
}
