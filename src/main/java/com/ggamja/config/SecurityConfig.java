package com.ggamja.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/members/register",
                                "/api/members/login"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin().disable()
                .httpBasic().disable()
                .logout(logout -> logout
                        .logoutUrl("/api/members/logout")   // 로그아웃 엔드포인트 지정
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\":\"logout successfully\"}");
                        })
                ); // 기본 로그인 폼 비활성화

        return http.build();
    }
}

// Todo 배포 서버 올릴 때 수정해서 올리기
// .csrf(csrf -> csrf
//    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//)