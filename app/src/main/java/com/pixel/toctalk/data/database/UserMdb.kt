package com.pixel.toctalk.data.database

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.pixel.toctalk.Constants
import com.pixel.toctalk.data.model.User

object UserMdb {
    private val storageRef = Firebase.storage.reference

    fun createUser(user: User, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_USERS)
            .document(user.uid ?: "")
            .set(user)
            .addOnCompleteListener(onComplete)
    }

    fun getUser(uid: String?, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_USERS)
            .document(uid!!)
            .get()
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

    fun updateUserData(
        uid: String,
        newUserName: String,
        imageUri: Uri,
        onComplete: OnCompleteListener<Void>,
    ) {
        uploadUserPic(uid, imageUri) { picUploaded ->
            if (picUploaded.isSuccessful) {
                Firebase
                    .firestore
                    .collection(Constants.COLLECTION_USERS)
                    .document(uid)
                    .update(User.PROFILE_PIC_FIELD, picUploaded.result.toString())
                    .addOnCompleteListener { picUpdated ->
                        if (picUpdated.isSuccessful) {
                            Firebase
                                .firestore
                                .collection(Constants.COLLECTION_USERS)
                                .document(uid)
                                .update(User.USERNAME_FIELD, newUserName)
                                .addOnCompleteListener(onComplete)
                        }
                    }
            }
        }
    }

    fun updateUsernameOnly(
        uid: String,
        newUserName: String,
        onComplete: OnCompleteListener<Void>,
    ) {
        Firebase
            .firestore
            .collection(Constants.COLLECTION_USERS)
            .document(uid)
            .update(User.USERNAME_FIELD, newUserName)
            .addOnCompleteListener(onComplete)
    }

    fun updateUserPicOnly(
        uid: String,
        imageUri: Uri,
        onComplete: OnCompleteListener<Void>,
    ) {
        uploadUserPic(uid, imageUri) { picUploaded ->
            if (picUploaded.isSuccessful) {
                Firebase
                    .firestore
                    .collection(Constants.COLLECTION_USERS)
                    .document(uid)
                    .update(User.PROFILE_PIC_FIELD, picUploaded.result.toString())
                    .addOnCompleteListener(onComplete)
            }
        }
    }
}
