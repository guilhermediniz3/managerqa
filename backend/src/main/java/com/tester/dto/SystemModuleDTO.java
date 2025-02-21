package com.tester.dto;

import com.tester.entity.SystemModule;

public class SystemModuleDTO {

    private Long id;

    private String name;
    private Boolean active;

	public SystemModuleDTO() {}
	

	public SystemModuleDTO(Long id, String name, Boolean active) {
	
		this.id = id;
		this.name = name;
		this.active = active;
	}
	
	public SystemModuleDTO(SystemModule module) {
		this.id = module.getId();
		this.name = module.getName();
		this.active = module.isActive();
		
		
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

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
    
    
    
}