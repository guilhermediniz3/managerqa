package com.tester.dto;

import com.tester.entity.TesterQA;

public class TesterQADTO {

    private Long id;

    private String name;
    private boolean active;
    
    public TesterQADTO() {}

;

	public TesterQADTO(Long id, String name, boolean active) {
	
		this.id = id;
		this.name = name;
		this.active = active;
	}
	
	public TesterQADTO(TesterQA testerQA) {
		this.id = testerQA.getId();
		this.name = testerQA.getName();
		this.active = testerQA.isActive();
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
