package com.pixel.toctalk.auth.domain.usecase

import com.pixel.toctalk.auth.domain.util.ValidationError

class ValidationUseCase {
    fun isValidUserName(userName: String?): ValidationError =
        if (userName.isNullOrEmpty() || userName.length < 3) {
            ValidationError.USERNAME_OUT_OF_RANGE
        } else {
            ValidationError.NONE
        }

    fun isValidEmail(email: String): ValidationError {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return if (emailRegex.matches(email)) {
            ValidationError.NONE
        } else {
            ValidationError.INVALID_EMAIL
        }
    }

//    fun isValidPhoneNumber(phoneNumber: String): ValidationError {
//        return if (phoneNumber.length == 10) {
//            phoneNumber[0] == '1' &&
//                    arrayOf(
//                        '0',
//                        '1',
//                        '2',
//                        '5',
//                    ).contains(phoneNumber[1])
//        } else {
//            ValidationError.NONE
//        }
//    }

    fun isValidPassword(password: String): ValidationError {
        val passwordRegex =
            Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$")
        return if (passwordRegex.matches(password)) {
            ValidationError.NONE
        } else {
            ValidationError.INVALID_PASSWORD
        }
    }

//    fun isValidConfirmPassword(
//        password: String?,
//        confirmPassword: String?,
//    ): Boolean {
//        return if (password.isNullOrEmpty() && confirmPassword.isNullOrEmpty()) {
//            false
//        } else {
//            password == confirmPassword
//        }
//    }
}
