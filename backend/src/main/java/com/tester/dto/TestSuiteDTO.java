package com.tester.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.tester.entity.TestSuite;
import com.tester.enuns.Status;

public class TestSuiteDTO {

	private Long id;

	private Status status;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	private Long testPlanId;
	private Set<Long> TestCaseId;
	
	public TestSuiteDTO() {
		
	}


	public TestSuiteDTO(Long id, Status status, LocalDate data, Long testPlanId, Set<Long> testCaseId) {
		
		this.id = id;
		this.status = status;
		this.data = data;
		this.testPlanId = testPlanId;
		TestCaseId = testCaseId;
	}
	
	public TestSuiteDTO(TestSuite testSuite) {
		
		this.id = testSuite.getId();
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
	
	
	
	

}
