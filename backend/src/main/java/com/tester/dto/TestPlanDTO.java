	package com.tester.dto;
	
	import java.time.LocalDate;
	import java.util.HashSet;
	import java.util.Set;
	import java.util.stream.Collectors;
	
	import org.springframework.format.annotation.DateTimeFormat;
	
	import com.tester.entity.TestPlan;
	import com.tester.entity.TestSuite;
	import com.tester.enuns.Status;
	import com.tester.enuns.TaskStatus;
	
	import jakarta.validation.constraints.NotBlank;
	
	public class TestPlanDTO {
		private Long id;
		@NotBlank(message = "Informe a descrição da UL")
		private String name;
	
		private String observation;
	
		private Status status;
		private TaskStatus taskStatus;
	
		@NotBlank(message = "Informe o número da UL")
		private String jira;
	
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private LocalDate data;
	
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private LocalDate deliveryData;
		private String matriz;
		private String userName;
		private String callNumber;
		private Long developerId;
		private Long systemModuleId;
		private Long testerId;
		private String password;
		// carrega somente o id evitando carregar todo o objeto
		private Set<Long> testeSuiteId;
	
		TestPlanDTO() {
	
		}
	
	
		public TestPlanDTO(Long id, @NotBlank(message = "Informe a descrição da UL") String name, String observation,
				Status status, TaskStatus taskStatus, @NotBlank(message = "Informe o número da UL") String jira,
				LocalDate data, LocalDate deliveryData, String matriz, String userName, String callNumber, Long developerId,
				Long systemModuleId, Long testerId, String password, Set<Long> testeSuiteId) {
		
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
			this.password = password;
			this.callNumber = callNumber;
			this.developerId = developerId;
			this.systemModuleId = systemModuleId;
			this.testerId = testerId;
			this.testeSuiteId = testeSuiteId;
		}
	
	
	
	
	
	
	
		public TestPlanDTO(TestPlan testPlan) {
			this.id = testPlan.getId();
			this.name = testPlan.getName();
			this.observation = testPlan.getObservation();
			this.status = testPlan.getStatus();
			this.taskStatus = testPlan.getTaskStatus();
			this.jira = testPlan.getJira();
			this.data = testPlan.getData();
			this.deliveryData = testPlan.getDeliveryData();
			this.matriz = testPlan.getMatriz();
			this.userName = testPlan.getUserName();
			this.callNumber = testPlan.getCallNumber();
			this.developerId = testPlan.getDeveloper().getId();
			this.systemModuleId = testPlan.getSystemModule().getId();
			this.testerId = testPlan.getTester().getId();
			this.testeSuiteId = testPlan.getTesteSuite().stream().map(testSuit->testSuit.getId()).collect(Collectors.toSet());
	
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
