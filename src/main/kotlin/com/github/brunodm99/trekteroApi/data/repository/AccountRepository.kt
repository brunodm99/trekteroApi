package com.github.brunodm99.trekteroApi.data.repository

import com.github.brunodm99.trekteroApi.data.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long>{
    fun findByEmail(useremail: String): Account?
}