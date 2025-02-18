	package com.tester.dto;
	
	import jakarta.validation.constraints.Email;
	import jakarta.validation.constraints.NotEmpty;
	
	public class UserDTO {
	
		private Long id;
		private String name;
	
		@Email(message = "O e-mail deve ser válido")
		@NotEmpty(message = "O e-mail é obrigatório")
		private String email;
	
		@NotEmpty(message = "A senha é obrigatória")
		private String password;
	
		private Boolean active;
	
		public UserDTO() {
		}
	
		public UserDTO(Long id, String name, String email, String password, boolean active) {
			this.id = id;
			this.name = name;
			this.email = email;
			this.password = password;
			this.active = active;
		}
	
		// Getters e Setters
		public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
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
	
		public Boolean isActive() {
			return active;
		}
	
		public void setActive(Boolean active) {
			this.active = active;
		}
	
	
	
	}
