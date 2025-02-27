package com.pixel.toctalk.auth.domain.util

enum class ValidationError {
    EMPTY_EMAIL,
    EMPTY_PASSWORD,
    EMPTY_USERNAME,
    INVALID_EMAIL,
    INVALID_PASSWORD,
    UNMATCHED_PASSWORD,
    USERNAME_OUT_OF_RANGE,
    NONE,
}
