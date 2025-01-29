package com.tester.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDTO {

	
	    private String name;
	    

	    @Email(message = "O e-mail deve ser válido")
	    @NotEmpty(message = "O e-mail é obrigatório")
	    private String email;

	    @NotEmpty(message = "A senha é obrigatória")
	    private String password;

	    // Construtores
	    public UserDTO() {
	    }

	    public UserDTO(String name, String email, String password) {
	        this.name = name;
	        this.email = email;
	        this.password = password;
	    }

	    // Getters e Setters
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
}
