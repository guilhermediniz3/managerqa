package com.tester.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tester.dto.UserDTO;
import com.tester.service.UserService;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/api/users")

public class UserController {
	
	@Autowired
	
	private UserService userService;
	
	
	  @PostMapping
	  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
		  UserDTO  saveUser = userService.createUser(userDTO);
		  return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
		  
	  }
	  
	  
	  @GetMapping
	  
	  public ResponseEntity<List<UserDTO>> getAllUsers(){
		  List<UserDTO> users = userService.getAllUsers();
		  return ResponseEntity.ok(users);
  
		  
	  }
	  
	  @GetMapping("/{id}")
	    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
	        Optional<UserDTO> user = userService.getUserById(id);
	        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }
	  
	  @PutMapping("/{id}")
	    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
	        UserDTO updatedUser = userService.updateUser(id, userDTO);
	        return ResponseEntity.ok(updatedUser);
	    }

	    // Deletar um usuário
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
	        try {
	            userService.deleteUser(id);
	            return ResponseEntity.noContent().build();
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }	
	    
	    @PatchMapping("/{id}")
	    public ResponseEntity<UserDTO> patchUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
	        UserDTO updatedUser = userService.patchUser(id, userDTO);
	        return ResponseEntity.ok(updatedUser);
	    }
	    

}
