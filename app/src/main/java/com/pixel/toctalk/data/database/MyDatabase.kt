package com.pixel.toctalk.data.database

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.pixel.toctalk.Constants
import com.pixel.toctalk.Constants.COLLECTION_GROUPS
import com.pixel.toctalk.Constants.COLLECTION_MESSAGES
import com.pixel.toctalk.Constants.COLLECTION_PRIVATE_CHAT
import com.pixel.toctalk.Constants.COLLECTION_USERS
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.data.model.User

object MyDatabase {
    private val storageRef = Firebase.storage.reference

    fun createUser(user: User, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(COLLECTION_USERS)
            .document(user.uid ?: "")
            .set(user)
            .addOnCompleteListener(onComplete)
    }

    fun getUser(uid: String?, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(COLLECTION_USERS)
            .document(uid!!)
            .get()
            .addOnCompleteListener(onComplete)
    }

    fun createGroup(group: Group, onComplete: OnCompleteListener<DocumentReference>) {
        Firebase
            .firestore
            .collection(COLLECTION_GROUPS)
            .add(group)
            .addOnCompleteListener(onComplete)
    }

    fun uploadGroupPic(groupId: String, groupUri: Uri?, onComplete: OnCompleteListener<Uri>) {
        val ref = storageRef
            .child("${Constants.GROUP_IMAGE_PATH}/$groupId")
        val uploadImage = groupUri?.let {
            ref.putFile(it)
        }
        uploadImage?.continueWith { task ->
            if (task.isSuccessful) {
                ref.downloadUrl.addOnCompleteListener(onComplete)
            }
        }
    }

    fun updateGroup(uid: String, imageUrl: String, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(COLLECTION_GROUPS)
            .document(uid)
            .update(Group.UID_FIELD, uid)
            .addOnCompleteListener(onComplete)
        Firebase
            .firestore
            .collection(COLLECTION_GROUPS)
            .document(uid)
            .update(Group.GROUP_PIC_FIELD, imageUrl)
    }

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
            .collection(COLLECTION_MESSAGES)
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
            .collection(COLLECTION_MESSAGES)
            .document(id)
            .update(Group.UID_FIELD, id)
            .addOnCompleteListener(onComplete)
    }

    fun uploadUserPic(uid: String, userProfileUri: Uri?, onComplete: OnCompleteListener<Uri>) {
        val ref = storageRef
            .child("${Constants.USER_IMAGE_PATH}/$uid")
        val uploadImage = userProfileUri?.let {
            ref.putFile(it)
        }
        uploadImage?.continueWith { task ->
            if (task.isSuccessful) {
                ref.downloadUrl.addOnCompleteListener(onComplete)
            }
        }
    }

    fun addMemberToGroup(
        groupID: String,
        groupMembersList: List<String>?,
        onComplete: OnCompleteListener<Void>,
    ) {
        Firebase
            .firestore
            .collection(COLLECTION_GROUPS)
            .document(groupID)
            .update(Group.MEMBER_LIST_FIELD, groupMembersList)
            .addOnCompleteListener(onComplete)
    }

    fun createChat(contact: Contact, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(COLLECTION_PRIVATE_CHAT)
            .add(contact)
            .addOnCompleteListener { task ->
                updateChatID(task.result.id, onComplete)
            }
    }

    private fun updateChatID(id: String, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(COLLECTION_PRIVATE_CHAT)
            .document(id)
            .update(Contact.CHAT_ID_FIELD, id)
            .addOnCompleteListener(onComplete)
    }

    fun updateLastMessage(id: String, docID: String, collection: String) {
        if (collection == COLLECTION_PRIVATE_CHAT) {
            Firebase
                .firestore
                .collection(collection)
                .document(docID)
                .collection(COLLECTION_MESSAGES)
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
