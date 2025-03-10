package com.tester.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.enuns.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_test_suite", schema = "operational")
public class TestSuite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codeSuite;

	@Enumerated(EnumType.STRING)
	private Status status;
	
	@JsonFormat(pattern = "dd/MM/yyyy") // Adiciona o formato esperado
	 @DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	@ManyToOne
	@JoinColumn(name = "test_plan_id") 
	private TestPlan testPlan;

	@OneToMany(mappedBy = "testSuite", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TestCaseEntity> cases = new HashSet<>();

	public TestSuite() {

	}

	public TestSuite(Long id, Long codeSuite, Status status, LocalDate data, Set<TestCaseEntity> cases) {
		super();
		this.id = id;
		this.codeSuite = codeSuite;
		this.status = status;
		this.data = data;
		this.cases = cases;
		
	}

	public Long getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public LocalDate getData() {
		return data;
	}

	public Set<TestCaseEntity> getCases() {
		return cases;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public TestPlan getTestPlan() {
		return testPlan;
	}

	public void setTestPlan(TestPlan testPlan) {
		this.testPlan = testPlan;
	}

	public Long getCodeSuite() {
		return codeSuite;
	}

	public void setCodeSuite(Long codeSuite) {
		this.codeSuite = codeSuite;
	}

	public void setCases(Set<TestCaseEntity> cases) {
		this.cases = cases;
	}

}
