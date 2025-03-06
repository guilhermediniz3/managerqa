package com.tester.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.enuns.Status;
import com.tester.enuns.TaskStatus;

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
import jakarta.validation.constraints.NotBlank;
import java.lang.Boolean; 
@Entity
@Table(name="tb_test_plan",schema="operational")
public class TestPlan {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "informe a descrição da UL")
	private String name;
    private Boolean created;
	private String observation;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus;
	@NotBlank(message = "informe o número da UL")
	private String jira;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	@JsonFormat(pattern = "dd/MM/yyyy")	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deliveryData;
	private String matriz;
	private String userName;
	private String password;
	// numero do bitrix
	private String callNumber;
	@ManyToOne
	@JoinColumn(name = "developer_id")
	private Developer developer;
	@ManyToOne
	@JoinColumn(name = "system_module_id")
	private SystemModule systemModule;
	@ManyToOne
	@JoinColumn(name = "tester_id")
	private TesterQA tester;

	 @OneToMany(mappedBy = "testPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestSuite> testeSuite = new HashSet<>();	
	 
	public TestPlan() {
		
	}


	public TestPlan(Long id, String name, Boolean created, String observation,
			Status status, String jira, LocalDate data,
			LocalDate deliveryData, String matriz, String userName, String password, String callNumber,
			Developer developer, SystemModule systemModule, TesterQA tester, Set<TestSuite> testeSuite) {
	
		this.id = id;
		this.name = name;
		this.created = created;
		this.observation = observation;
		this.status = status;
		this.jira = jira;
		this.data = data;
		this.deliveryData = deliveryData;
		this.matriz = matriz;
		this.userName = userName;
		this.password = password;
		this.callNumber = callNumber;
		this.developer = developer;
		this.systemModule = systemModule;
		this.tester = tester;
		this.testeSuite = testeSuite;
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


	public String getPassword() {
		return password;
	}


	public String getCallNumber() {
		return callNumber;
	}


	public Developer getDeveloper() {
		return developer;
	}


	public SystemModule getSystemModule() {
		return systemModule;
	}


	public TesterQA getTester() {
		return tester;
	}


	public Set<TestSuite> getTesteSuite() {
		return testeSuite;
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


	public void setPassword(String password) {
		this.password = password;
	}


	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}


	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}


	public void setSystemModule(SystemModule systemModule) {
		this.systemModule = systemModule;
	}


	public void setTester(TesterQA tester) {
		this.tester = tester;
	}


	public TaskStatus getTaskStatus() {
		return taskStatus;
	}


	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}


	public void setTesteSuite(Set<TestSuite> testeSuite) {
		this.testeSuite = testeSuite;
	}


	public Boolean getCreated() {
		return created;
	}


	public void setCreated(Boolean created) {
		this.created = created;
	}



	
	


}
