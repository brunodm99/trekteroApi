package com.github.brunodm99.trekteroApi.data.service.action

import com.github.brunodm99.trekteroApi.data.entity.Account

interface AccountAction {
    fun get(accessToken: String): Account
    fun exists(useremail: String): Boolean
    fun save(account: Account): Account
    fun edit(account: Account): Account
    fun delete(account: Account)
}