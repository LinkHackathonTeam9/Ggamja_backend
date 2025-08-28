package com.ggamja.global.config;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {

    @Bean
    public CookieSameSiteSupplier cookieSameSiteSupplier() {
        // JSESSIONID 쿠키에 SameSite=None 적용
        return CookieSameSiteSupplier.ofNone();
    }
}