package com.tester.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.entity.TestCaseEntity;
import com.tester.entity.TestSuite;
import com.tester.enuns.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

public class TestCaseDTO {

	private Long id;

	private Long codeCase;
	@NotBlank(message = "O nome não pode ser vazio")
	private String scenario;
	private String expectedResult;
	private String obtainedResult;
	private String videoEvidence;
	private Status status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	private Long testSuiteId;
	private Long testPlanId;

	public TestCaseDTO() {

	}

	public TestCaseDTO(Long id, Long codeCase, String scenario, String expectedResult, String obtainedResult,
			String videoEvidence, Status status, LocalDate data, Long testSuiteId) {

		this.id = id;
		this.codeCase = codeCase;
		this.scenario = scenario;
		this.expectedResult = expectedResult;
		this.obtainedResult = obtainedResult;
		this.videoEvidence = videoEvidence;
		this.status = status;
		this.data = data;
		this.testSuiteId = testSuiteId;
	}

	public TestCaseDTO(TestCaseEntity testCase) {
		this.id = testCase.getId();
		this.codeCase = testCase.getCodeCase();
		this.scenario = testCase.getScenario();
		this.expectedResult = testCase.getExpectedResult();
		this.obtainedResult = testCase.getObtainedResult();
		this.videoEvidence = testCase.getVideoEvidence();
		this.status = testCase.getStatus();
		this.data = testCase.getData();
		this.testSuiteId = testCase.getTestSuite().getId();

	}

	public TestCaseDTO(Long id, String scenario, String expectedResult, String obtainedResult, String videoEvidence,
			Status status, LocalDate data, Long testSuiteId, Long testPlanId) {
		this.id = id;
		this.scenario = scenario;
		this.expectedResult = expectedResult;
		this.obtainedResult = obtainedResult;
		this.videoEvidence = videoEvidence;
		this.status = status;
		this.data = data;
		this.testSuiteId = testSuiteId;
		this.testPlanId = testPlanId;
	}

	public Long getId() {
		return id;
	}

	public String getScenario() {
		return scenario;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public String getObtainedResult() {
		return obtainedResult;
	}

	public String getVideoEvidence() {
		return videoEvidence;
	}

	public Status getStatus() {
		return status;
	}

	public LocalDate getData() {
		return data;
	}

	public Long getTestSuiteId() {
		return testSuiteId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public void setObtainedResult(String obtainedResult) {
		this.obtainedResult = obtainedResult;
	}

	public void setVideoEvidence(String videoEvidence) {
		this.videoEvidence = videoEvidence;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Long getCodeCase() {
		return codeCase;
	}

	public void setCodeCase(Long codeCase) {
		this.codeCase = codeCase;
	}

	public void setTestSuiteId(Long testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public Long getTestPlanId() {
		return testPlanId;
	}

	public void setTestPlanId(Long testPlanId) {
		this.testPlanId = testPlanId;
	}

}
