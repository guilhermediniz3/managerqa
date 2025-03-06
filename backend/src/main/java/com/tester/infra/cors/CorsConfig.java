package com.tester.infra.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

	  @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**") // Permite todas as rotas
	                .allowedOrigins("http://localhost:5173") // Origem do seu frontend (ajuste para a porta correta)
	                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS") // Métodos permitidos
	                .allowedHeaders("*") // Cabeçalhos permitidos
	                .allowCredentials(true) // Permite cookies (se necessário)
	                .maxAge(3600); // Cache de preflight OPTIONS por 1 hora
	        
	    }
}