package com.tester.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tester.dto.LoginRequestDTO;
import com.tester.dto.RegisterRequestDTO;
import com.tester.dto.ResponseDTO;
import com.tester.entity.User;
import com.tester.infra.security.TokenService;
import com.tester.repository.UserRepository;

@RestController
@RequestMapping("/auth")


public class AuthController {

	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	private final UserRepository repository;

	@Autowired
	public AuthController(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
	    User user = this.repository.findByEmail(body.email())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Verifica se o usuário está ativo
	    if (!user.isActive()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not active");
	    }

	    // Verifica se a senha está correta
	    if (passwordEncoder.matches(body.password(), user.getPassword())) {
	        String token = this.tokenService.generationToken(user);
	        return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
	    }

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
		Optional<User> user = this.repository.findByEmail(body.email());

		if (user.isEmpty()) {
			User newUser = new User();
			newUser.setPassword(passwordEncoder.encode(body.password()));
			newUser.setEmail(body.email());
			newUser.setName(body.name());
			this.repository.save(newUser);

			String token = this.tokenService.generationToken(newUser);
			return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
		}
		return ResponseEntity.badRequest().build();
	}
	
	
}
