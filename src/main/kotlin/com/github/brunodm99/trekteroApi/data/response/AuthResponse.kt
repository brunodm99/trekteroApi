package com.github.brunodm99.trekteroApi.data.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.github.brunodm99.trekteroApi.data.entity.Account

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthResponse(
    val account: Account?,
    val message: String?
)