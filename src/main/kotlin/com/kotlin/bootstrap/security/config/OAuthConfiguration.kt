package com.kotlin.bootstrap.security.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import kotlin.properties.Delegates


@Configuration
@EnableAuthorizationServer
class OAuthConfiguration(
        @Qualifier("authenticationManagerBean")
        private val authenticationManager: AuthenticationManager,
        private val passwordEncoder: PasswordEncoder,
        @Qualifier("SecurityDetailsService")
        private val userDetailsService: UserDetailsService,

        @Value(value = "\${jwt.client.id}")
        val clientId: String,

        @Value(value = "\${jwt.client.secret}")
        val clientSecret: String,

        @Value(value = "\${jwt.client.signing-key}")
        val jwtSigningKey: String,

        @Value(value = "\${jwt.client.access-token-validity}")
        val accessTokenValiditySeconds: Int,

        @Value(value = "\${jwt.client.authorized-grant-types}")
        val authorizedGrantTypes: String,

        @Value(value = "\${jwt.client.refresh-token-validity}")
        val refreshTokenValiditySeconds: Int
) : AuthorizationServerConfigurerAdapter() {


    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .scopes("read", "write")
                .resourceIds("api")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        return JwtAccessTokenConverter()
    }
}