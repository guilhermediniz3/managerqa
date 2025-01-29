package com.tester.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica a todas as APIs
                .allowedOrigins("http://localhost:5173") // Porta do seu frontend!
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos que você usa
                .allowedHeaders("*"); // Ou liste os cabeçalhos específicos que você precisa
    }
}
