package com.tester.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name ="tb_testerQA",schema = "people")
public class TesterQA {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @NotBlank(message = "O nome não pode ser vazio")
	    private String name;
	    private Boolean active;
	    
	    public TesterQA() {}

		public TesterQA(Long id, String name, Boolean active) {
			super();
			this.id = id;
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
		public Boolean isActive() {
			return active;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
}
