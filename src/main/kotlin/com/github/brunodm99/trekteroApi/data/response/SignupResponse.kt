package com.github.brunodm99.trekteroApi.data.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.github.brunodm99.trekteroApi.data.entity.Account
import com.github.brunodm99.trekteroApi.data.entity.JWTToken

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SignupResponse(
    val token: JWTToken?,
    val message: String?
)