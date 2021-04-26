package com.github.brunodm99.trekteroApi.data.entity

data class Credential(
    val email: String,
    val password: String
) {

    fun isValidForSignup() = email.isNotEmpty() && password.isNotEmpty()

}