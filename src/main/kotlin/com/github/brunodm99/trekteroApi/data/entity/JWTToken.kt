package com.github.brunodm99.trekteroApi.data.entity

import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils
import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils.toLocalDateTime
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
    var denied: Int,
    var userEmail: String
){
    fun isExpired(): Boolean{
        val expiredDate = expireAt.toLocalDateTime()
        val currentTime = DateTimeUtils.currentDateTime().toLocalDateTime()

        return currentTime.isAfter(expiredDate)
    }

    fun isDenied() = denied == 1
}
