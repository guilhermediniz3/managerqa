package com.tester.dto;

import com.tester.entity.TesterQA;

public class TesterQADTO {

    private Long id;

    private String name;
    private Boolean active;
    
    public TesterQADTO() {}

;

	public TesterQADTO(Long id, String name, Boolean active) {
	
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
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}


}
