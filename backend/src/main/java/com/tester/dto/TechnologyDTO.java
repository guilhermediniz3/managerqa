package com.tester.dto;

import java.util.Set;

import com.tester.entity.Technology;

public class TechnologyDTO {
	    private  Long id;
	    private  String name;
	    private  boolean active;
	    private  Set<DeveloperDTO> developers;
	    

	    public TechnologyDTO(Technology technology) {
	        this.id = technology.getId();
	        this.name = technology.getName();
	        this.active = technology.isActive();
	    }
	 

	    // Getters only
	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public boolean isActive() {
	        return active;
	    }

	    public Set<DeveloperDTO> getDevelopers() {
	        return developers;
	    }
	
	

}
