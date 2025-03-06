package com.tester.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.tester.entity.TestPlan;
import com.tester.enuns.Status;
import com.tester.enuns.TaskStatus;

public class TestPlanFilterDTO {
	private Long id;
	private String name;
	private Boolean created;
	private String observation;
	private Status status;
	private TaskStatus taskStatus;
	private String jira;
	private LocalDate data;
	private LocalDate deliveryData;
	private String matriz;
	private String userName;
	private String callNumber;
	private Long developerId;
	private Long systemModuleId;
	private Long testerId;

	private Set<Long> testeSuiteId;

	public TestPlanFilterDTO() {
	}

	public TestPlanFilterDTO(Long id, String name, Boolean created, String observation, Status status, TaskStatus taskStatus,
			String jira, LocalDate data, LocalDate deliveryData, String matriz, String userName, String callNumber,
			Long developerId, Long systemModuleId, Long testerId, Set<Long> testeSuiteId) {
		super();
		this.id = id;
		this.name = name;
		this.observation = observation;
		this.status = status;
		this.taskStatus = taskStatus;
		this.jira = jira;
		this.data = data;
		this.deliveryData = deliveryData;
		this.matriz = matriz;
		this.userName = userName;
		this.callNumber = callNumber;
		this.developerId = developerId;
		this.systemModuleId = systemModuleId;
		this.testerId = testerId;
		this.testeSuiteId = testeSuiteId;
		this.created = created;
	}

	// Construtor que corresponde aos parâmetros da query

	public TestPlanFilterDTO(TestPlan testPlan) {
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

		// Verifique se o Developer não é nulo antes de acessar getId()
		this.developerId = testPlan.getDeveloper() != null ? testPlan.getDeveloper().getId() : null;

		// Verifique se o SystemModule não é nulo antes de acessar getId()
		this.systemModuleId = testPlan.getSystemModule() != null ? testPlan.getSystemModule().getId() : null;

		// Verifique se o Tester não é nulo antes de acessar getId()
		this.testerId = testPlan.getTester() != null ? testPlan.getTester().getId() : null;

		// Verifique se o TesteSuite não é nulo antes de acessar stream()
		this.testeSuiteId = testPlan.getTesteSuite() != null
				? testPlan.getTesteSuite().stream().map(testSuite -> testSuite.getId()).collect(Collectors.toSet())
				: null;
	}

	public TestPlanFilterDTO(Long id, String name, String observation, Status status, TaskStatus taskStatus,
			String jira, LocalDate data, LocalDate deliveryData, String matriz, String userName, String callNumber,
			Long developerId, Long systemModuleId, Long testerId) {
		this.id = id;
		this.name = name;
		this.observation = observation;
		this.status = status;
		this.taskStatus = taskStatus;
		this.jira = jira;
		this.data = data;
		this.deliveryData = deliveryData;
		this.matriz = matriz;
		this.userName = userName;
		this.callNumber = callNumber;
		this.developerId = developerId;
		this.systemModuleId = systemModuleId;
		this.testerId = testerId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getObservation() {
		return observation;
	}

	public Status getStatus() {
		return status;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public String getJira() {
		return jira;
	}

	public LocalDate getData() {
		return data;
	}

	public LocalDate getDeliveryData() {
		return deliveryData;
	}

	public String getMatriz() {
		return matriz;
	}

	public String getUserName() {
		return userName;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public Long getDeveloperId() {
		return developerId;
	}

	public Long getSystemModuleId() {
		return systemModuleId;
	}

	public Long getTesterId() {
		return testerId;
	}

	public Set<Long> getTesteSuiteId() {
		return testeSuiteId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public void setJira(String jira) {
		this.jira = jira;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public void setDeliveryData(LocalDate deliveryData) {
		this.deliveryData = deliveryData;
	}

	public void setMatriz(String matriz) {
		this.matriz = matriz;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}

	public void setSystemModuleId(Long systemModuleId) {
		this.systemModuleId = systemModuleId;
	}

	public void setTesterId(Long testerId) {
		this.testerId = testerId;
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
