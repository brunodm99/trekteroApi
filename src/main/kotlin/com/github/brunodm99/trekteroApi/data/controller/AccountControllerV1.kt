package com.github.brunodm99.trekteroApi.data.controller

import com.github.brunodm99.trekteroApi.data.entity.Account
import com.github.brunodm99.trekteroApi.data.entity.Credential
import com.github.brunodm99.trekteroApi.data.response.AuthResponse
import com.github.brunodm99.trekteroApi.data.service.AccountServiceV1
import com.github.brunodm99.trekteroApi.data.utils.JWT
import org.hibernate.cfg.NotYetImplementedException
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.sql.SQLException
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaWildcardType

@RestController
@RequestMapping("api/v1/auth")
class AccountControllerV1 {
    @Autowired
    lateinit var service: AccountServiceV1

    @PostMapping("/signup")
    fun signup(@RequestBody credential: Credential): ResponseEntity<Any> {
        var response: ResponseEntity<Any>

        try{
            if(service.exists(credential.email)){
                val authResponse = AuthResponse(null, "El email introducido ya esta registrado.")
                return ResponseEntity(authResponse, HttpStatus.UNPROCESSABLE_ENTITY)
            }

            response = if(credential.isValidForSignup()){
                val account = Account(
                    0,
                    JWT.getJWTToken(credential.email),
                    credential.email,
                    credential.password,
                    LocalDateTime.now().toString())

                ResponseEntity(service.save(account), HttpStatus.CREATED)
            }else{
                val authResponse = AuthResponse(null, "Email o contraseña vacíos.")
                ResponseEntity(authResponse, HttpStatus.UNPROCESSABLE_ENTITY)
            }
        }catch(e: Exception){
            val authResponse = AuthResponse(null, e.message)
            response = ResponseEntity(authResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return response
    }

    @PostMapping("/refreshToken")
    fun refreshToken(@RequestBody token: String): ResponseEntity<Any>{
        throw NotYetImplementedException("No implementado todavía")
    }
}