package com.tester.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.tester.entity.Developer;
import com.tester.entity.Technology;

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

    // Construtor que recebe a entidade Developer
    public DeveloperDTO(Developer developer) {
        this.id = developer.getId();
        this.name = developer.getName();
        this.active = developer.isActive();
        this.technologyIds = developer.getTechnologies().stream()
                .map(Technology::getId)
                .collect(Collectors.toSet());
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Set<Long> getTechnologyIds() { return technologyIds; }
    public void setTechnologyIds(Set<Long> technologyIds) { this.technologyIds = technologyIds; }
}
