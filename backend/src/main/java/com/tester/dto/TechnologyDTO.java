package com.tester.dto;

import java.util.Set;

import com.tester.entity.Technology;

public class TechnologyDTO {
	private Long id;
    private String name;
    private boolean active;

    // Construtor padrão
    public TechnologyDTO() {}

    // Construtor com parâmetros
    public TechnologyDTO(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
