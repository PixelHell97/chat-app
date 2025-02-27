package com.pixel.toctalk.auth.domain

import android.net.Uri
import com.pixel.toctalk.auth.domain.models.AuthUser
import com.pixel.toctalk.core.domain.util.AuthError
import com.pixel.toctalk.core.domain.util.Result

interface AuthenticationRepository {
    suspend fun login(
        email: String,
        password: String,
    ): Result<AuthUser, AuthError>

    suspend fun createAccount(
        email: String,
        password: String,
    ): Result<AuthUser, AuthError>

    suspend fun updateProfile(
        userName: String,
        userImage: Uri?,
    ): Result<AuthUser, AuthError>

    suspend fun createUserInFireStore(authUser: AuthUser): Result<Boolean, AuthError>

    suspend fun sendResetPasswordCodeWithEmail(email: String): Result<Boolean?, AuthError>

    suspend fun confirmResetPasswordCode(code: String): Result<Boolean, AuthError>

    suspend fun resetPassword(
        resetCode: String,
        newPassword: String,
    ): Result<Boolean, AuthError>

    suspend fun uploadUserImageToStorage(
        uid: String,
        image: Uri,
    ): String
}
