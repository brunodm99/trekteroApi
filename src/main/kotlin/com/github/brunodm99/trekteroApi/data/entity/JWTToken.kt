package com.github.brunodm99.trekteroApi.data.entity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="token_list")
data class JWTToken(
    @Id
    @Column(unique = true, length = 300)
    val token: String,
    @Column(name="expire_at")
    val expireAt: String,
    @Column(length = 1)
    val denied: Int
){
    fun isExpired(): Boolean{
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        val expiredDate = LocalDateTime.from(dtf.parse(expireAt))
        val currentTime = LocalDateTime.parse(dtf.format(LocalDateTime.now()))

        return expiredDate.isAfter(currentTime)
    }

    fun isDenied() = denied == 1
}
