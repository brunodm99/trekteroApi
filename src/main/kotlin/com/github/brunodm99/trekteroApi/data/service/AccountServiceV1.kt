package com.github.brunodm99.trekteroApi.data.service

import com.github.brunodm99.trekteroApi.data.entity.Account
import com.github.brunodm99.trekteroApi.data.repository.AccountRepository
import com.github.brunodm99.trekteroApi.data.service.action.AccountAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.*

@Service
class AccountServiceV1 : AccountAction {
   @Autowired
   lateinit var repository: AccountRepository


    override fun getByUseremail(useremail: String): Account {
        try{
            return repository.findByEmail(useremail)!!
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
            val accountToSave = repository.getOne(account.id)
            accountToSave.lastConnection = account.lastConnection

            if(account.password != accountToSave.password)
                accountToSave.password = account.password

            save(accountToSave)
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