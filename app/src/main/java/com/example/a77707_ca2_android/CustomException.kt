package com.example.a77707_ca2_android

open class CustomException(
    message: String = "Word exceeds 10 characters"
) : Exception(message) {

    override fun toString(): String {
        return "CustomException: $message"
    }
}