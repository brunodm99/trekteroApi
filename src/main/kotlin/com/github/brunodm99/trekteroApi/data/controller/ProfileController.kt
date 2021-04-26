package com.github.brunodm99.trekteroApi.data.controller

import com.github.brunodm99.trekteroApi.data.response.AuthResponse
import com.github.brunodm99.trekteroApi.data.service.JWTTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/profile")
class ProfileController {
    @Autowired
    lateinit var tokenService: JWTTokenService

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") accessToken: String): ResponseEntity<Any> {
        val response: ResponseEntity<Any>
        val token = accessToken.replace("Bearer", "")

        response = try{
            tokenService.delete(token)
            val profileResponse = AuthResponse(null, "Usuario desconectado con éxito.")
            ResponseEntity(profileResponse, HttpStatus.OK)
        }catch (e: Exception){
            val authResponse = AuthResponse(null, e.message)
            ResponseEntity(authResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return response
    }
}