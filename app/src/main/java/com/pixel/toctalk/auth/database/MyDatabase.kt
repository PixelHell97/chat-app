package com.pixel.toctalk.auth.database

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

object MyDatabase {
    private const val COLLECTION_NAME = "users"

    fun createUser(user: User, onComplete: OnCompleteListener<Void>) {
        Firebase
            .firestore
            .collection(COLLECTION_NAME)
            .document(user.uid)
            .set(user)
            .addOnCompleteListener(onComplete)
    }

    fun getUser(uid: String?, onComplete: OnCompleteListener<DocumentSnapshot>) {
        Firebase
            .firestore
            .collection(COLLECTION_NAME)
            .document(uid!!)
            .get()
            .addOnCompleteListener(onComplete)
    }
}
