package com.tester.dto;

import com.tester.entity.TestSuite;

public class LastCodeSuiteDTO {

    private Long id; // ID do TestSuite (opcional, pode ser null se não houver TestSuite)
    private Long codeSuite; // Código da suite (pode ser 0 ou 1 como valor padrão)
    private Long testPlanId; // ID do TestPlan associado

    // Construtor padrão
    public LastCodeSuiteDTO() {
        this.id = null; // ID pode ser null se não houver TestSuite
        this.codeSuite = 0L; // Valor padrão para codeSuite
        this.testPlanId = null; // Inicialmente null, deve ser definido posteriormente
    }

    // Construtor com parâmetros
    public LastCodeSuiteDTO(Long id, Long codeSuite, Long testPlanId) {
        this.id = id;
        this.codeSuite = codeSuite;
        this.testPlanId = testPlanId;
    }

    // Construtor que recebe a entidade TestSuite
    public LastCodeSuiteDTO(TestSuite testSuite) {
        if (testSuite != null) {
            this.id = testSuite.getId();
            this.codeSuite = testSuite.getCodeSuite();
            this.testPlanId = testSuite.getTestPlan() != null ? testSuite.getTestPlan().getId() : null;
        } else {
            this.id = null;
            this.codeSuite = 0L; // Valor padrão se o TestSuite for null
            this.testPlanId = null;
        }
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

    // Método toString para facilitar a depuração
    @Override
    public String toString() {
        return "LastCodeSuiteDTO{" +
                "id=" + id +
                ", codeSuite=" + codeSuite +
                ", testPlanId=" + testPlanId +
                '}';
    }
}