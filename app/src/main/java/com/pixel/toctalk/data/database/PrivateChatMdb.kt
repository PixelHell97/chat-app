package com.pixel.toctalk.data.database

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.data.model.Contact

object PrivateChatMdb {
    fun createChat(contact: Contact, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .add(contact)
            .addOnCompleteListener { task ->
                updateChatID(task.result.id, onComplete)
            }
    }

    private fun updateChatID(id: String, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .document(id)
            .update(Contact.CHAT_ID_FIELD, id)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Firebase
                        .firestore
                        .collection(Constants.COLLECTION_PRIVATE_CHAT)
                        .document(id)
                        .get()
                        .addOnCompleteListener(onComplete)
                }
            }
    }

    fun updateLastMessage(id: String, docID: String, collection: String) {
        if (collection == Constants.COLLECTION_PRIVATE_CHAT) {
            Firebase
                .firestore
                .collection(collection)
                .document(docID)
                .collection(Constants.COLLECTION_MESSAGES)
                .document(id)
                .get()
                .addOnCompleteListener {
                    val chatMessage = it.result.toObject(ChatMessage::class.java)
                    Firebase
                        .firestore
                        .collection(collection)
                        .document(docID)
                        .update(
                            Contact.LAST_MESSAGE_FIELD,
                            chatMessage,
                        )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Firebase
                                    .firestore
                                    .collection(collection)
                                    .document(docID)
                                    .update(
                                        Contact.LAST_TIMESTAMP_FIELD,
                                        chatMessage?.timestamp,
                                    )
                            }
                        }
                }
        }
    }

    private fun deleteChat(chatID: String) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .document(chatID)
            .delete()
            .addOnCompleteListener { if (it.isSuccessful) return@addOnCompleteListener }
    }

    fun checkIfChatEmpty(id: String?) {
        getChat(id) { chatTask ->
            if (chatTask.isSuccessful) {
                val chat = chatTask.result.toObject(Contact::class.java)
                if (chat?.lastMessage == null) {
                    deleteChat(id!!)
                }
            }
        }
    }

    private fun getChat(id: String?, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .document(id!!)
            .get()
            .addOnCompleteListener(onComplete)
    }
}
