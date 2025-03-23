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
import org.springframework.web.bind.annotation.GetMapping;

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
	                        .requestMatchers(HttpMethod.GET, "/testplans/filter/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/api/users/**").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/api/users/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/api/users/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/api/users/**").permitAll()
	                        .requestMatchers(HttpMethod.PATCH,"/api/users/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/developers").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/developers/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/developers/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/developers/**").permitAll()
	                        .requestMatchers(HttpMethod.PATCH,"/developers/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/technologies/**").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/technologies/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/technologies/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/technologies/**").permitAll()
	                        .requestMatchers(HttpMethod.PATCH,"/technologies/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/testers").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/testers/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/testers/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/testers/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/testplans").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/testplans/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/testplans/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/testplans/**").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/testplans/all/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/test-suites").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/test-suites/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/test-suites/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/test-suites/**").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/test-suites/clone/{id}").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/testcases").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/testcases/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/testcases/**").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/testcases/**").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.PUT, "/testcases/**").permitAll()
	                        .requestMatchers(HttpMethod.POST,"/modules").permitAll()
	                        .requestMatchers(HttpMethod.GET,"/modules/**").permitAll()
	                        .requestMatchers(HttpMethod.DELETE,"/modules/**").permitAll()
	                        .requestMatchers(HttpMethod.PUT,"/modules/**").permitAll()
	                        .requestMatchers(HttpMethod.PATCH,"/modules/**").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/test-suites/testplan/{testPlanId}/suites").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/test-suites/testplan/**").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/api/test-plans/{testPlanId}/last-code-suite").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/api/test-plans/{testPlanId}/last-code-suite").permitAll()
	                        .requestMatchers(HttpMethod.PUT, "/api/test-plans/{testPlanId}/last-code-suite").permitAll()
	                        .requestMatchers(HttpMethod.DELETE, "/api/test-plans/{testPlanId}/last-code-suite").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/plan/{testPlanId}/suite/{testSuiteId}/last-code-case").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.POST, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.PUT, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.DELETE, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/testcases/plan/{testPlanId}/suite/{testSuiteId}").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/plan/{testPlanId}/suite/{testSuiteId}/last-code-case").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/testplan/*/created-by").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/relatorios/excel").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/relatorios/pdf").permitAll() 
	                        .requestMatchers(HttpMethod.GET, "/relatorio-jira").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/relatorio-jira/pdf").permitAll()
	                        .requestMatchers(HttpMethod.GET, "/search**").permitAll()
		                    
	                 
	               
                        
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
