package com.tester.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.tester.entity.Developer;
import com.tester.entity.Technology;

public class DeveloperDTO {
    private Long id;
    private String name;
    private Boolean active;
    private Set<Long> technologyIds;


    public DeveloperDTO() {}


    public DeveloperDTO(Long id, String name, Boolean active, Set<Long> technologyIds) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.technologyIds = technologyIds;
    }

   
    public DeveloperDTO(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.active = developer.isActive();
        this.technologyIds = developer.getTechnologies().stream()
                .map(technology -> technology.getId())  
                .collect(Collectors.toSet());
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Set<Long> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(Set<Long> technologyIds) { this.technologyIds = technologyIds; }
}
