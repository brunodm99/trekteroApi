package com.github.brunodm99.trekteroApi.data.utils

import com.github.brunodm99.trekteroApi.Constants
import com.github.brunodm99.trekteroApi.data.entity.JWTToken
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import java.sql.DriverManager
import java.util.*
import java.util.stream.Collectors

object JWT {
    fun getJWTToken(useremail: String): String {
        val secretKey = Constants.SECRET_KEY

        val grantedAuthorities = AuthorityUtils
            .commaSeparatedStringToAuthorityList("ROLE_USER")

        val builder = Jwts
            .builder()
            .setId("trekteroJWT")
            .setSubject(useremail)
            .claim("authorities",
                grantedAuthorities.stream()
                    .map { obj: GrantedAuthority -> obj.authority }
                    .collect(Collectors.toList()))
            .signWith(
                SignatureAlgorithm.HS512,
                secretKey.toByteArray()
            )

        val issuedAt = Date(System.currentTimeMillis())
        val expirationDate = Date(System.currentTimeMillis() + 600000)
        builder.setIssuedAt(issuedAt)
        builder.setExpiration(expirationDate)

        val token = builder.compact()
        saveToken(JWTToken(token, expirationDate.toString(), 0))
        return token
    }

    private fun saveToken(token: JWTToken){
        val conn = DriverManager.getConnection("jdbc:mysql://localhost/trektero?useSSL=false&serverTimeXone=UTC&allowPublicKeyRetrieval=true", "root", "@2804BHdaP@")
        val pstm = conn.prepareStatement("INSERT INTO token_list(token, expire_at, denied) VALUES(?, ?, ?)")
        pstm.setString(1, token.token)
        pstm.setString(2, token.expireAt)
        pstm.setInt(3, token.denied)
        pstm.executeUpdate()
        pstm.close()
        conn.close()
    }
}