

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

@Entity
@Table(name="tb_developer",schema = "people")
public class Developer {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String name;
	    private boolean active;

	    @ManyToMany
	    @JoinTable(
	        name = "developer_technology",
	        joinColumns = @JoinColumn(name = "developer_id"),
	        inverseJoinColumns = @JoinColumn(name = "technology_id")
	    )
	    private Set<Technology> technologies = new HashSet<>();

	    // Construtor padrão
	    public Developer() {}

	    // Construtor com parâmetros
	    public Developer(String name, boolean active) {
	        this.name = name;
	        this.active = active;
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

	    // Getter para technologies (retorna uma cópia defensiva)
	    public Set<Technology> getTechnologies() {
	        return new HashSet<>(technologies);
	    }

	    // Método para adicionar uma tecnologia
	    public void addTechnology(Technology technology) {
	        this.technologies.add(technology);
	        technology.getDevelopers().add(this); // Atualiza o lado inverso da relação
	    }

	    // Método para remover uma tecnologia
	    public void removeTechnology(Technology technology) {
	        this.technologies.remove(technology);
	        technology.getDevelopers().remove(this); // Atualiza o lado inverso da relação
	    }
}