package com.tester.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tester.entity.User;

@Service
public class TokenService {
	
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generationToken(User user) {
	
			
		try {
			
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token =JWT.create()
					       .withIssuer("login-auth-api")
					       .withSubject(user.getEmail())
					       .withExpiresAt(this.generationExpirationDate())
					       .sign(algorithm);
			return token;
			
		} catch (JWTCreationException  exception) {
			throw new RuntimeException("Error while authenticating");
			// TODO: handle exception
		}
		
		
	}
	
	public String validateToken(String token) {
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					 .withIssuer("login-auth-api")
					 .build()
					 .verify(token)
					  .getSubject();
		
				     
		}catch(JWTVerificationException exception) {
			
			return null;
		}
		
	}
	

	private Instant generationExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(-3));
	}

}
	