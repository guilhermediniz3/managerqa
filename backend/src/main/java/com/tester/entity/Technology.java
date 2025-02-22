package com.tester.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="tb_technology",schema="tech")
public class Technology {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	   @NotBlank(message = "O nome não pode ser vazio")
	    private String name;
	    private Boolean active;

	    @ManyToMany(mappedBy = "technologies")
	    private Set<Developer> developers = new HashSet<>();

	    // Construtor padrão
	    public Technology() {}

	    // Construtor com parâmetros
	    public Technology(String name, Boolean active) {
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


		public Boolean isActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		// Getter para developers (retorna uma cópia defensiva)
	    public Set<Developer> getDevelopers() {
	        return new HashSet<>(developers);
	    }

	    // Método package-private para adicionar um desenvolvedor
	    void internalAddDeveloper(Developer developer) {
	        this.developers.add(developer);
	    }

	    // Método package-private para remover um desenvolvedor
	    void internalRemoveDeveloper(Developer developer) {
	        this.developers.remove(developer);
	    }
}

