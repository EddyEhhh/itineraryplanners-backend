package com.scrapheap.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITELIST_URL = {
            "/login",
            "/api/v1/accounts",
            "/api/v1/accounts/verify",
            "/api/v1/accounts/resendVerify"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().requestMatchers(WHITELIST_URL).permitAll()
                .anyRequest().authenticated()
                .and().csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable());
        return http.build();
    }

}