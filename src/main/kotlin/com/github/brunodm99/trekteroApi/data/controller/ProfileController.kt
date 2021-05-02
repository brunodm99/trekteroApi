package com.github.brunodm99.trekteroApi.data.controller

import com.github.brunodm99.trekteroApi.data.response.LogoutResponse
import com.github.brunodm99.trekteroApi.data.service.AccountServiceV1
import com.github.brunodm99.trekteroApi.data.service.AuthenticatorService
import com.github.brunodm99.trekteroApi.data.service.JWTTokenServiceV1
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/profile")
class ProfileController  : InitializingBean {
    @Autowired
    lateinit var service: AccountServiceV1
    @Autowired
    lateinit var tokenService: JWTTokenServiceV1

    private lateinit var authenticator: AuthenticatorService

    override fun afterPropertiesSet(){
        authenticator = AuthenticatorService(service, tokenService)
    }
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<LogoutResponse> {
        return authenticator.logout(token)
    }
}