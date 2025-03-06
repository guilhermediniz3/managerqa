package com.tester.dto;
import java.time.LocalDate;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.entity.TestPlan;
import com.tester.enuns.Status;
import com.tester.enuns.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import java.lang.Boolean; 

public class TestPlanListagemDTO {

    private Long id;

    @NotBlank(message = "Informe a descrição da UL")
    private String name;
    private Boolean created;
    private String observation;
    private Status status;
    private TaskStatus taskStatus;

    @NotBlank(message = "Informe o número da UL")
    private String jira;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate deliveryData;

    private String matriz;
    private String userName;
    private String callNumber;
    private String developerName; // Nome do Developer
    private String systemModuleName; // Nome do SystemModule
    private String testerQAName; // Nome do TesterQA
    private String password;

    // Carrega somente o ID, evitando carregar todo o objeto
    private Set<Long> testeSuiteId;

    // Construtor com todos os parâmetros
    public TestPlanListagemDTO(Long id, String name,Boolean created, String observation, Status status, TaskStatus taskStatus,
                               String jira, LocalDate data, LocalDate deliveryData, String matriz, String userName,
                               String callNumber, String developerName, String systemModuleName, String testerQAName,
                               String password, Set<Long> testeSuiteId) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.observation = observation;
        this.status = status;
        this.taskStatus = taskStatus;
        this.jira = jira;
        this.data = data;
        this.deliveryData = deliveryData;
        this.matriz = matriz;
        this.userName = userName;
        this.callNumber = callNumber;
        this.developerName = developerName;
        this.systemModuleName = systemModuleName;
        this.testerQAName = testerQAName;
        this.password = password;
        this.testeSuiteId = testeSuiteId;
    }

    // Construtor que inicializa a partir de um TestPlan
    public TestPlanListagemDTO(TestPlan testPlan) {
        this.id = testPlan.getId();
        this.name = testPlan.getName();
        this.created = testPlan.getCreated();
        this.observation = testPlan.getObservation();
        this.status = testPlan.getStatus();
        this.taskStatus = testPlan.getTaskStatus();
        this.jira = testPlan.getJira();
        this.data = testPlan.getData();
        this.deliveryData = testPlan.getDeliveryData();
        this.matriz = testPlan.getMatriz();
        this.userName = testPlan.getUserName();
        this.callNumber = testPlan.getCallNumber();
        this.password = testPlan.getPassword();
        this.developerName = testPlan.getDeveloper() != null ? testPlan.getDeveloper().getName() : null;
        this.systemModuleName = testPlan.getSystemModule() != null ? testPlan.getSystemModule().getName() : null;
        this.testerQAName = testPlan.getTester() != null ? testPlan.getTester().getName() : null;

        // Verifique se o TesteSuite não é nulo antes de acessar stream()
        this.testeSuiteId = testPlan.getTesteSuite() != null
                ? testPlan.getTesteSuite().stream().map(testSuite -> testSuite.getId()).collect(Collectors.toSet())
                : null;
    }

    // Getters e Setters (gerados automaticamente ou manualmente)

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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getJira() {
        return jira;
    }

    public void setJira(String jira) {
        this.jira = jira;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalDate getDeliveryData() {
        return deliveryData;
    }

    public void setDeliveryData(LocalDate deliveryData) {
        this.deliveryData = deliveryData;
    }

    public String getMatriz() {
        return matriz;
    }

    public void setMatriz(String matriz) {
        this.matriz = matriz;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getSystemModuleName() {
        return systemModuleName;
    }

    public void setSystemModuleName(String systemModuleName) {
        this.systemModuleName = systemModuleName;
    }

    public String getTesterQAName() {
        return testerQAName;
    }

    public void setTesterQAName(String testerQAName) {
        this.testerQAName = testerQAName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getTesteSuiteId() {
        return testeSuiteId;
    }

    public void setTesteSuiteId(Set<Long> testeSuiteId) {
        this.testeSuiteId = testeSuiteId;
    }

	public Boolean getCreated() {
		return created;
	}

	public void setCreated(Boolean created) {
		this.created = created;
	}
}
