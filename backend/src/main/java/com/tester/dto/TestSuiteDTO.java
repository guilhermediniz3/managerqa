package com.tester.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.entity.TestSuite;
import com.tester.enuns.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TestSuiteDTO {

	private Long id;
	private Long codeSuite;
   @Enumerated(EnumType.STRING)
	private Status status;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	private Long testPlanId;
	private Set<Long> TestCaseId = new HashSet<>();
	
	public TestSuiteDTO() {
		
	}
	
	   // Construtor para a consulta JPQL
    public TestSuiteDTO(Long id, Long codeSuite, Status status, LocalDate data, Long testPlanId) {
        this.id = id;
        this.codeSuite = codeSuite;
        this.status = status;
        this.data = data;
        this.testPlanId = testPlanId;
        this.TestCaseId = new HashSet<>(); // Inicializa o conjunto de IDs de casos de teste como vazio
    }


	public TestSuiteDTO(Long id,Long codeSuite, Status status, LocalDate data, Long testPlanId, Set<Long> testCaseId) {
		
		this.id = id;
		this.codeSuite =codeSuite;
		this.status = status;
		this.data = data;
		this.testPlanId = testPlanId;
		TestCaseId = testCaseId;
	}
	
	public TestSuiteDTO(TestSuite testSuite) {
		
		this.id = testSuite.getId();
		this.codeSuite = testSuite.getCodeSuite();
		this.status = testSuite.getStatus();
		this.data = testSuite.getData();
		this.testPlanId = testSuite.getTestPlan().getId(); 	// composição
		this.TestCaseId = testSuite.getCases().stream().map(testCase -> testCase.getId()).collect(Collectors.toSet());
		
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


	public Long getTestPlanId() {
		return testPlanId;
	}


	public Set<Long> getTestCaseId() {
		return TestCaseId;
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


	public void setTestPlanId(Long testPlanId) {
		this.testPlanId = testPlanId;
	}


	public void setTestCaseId(Set<Long> testCaseId) {
		TestCaseId = testCaseId;
	}


	public Long getCodeSuite() {
		return codeSuite;
	}


	public void setCodeSuite(Long codeSuite) {
		this.codeSuite = codeSuite;
	}
	
	
	
	

}
