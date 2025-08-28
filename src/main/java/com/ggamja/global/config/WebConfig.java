package com.ggamja.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173", // 프론트 로컬 주소
                        "https://release.d1ojpmn1g2nfhz.amplifyapp.com", // 프론트 배포 주소
                        "https://api.ggamja.o-r.kr" // 백엔드 배포 주소
                )
                .allowedMethods("GET","POST","PATCH","DELETE", "PUT", "OPTION")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
