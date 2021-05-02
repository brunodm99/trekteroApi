package com.github.brunodm99.trekteroApi.data.entity

import javax.persistence.*

@Entity
@Table(name="account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    @Column(unique = true)
    val email: String,
    var password: String,
    var createdAt: String,
    var lastConnection: String
){
    fun cloneWithPassword(newPassword: String): Account{
        return Account(id, email, newPassword, createdAt, lastConnection)
    }

}
