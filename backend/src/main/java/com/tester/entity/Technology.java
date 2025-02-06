package com.tester.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="technology",schema="tech")
public class Technology {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
    boolean active ;
    @ManyToMany(mappedBy="technologies")
    private Set<Developer>developers = new HashSet<>();
    
    public Technology() {}

    public Technology(String name, boolean active) {
        this.name = name;
        this.active = active;
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
	public Set<Developer> getDevelopers() {
		return developers;
	}

    
}

