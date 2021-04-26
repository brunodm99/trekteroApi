package com.github.brunodm99.trekteroApi.data.service

import com.github.brunodm99.trekteroApi.data.entity.Account
import com.github.brunodm99.trekteroApi.data.repository.AccountRepository
import com.github.brunodm99.trekteroApi.data.service.action.AccountAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.*

@Service
class AccountServiceV1 : AccountAction{
    @Autowired
    lateinit var repository: AccountRepository

    override fun get(accessToken: String): Account {
        try{
            return repository.findByAccessToken(accessToken)
        }catch(e: Exception){
            throw NullPointerException("")
        }
    }

    override fun exists(useremail: String): Boolean{
        val opt = Optional.ofNullable(repository.findByEmail(useremail))

        return opt.isPresent
    }

    override fun save(account: Account): Account {
        try {
            return repository.save(account)
        }catch(e: Exception){
            throw NullPointerException("")
        }
    }

    override fun edit(account: Account) : Account{
        try{
            delete(account)
            return repository.save(account)
        }catch (e: Exception){
            throw NullPointerException("")
        }
    }

    override fun delete(account: Account) {
        try {
            repository.delete(account)
        }catch(e: Exception){

        }
    }

}