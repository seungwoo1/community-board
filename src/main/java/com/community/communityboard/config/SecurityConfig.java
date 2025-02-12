package com.community.communityboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (POST 요청 허용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register").permitAll() // 회원가입 요청 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        return http.build();
    }
}
