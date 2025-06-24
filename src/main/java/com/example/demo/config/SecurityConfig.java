package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 禁用預設的登入頁面，保持原有的 Session 認證機制
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（因為前端是獨立）
            .csrf(csrf -> csrf.disable())
            
            // 允許所有請求通過（使用原有的 Session 認證）
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // 禁用預設的登入頁面
            .formLogin(form -> form.disable())
            
            // 禁用 HTTP Basic 認證
            .httpBasic(basic -> basic.disable())
            
            // 禁用登出功能（使用原有的登出邏輯）
            .logout(logout -> logout.disable());

        return http.build();
    }
}