package com.pixel.toctalk.auth.data

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.pixel.toctalk.auth.domain.AuthenticationRepository
import com.pixel.toctalk.auth.domain.models.AuthUser
import com.pixel.toctalk.core.Constants
import com.pixel.toctalk.core.data.models.UserDto
import com.pixel.toctalk.core.domain.util.AuthError
import com.pixel.toctalk.core.domain.util.Result
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthenticationFirebaseDataSource : AuthenticationRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storageRef = Firebase.storage.reference

    override suspend fun login(
        email: String,
        password: String,
    ): Result<AuthUser, AuthError> =
        suspendCoroutine { continuation ->
            auth
                .signInWithEmailAndPassword(
                    email,
                    password,
                ).addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            val firebaseUser = task.result.user
                            Result.Success(
                                AuthUser(
                                    uid = firebaseUser?.uid,
                                    profilePic = firebaseUser?.photoUrl,
                                    displayName = firebaseUser?.displayName,
                                    email = firebaseUser?.email,
                                ),
                            )
                        } else {
                            Log.e("login", "${task.exception}")
                            val error =
                                when (task.exception) {
                                    is FirebaseAuthEmailException -> AuthError.INVALID_EMAIL
                                    is FirebaseAuthInvalidUserException -> AuthError.USER_NOT_FOUND
                                    is FirebaseAuthInvalidCredentialsException -> AuthError.WRONG_EMAIL_OR_PASSWORD
                                    is FirebaseNetworkException -> AuthError.NETWORK_ERROR
                                    else -> AuthError.UNKNOWN
                                }
                            Result.Error(error)
                        }
                    continuation.resume(result)
                }
        }

    override suspend fun createAccount(
        email: String,
        password: String,
    ): Result<AuthUser, AuthError> =
        suspendCoroutine { continuation ->
            auth
                .createUserWithEmailAndPassword(
                    email,
                    password,
                ).addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            val firebaseUser = task.result.user
                            Result.Success(
                                AuthUser(
                                    uid = firebaseUser?.uid,
                                    profilePic = firebaseUser?.photoUrl,
                                    displayName = firebaseUser?.displayName,
                                    email = firebaseUser?.email,
                                    isEmailVerified = firebaseUser?.isEmailVerified ?: false,
                                ),
                            )
                        } else {
                            val error =
                                when (task.exception) {
                                    is FirebaseAuthEmailException -> AuthError.INVALID_EMAIL
                                    is FirebaseAuthWeakPasswordException -> AuthError.WEAK_PASSWORD
                                    is FirebaseNetworkException -> AuthError.NETWORK_ERROR
                                    is FirebaseAuthUserCollisionException -> AuthError.EMAIL_ALREADY_IN_USE
                                    else -> AuthError.UNKNOWN
                                }
                            Result.Error(error)
                        }
                    continuation.resume(result)
                }
        }

    override suspend fun updateProfile(
        userName: String,
        userImage: Uri?,
    ): Result<AuthUser, AuthError> =
        suspendCoroutine { continuation ->
            val profileUpdates =
                UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(userName)
                    .setPhotoUri(userImage)
                    .build()
            auth.currentUser
                ?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            Log.d("Auth: updateProfile", "$firebaseUser")
//                            firebaseUser?.photoUrl?.let { image ->
//                                uploadUserImage(firebaseUser.uid, image)
//                            }
                            Result.Success(
                                AuthUser(
                                    uid = firebaseUser?.uid,
                                    profilePic = firebaseUser?.photoUrl,
                                    displayName = firebaseUser?.displayName,
                                    email = firebaseUser?.email,
                                    isEmailVerified = firebaseUser?.isEmailVerified ?: false,
                                ),
                            )
                        } else {
                            val error =
                                when (task.exception) {
                                    is FirebaseAuthEmailException -> AuthError.INVALID_EMAIL
                                    is FirebaseAuthWeakPasswordException -> AuthError.WEAK_PASSWORD
                                    is FirebaseNetworkException -> AuthError.NETWORK_ERROR
                                    else -> AuthError.UNKNOWN
                                }
                            Result.Error(error)
                        }
                    continuation.resume(result)
                }
        }

    override suspend fun uploadUserImageToStorage(
        uid: String,
        image: Uri,
    ): String =
        suspendCoroutine { continuation ->
            val imageRef = storageRef.child("${Constants.USER_IMAGE_PATH}/$uid")
            val uploadedImage =
                imageRef
                    .putFile(image)
                    .addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener {
                            continuation.resume(it.toString())
                        }
                    }
        }

    override suspend fun createUserInFireStore(authUser: AuthUser): Result<Boolean, AuthError> =
        suspendCoroutine { continuation ->
            firestore
                .collection(Constants.COLLECTION_USERS)
                .document(authUser.uid.toString())
                .set(
                    UserDto(
                        uid = authUser.uid.toString(),
                        displayName = authUser.displayName.toString(),
                        emailAddress = authUser.email.toString(),
                        phoneNumber = null,
                        profilePic = authUser.profilePic.toString(),
                        bio = null,
                        friendsList = emptyList(),
                        friendsRequestList = emptyList(),
                        isOnline = true,
                        isEmailVerified = authUser.isEmailVerified,
                        isPhoneNumberVerified = false,
                        createdAt = Timestamp.now(),
                    ),
                ).addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            Log.d("Auth: CreateUserInFireStore", "${task.isSuccessful}")
                            Result.Success(true)
                        } else {
                            Log.e("Auth: CreateUserInFireStore", "${task.exception}")
                            val error = AuthError.UNKNOWN
                            Result.Error(error)
                        }
                    continuation.resume(result)
                }
        }

    override suspend fun sendResetPasswordCodeWithEmail(email: String): Result<Boolean?, AuthError> =
        suspendCoroutine { continuation ->
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                val result =
                    if (task.isSuccessful) {
                        Result.Success(true)
                    } else {
                        Log.e("Auth: SendResetPasswordCode", "${task.exception}")
                        val error =
                            when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> AuthError.USER_NOT_FOUND
                                is FirebaseNetworkException -> AuthError.NETWORK_ERROR
                                else -> AuthError.UNKNOWN
                            }
                        Result.Error(error)
                    }
                continuation.resume(result)
            }
        }

    override suspend fun confirmResetPasswordCode(code: String): Result<Boolean, AuthError> =
        suspendCoroutine { continuation ->
            auth.checkActionCode(code).addOnCompleteListener { task ->
                val result =
                    if (task.isSuccessful) {
                        Result.Success(true)
                    } else {
                        val error =
                            when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> AuthError.INVALID_CODE
                                is FirebaseNetworkException -> AuthError.NETWORK_ERROR
                                else -> AuthError.UNKNOWN
                            }
                        Result.Error(error)
                    }
                continuation.resume(result)
            }
        }

    override suspend fun resetPassword(
        resetCode: String,
        newPassword: String,
    ): Result<Boolean, AuthError> =
        suspendCoroutine { continuation ->
            auth
                .confirmPasswordReset(
                    resetCode,
                    newPassword,
                ).addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            Result.Success(true)
                        } else {
                            val error =
                                when (task.exception) {
                                    else -> AuthError.UNKNOWN
                                }
                            Result.Error(error)
                        }
                    continuation.resume(result)
                }
        }
}
