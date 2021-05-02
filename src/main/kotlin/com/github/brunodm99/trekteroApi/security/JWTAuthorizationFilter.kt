package com.github.brunodm99.trekteroApi.security

import com.github.brunodm99.trekteroApi.Constants
import com.github.brunodm99.trekteroApi.data.entity.JWTToken
import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.sql.DriverManager
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter : OncePerRequestFilter() {
    private val HEADER = "Authorization"
    private val PREFIX = "Bearer "

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            if (existsJWTToken(request, response)) {
                val claims = validateToken(request)
                if (claims["authorities"] != null) {
                    setUpSpringAuthentication(claims)
                } else {
                    SecurityContextHolder.clearContext()
                }
            } else {
                SecurityContextHolder.clearContext()
            }
            chain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "EXPIRED TOKEN")
            return
        } catch (e: UnsupportedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        } catch (e: MalformedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        }
    }

    private fun validateToken(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader(HEADER).replace(PREFIX, "").trim()
        val claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY.toByteArray()).parseClaimsJws(jwtToken).body

        val token = getJWTToken(jwtToken)

        token?.let{
            if(token.isDenied() || token.isExpired()){
                claims["authorities"] = null
                delete(token)
            }
        }

        if(token == null) claims["authorities"] = null

        return claims
    }

    private fun delete(token: JWTToken) {
        val conn = DriverManager.getConnection("jdbc:mysql://localhost/trektero?useSSL=false&serverTimeXone=UTC&allowPublicKeyRetrieval=true", "root", "@2804BHdaP@")
        val pstm = conn.prepareStatement("DELETE FROM token_list WHERE token=?")
        pstm.setString(1, token.token)
        pstm.executeUpdate()
        pstm.close()
        conn.close()
    }

    fun getJWTToken(token: String): JWTToken?{
        var jwtToken : JWTToken? = null
        val conn = DriverManager.getConnection("jdbc:mysql://localhost/trektero?useSSL=false&serverTimeXone=UTC&allowPublicKeyRetrieval=true", "root", "@2804BHdaP@")
        val pstm = conn.prepareStatement("SELECT * FROM token_list WHERE token=?")
        pstm.setString(1, token)

        val rs = pstm.executeQuery()
        if(rs.next()){
            jwtToken = JWTToken(
                rs.getString("token"),
                rs.getString("expire_at"),
                rs.getInt("denied"),
                rs.getString("user_email"))
        }
        pstm.close()
        conn.close()

        return jwtToken
    }

    /**
     * Metodo para autenticarnos dentro del flujo de Spring
     *
     * @param claims
     */
    private fun setUpSpringAuthentication(claims: Claims) {
        val authorities: List<String> = claims["authorities"] as List<String>
        val auth = UsernamePasswordAuthenticationToken(claims.subject, null,
            authorities.stream().map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }.collect(Collectors.toList())
        )
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun existsJWTToken(request: HttpServletRequest, res: HttpServletResponse): Boolean {
        val authenticationHeader = request.getHeader(HEADER)
        return !(authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
    }

}
