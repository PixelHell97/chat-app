package com.pixel.toctalk.data.database

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.data.model.Group

object ChatMdb {

    fun sendMessage(
        uid: String?,
        collection: String,
        message: ChatMessage,
        onComplete: OnCompleteListener<DocumentReference>,
    ) {
        Firebase
            .firestore
            .collection(collection)
            .document(uid!!)
            .collection(Constants.COLLECTION_MESSAGES)
            .add(message)
            .addOnCompleteListener(onComplete)
    }

    fun updateMessage(
        id: String,
        docID: String,
        collection: String,
        onComplete: OnCompleteListener<Void>,
    ) {
        Firebase
            .firestore
            .collection(collection)
            .document(docID)
            .collection(Constants.COLLECTION_MESSAGES)
            .document(id)
            .update(Group.UID_FIELD, id)
            .addOnCompleteListener(onComplete)
    }
}
