package com.pixel.toctalk.data.database

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.data.model.Contact

object PrivateChatMdb {
    fun createChat(contact: Contact, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .add(contact)
            .addOnCompleteListener { task ->
                updateChatID(task.result.id, onComplete)
            }
    }

    private fun updateChatID(id: String, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_PRIVATE_CHAT)
            .document(id)
            .update(Contact.CHAT_ID_FIELD, id)
            .addOnCompleteListener(onComplete)
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
                    Firebase
                        .firestore
                        .collection(collection)
                        .document(docID)
                        .update(
                            Contact.LAST_MESSAGE_FIELD,
                            it.result.toObject(ChatMessage::class.java),
                        )
                }
        }
    }
}
