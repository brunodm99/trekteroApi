package com.github.brunodm99.trekteroApi.data.repository

import com.github.brunodm99.trekteroApi.data.entity.JWTToken
import org.springframework.data.jpa.repository.JpaRepository

interface JWTTokenRepository : JpaRepository<JWTToken, String>