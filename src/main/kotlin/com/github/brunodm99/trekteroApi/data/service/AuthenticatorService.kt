package com.github.brunodm99.trekteroApi.data.service

import com.github.brunodm99.trekteroApi.data.entity.Account
import com.github.brunodm99.trekteroApi.data.entity.Credential
import com.github.brunodm99.trekteroApi.data.response.LoginResponse
import com.github.brunodm99.trekteroApi.data.response.LogoutResponse
import com.github.brunodm99.trekteroApi.data.response.RefreshResponse
import com.github.brunodm99.trekteroApi.data.response.SignupResponse
import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils
import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils.toLocalDateTime
import com.github.brunodm99.trekteroApi.data.utils.JWT
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.time.temporal.ChronoUnit.DAYS

fun clazz() = AuthenticatorService::class.java

class AuthenticatorService(
    private val accountService: AccountServiceV1,
    private val tokenService: JWTTokenServiceV1) {

    private val properties = Properties()
    private val connection: Connection

    init{
        properties.load(clazz().getResourceAsStream("/application.properties"))
        connection = DriverManager.getConnection(properties["db.url"].toString())
    }

    fun login(credential: Credential): ResponseEntity<LoginResponse> {
        val account = accountService.getByUseremail(credential.email)

        return if(account.password == credential.password){
            val token = JWT.getJWTToken(credential.email)
            deleteAllIfLogin(credential.email)
            tokenService.save(token)

            val loginResponse = LoginResponse(token, "Usuario conectado con éxito.")
            ResponseEntity(loginResponse, HttpStatus.OK)
        }else{
            val loginResponse = LoginResponse(null, "Contraseña errónea.")
            ResponseEntity(loginResponse, HttpStatus.UNAUTHORIZED)
        }
    }

    fun signup(credential: Credential): ResponseEntity<SignupResponse> {
        if(accountService.exists(credential.email)){
            val signupResponse = SignupResponse(null, "El email introducido ya esta registrado.")
            return ResponseEntity(signupResponse, HttpStatus.FORBIDDEN)
        }

        return if(credential.isValidForSignup()){
            val account = Account(
                0,
                credential.email,
                credential.password,
                DateTimeUtils.currentDateTime(),
                DateTimeUtils.currentDateTime())

            val token = JWT.getJWTToken(credential.email)
            accountService.save(account)
            tokenService.save(token)

            val signupResponse = SignupResponse(token, "Usuario registrado con éxito.")
            ResponseEntity(signupResponse, HttpStatus.OK)
        }else{
            val signupResponse = SignupResponse(null, "Email o contraseña vacíos.")
            ResponseEntity(signupResponse, HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    fun logout(token: String): ResponseEntity<LogoutResponse>{
        val jwtoken = token.replace("Bearer", "").trim()

        return try{
            tokenService.denied(jwtoken)
            ResponseEntity(LogoutResponse("Usuario desconectado con éxito."), HttpStatus.OK)
        }catch (e: Exception){
            ResponseEntity(LogoutResponse(e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    fun deleteAllIfLogin(useremail: String){
        tokenService.deleteAll(useremail)
    }

    fun shouldRefreshToken(token: String): ResponseEntity<RefreshResponse>{
        val jwtToken = tokenService.get(token)
        val account = accountService.getByUseremail(jwtToken.userEmail)
        account.lastConnection = DateTimeUtils.currentDateTime()
        accountService.edit(account)

        val currentDate = DateTimeUtils.currentDateTime().toLocalDateTime()
        val expiredDate = jwtToken.expireAt.toLocalDateTime()

        val beforeExpireDate = expiredDate.minusDays(1)
        //Si hoy es un dia antes de que expire el token, refresco el token
        return if(DateTimeUtils.isSameDay(beforeExpireDate, currentDate)){
            val newToken = JWT.getJWTToken(account.email)
            tokenService.delete(jwtToken)
            tokenService.save(newToken)

            val refreshResponse = RefreshResponse(newToken, "Token regenerado.")
            ResponseEntity(refreshResponse, HttpStatus.ACCEPTED)
        }else{
            val refreshResponse = RefreshResponse(jwtToken, "No hace falta regenerar token.")
            ResponseEntity(refreshResponse, HttpStatus.NOT_ACCEPTABLE)
        }

    }

    /**
     * Lo llama la JVM con el GC
     */
    protected fun finalize() {
        connection.close()
    }

}