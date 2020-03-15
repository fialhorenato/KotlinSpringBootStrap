package com.kotlin.bootstrap.security.utils

import com.kotlin.bootstrap.security.service.SecurityService.Companion.ROLE_PREFIX
import com.kotlin.bootstrap.security.service.UserDetails
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import kotlin.streams.toList

@Component
class JWTUtils {
    private val logger: Logger = LoggerFactory.getLogger(JWTUtils::class.java)

    @Value("\${kotlinstrap.app.jwtSecret}")
    lateinit var jwtSecret: String

    @Value("\${kotlinstrap.app.jwtExpirationMs}")
    var jwtExpirationMs = 86400000

    fun generateJwtToken(authentication: Authentication): String {
        // Get the User Principal to set the Subject in the JWT Token
        val userPrincipal = authentication.principal as UserDetails
        var claims = hashMapOf("email" to userPrincipal.email , "roles" to userPrincipal.roles, "password" to userPrincipal.password)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.username)
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun getPasswordFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body["password"] as String
    }

    fun getEmailFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body["email"] as String
    }

    fun getRolesFromJwtToken(token: String): List<String> {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body["roles"] as List<String>
    }

    fun getAuthoritiesFromJwtToken(token: String): List<SimpleGrantedAuthority> {
        return getRolesFromJwtToken(token).stream()
                .map { role -> SimpleGrantedAuthority(ROLE_PREFIX + role) }
                .toList()
    }

    fun toUserDetails(token : String): UserDetails {
        val username: String = getUserNameFromJwtToken(token)
        val email = getEmailFromJwtToken(token)
        val password = getPasswordFromJwtToken(token)
        val authorities = getAuthoritiesFromJwtToken(token)
        val roles = getRolesFromJwtToken(token)

        return com.kotlin.bootstrap.security.service.UserDetails(email, username, password, authorities, roles)
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }
}