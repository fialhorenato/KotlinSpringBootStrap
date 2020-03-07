package com.kotlin.bootstrap.security.filter

import com.kotlin.bootstrap.security.exception.JWTException
import com.kotlin.bootstrap.security.service.SecurityService
import com.kotlin.bootstrap.security.utils.JWTUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthTokenFilter (var jwtUtils : JWTUtils, var securityService: SecurityService) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt: String? = parseJwt(request)
            if (Objects.nonNull(jwt) && jwtUtils.validateJwtToken(jwt!!)) {
                val username: String? = jwtUtils.getUserNameFromJwtToken(jwt)
                val userDetails: UserDetails = securityService.loadUserByUsername(username!!)
                val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else null
    }

}