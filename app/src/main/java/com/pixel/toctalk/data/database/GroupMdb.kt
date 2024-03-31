package com.pixel.toctalk.data.database

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.Group

object GroupMdb {
    private val storageRef = Firebase.storage.reference

    fun createGroup(group: Group, onComplete: OnCompleteListener<DocumentReference>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_GROUPS)
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
            .collection(Constants.COLLECTION_GROUPS)
            .document(uid)
            .update(Group.UID_FIELD, uid)
            .addOnCompleteListener(onComplete)
        Firebase
            .firestore
            .collection(Constants.COLLECTION_GROUPS)
            .document(uid)
            .update(Group.GROUP_PIC_FIELD, imageUrl)
    }

    fun addMemberToGroup(
        groupID: String,
        groupMembersList: List<String>?,
        onComplete: OnCompleteListener<Void>,
    ) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_GROUPS)
            .document(groupID)
            .update(Group.MEMBER_LIST_FIELD, groupMembersList)
            .addOnCompleteListener(onComplete)
    }
}
