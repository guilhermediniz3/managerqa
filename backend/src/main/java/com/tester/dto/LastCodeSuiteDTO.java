package com.tester.dto;

import com.tester.entity.TestSuite;

public class LastCodeSuiteDTO {
	
	private Long id;
	private Long codeSuite;
    private Long testPlanId; 
	
    // Construtor padrão
    public LastCodeSuiteDTO() {
    }

    // Construtor com parâmetros
    public LastCodeSuiteDTO(Long id, Long codeSuite, Long testPlanId) {
        this.id = id;
        this.codeSuite = codeSuite;
        this.testPlanId = testPlanId;
    }

    // Construtor que recebe a entidade TestSuite
    public LastCodeSuiteDTO(TestSuite testSuite) {
        this.id = testSuite.getId();
        this.codeSuite = testSuite.getCodeSuite();
        this.testPlanId = testSuite.getTestPlan().getId(); 
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodeSuite() {
        return codeSuite;
    }

    public void setCodeSuite(Long codeSuite) {
        this.codeSuite = codeSuite;
    }

    public Long getTestPlanId() {
        return testPlanId;
    }

    public void setTestPlanId(Long testPlanId) {
        this.testPlanId = testPlanId;
    }

}
