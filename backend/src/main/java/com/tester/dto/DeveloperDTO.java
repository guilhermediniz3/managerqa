package com.tester.dto;
import java.util.Set;

public class DeveloperDTO {
	 private Long id;
	    private String name;
	    private boolean active;
	    private Set<Long> technologyIds;

	    // Construtor padrão
	    public DeveloperDTO() {}

	    // Construtor com parâmetros
	    public DeveloperDTO(Long id, String name, boolean active, Set<Long> technologyIds) {
	        this.id = id;
	        this.name = name;
	        this.active = active;
	        this.technologyIds = technologyIds;
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

	    public Set<Long> getTechnologyIds() {
	        return technologyIds;
	    }

	    public void setTechnologyIds(Set<Long> technologyIds) {
	        this.technologyIds = technologyIds;
	    }
}
