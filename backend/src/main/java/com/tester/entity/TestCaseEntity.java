package com.tester.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.enuns.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name="tb_test_case",schema="operational")
public class TestCaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "code_case")
	private Long codeCase;
	@NotBlank(message = "O nome não pode ser vazio")
	private String scenario;
	private String expectedResult;
	private String obtainedResult;
	private String videoEvidence;
	@Enumerated(EnumType.STRING)
	private Status status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	@ManyToOne
	@JoinColumn(name = "test_suite_id")
	private TestSuite testSuite;
	
	public TestCaseEntity() {
		
	}
	


	public TestCaseEntity(Long id,Long codeCase,String scenario,
			String expectedResult, String obtainedResult, String videoEvidence, Status status, LocalDate data,
			TestSuite testSuite) {
		
		this.id = id;
		this.codeCase = codeCase;
		this.scenario = scenario;
		this.expectedResult = expectedResult;
		this.obtainedResult = obtainedResult;
		this.videoEvidence = videoEvidence;
		this.status = status;
		this.data = data;
		this.testSuite = testSuite;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getObtainedResult() {
		return obtainedResult;
	}

	public void setObtainedResult(String obtainedResult) {
		this.obtainedResult = obtainedResult;
	}

	public String getVideoEvidence() {
		return videoEvidence;
	}

	public void setVideoEvidence(String videoEvidence) {
		this.videoEvidence = videoEvidence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public TestSuite getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}



	public Long getCodeCase() {
		return codeCase;
	}



	public void setCodeCase(Long codeCase) {
		this.codeCase = codeCase;
	}

}
