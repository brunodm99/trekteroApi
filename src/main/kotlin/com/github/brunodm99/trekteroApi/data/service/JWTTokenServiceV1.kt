package com.github.brunodm99.trekteroApi.data.service

import com.github.brunodm99.trekteroApi.data.entity.JWTToken
import com.github.brunodm99.trekteroApi.data.repository.JWTTokenRepository
import com.github.brunodm99.trekteroApi.data.service.action.JWTTokenAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class JWTTokenServiceV1 : JWTTokenAction{
    @Autowired
    lateinit var repository: JWTTokenRepository

    override fun get(token: String) = repository.findById(token).get()

    override fun exists(token: String) = repository.findById(token).isPresent

    override fun denied(token: String) {
        val jwtToken = get(token)
        jwtToken.denied = 1
        repository.delete(jwtToken)
        save(jwtToken)
    }

    override fun save(token: JWTToken) = repository.save(token)

    override fun delete(token: JWTToken) {
        repository.delete(token)
    }

    override fun deleteAll(useremail: String) {
        val allToken = repository.getByUserEmail(useremail)

        allToken.forEach{ delete(it) }
    }
}