package com.tester.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/developers").permitAll()
                        .requestMatchers(HttpMethod.POST,"/developers").permitAll()
                        .requestMatchers(HttpMethod.GET,"/developers/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/developers/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/developers/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/technologies").permitAll()
                        .requestMatchers(HttpMethod.GET,"/technologies/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/technologies/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/technologies/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/testers").permitAll()
                        .requestMatchers(HttpMethod.GET,"/testers/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/testers/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/testers/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
