package com.tester.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class TesterQADTO {

    private Long id;

    private String name;
    private boolean active;
    
    public TesterQADTO() {}

;

	public TesterQADTO(Long id, String name, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.active = active;
	}

	}
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}


}
