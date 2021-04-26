package com.github.brunodm99.trekteroApi.data.entity

import javax.persistence.*

@Entity
@Table(name="account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(name="access_token", unique=true, length = 300)
    var accessToken: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    var createdAt: String
){
    fun cloneWithPassword(newPassword: String): Account{
        return Account(id, accessToken, email, newPassword, createdAt)
    }
}
