package com.github.brunodm99.trekteroApi.data.service.action

import com.github.brunodm99.trekteroApi.data.entity.JWTToken

interface JWTTokenAction {
    fun get(token: String): JWTToken
    fun exists(token: String): Boolean
    fun denied(token: String)
    fun save(token: JWTToken): JWTToken
    fun delete(token: JWTToken)
    fun deleteAll(useremail: String)
}