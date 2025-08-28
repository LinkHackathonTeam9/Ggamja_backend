package com.ggamja.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CookieSecureFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        String origin = request.getHeader("Origin");
        if (origin != null && origin.contains("localhost")) {
            // 로컬 프론트 요청일 때 Secure 제거해서 다시 내려주기
            String jsessionId = request.getSession().getId();

            response.addHeader("Set-Cookie",
                    String.format("JSESSIONID=%s; Path=/; HttpOnly; SameSite=None", jsessionId));
        }
    }
}