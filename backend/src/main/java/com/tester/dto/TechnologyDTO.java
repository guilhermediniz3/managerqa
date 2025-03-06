package com.tester.dto;

import java.util.Set;

import com.tester.entity.Technology;

public class TechnologyDTO {
	private Long id;
    private String name;
    private Boolean active;

    // Construtor padrão
    public TechnologyDTO() {}


	public TechnologyDTO(Long id, String name, Boolean active) {
	
		this.id = id;
		this.name = name;
		this.active = active;
	}

	// Construtor que recebe a entidade
    public TechnologyDTO(Technology technology) {
        this.id = technology.getId();
        this.name = technology.getName();
        this.active = technology.isActive();
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


	public Boolean isActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


}
