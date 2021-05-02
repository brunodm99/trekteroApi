package com.github.brunodm99.trekteroApi.data.utils

import com.github.brunodm99.trekteroApi.Constants
import com.github.brunodm99.trekteroApi.data.entity.JWTToken
import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils.toDate
import com.github.brunodm99.trekteroApi.data.utils.DateTimeUtils.toLocalDateTime
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import java.util.stream.Collectors

object JWT {
    fun getJWTToken(useremail: String): JWTToken {
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

        val issuedAt = DateTimeUtils.currentDateTime()
        val expirationDate = issuedAt.toLocalDateTime().plusDays(30)
        builder.setIssuedAt(issuedAt.toLocalDateTime().toDate())
        builder.setExpiration(expirationDate.toDate())

        val token = builder.compact()

        return JWTToken(token, DateTimeUtils.asString(expirationDate), 0, useremail)
    }

}