package com.tester.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.tester.entity.Developer;

public class DeveloperDTO {

	    private Long id;
	    private String name;
	    private boolean active;
	    private Set<TechnologyDTO> technologies;
	    
	    public DeveloperDTO() {}

       public DeveloperDTO(Developer developer) {
    	   this.id = developer.getId();
    	   this.name = developer.getName();
    	   this.active = developer.isActive();
    	   this.technologies = developer.getTechnologies().stream()
    			                        .map(technology -> new TechnologyDTO(technology)) 
    			                        .collect(Collectors.toSet());
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
		public Set<TechnologyDTO> getTechnologies() {
			return technologies;
		}
}
	