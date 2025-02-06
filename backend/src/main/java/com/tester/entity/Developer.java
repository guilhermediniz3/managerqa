package com.tester.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="developer",schema="people")
public class Developer {
	  @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	  @NotNull(message = "O nome é obrigatório") 
	private String name;
	boolean active;
	@ManyToMany
	@JoinTable(name="developer_technology",
	joinColumns = @JoinColumn(name="developer_id"),
	inverseJoinColumns = @JoinColumn(name="technology_id")
	)
	Set<Technology> technologies = new HashSet<>();
	  
    public Developer() {}

    public Developer(String name, boolean active) {
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
	public Set<Technology> getTechnologies() {
		return technologies;
	}

	

}
