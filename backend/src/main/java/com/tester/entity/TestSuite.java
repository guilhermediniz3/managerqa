package com.tester.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.tester.enuns.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_test_suite", schema = "operational")
public class TestSuite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "informe a descrição da UL")
	private String name;
	@Lob
	private String observation;
	@Enumerated(EnumType.STRING)
	private Status status;
	@NotBlank(message = "informe o número da UL")
	private String Jira;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	private String matriz;
	private String userName;
	private String password;
	// numero do bitrix
	private String calledNumber;
	@ManyToOne
	@JoinColumn(name = "developer_id")
	private Developer developer;
	@ManyToOne
	@JoinColumn(name = "system_module_id")
	private SystemModule systemModule;
	@ManyToOne
	@JoinColumn(name = "tester_id")
	private TesterQA tester;
	@OneToMany(mappedBy = "testSuite", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TestCaseEntity> cases = new HashSet<>();

	public TestSuite() {

	}

	public TestSuite(Long id, String name, String observation, Status status, String jira, LocalDate data,
			String matriz, String user, String password, String calledNumber, Developer developer,
			SystemModule systemModule, TesterQA tester, Set<TestCaseEntity> cases) {

		this.id = id;
		this.name = name;
		this.observation = observation;
		this.status = status;
		this.Jira = jira;
		this.data = data;
		this.matriz = matriz;
		this.userName = user;
		this.password = password;
		this.calledNumber = calledNumber;
		this.developer = developer;
		this.systemModule = systemModule;
		this.tester = tester;
		this.cases = cases;
	}

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

	public String getJira() {
		return Jira;
	}

	public void setJira(String jira) {
		Jira = jira;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getMatriz() {
		return matriz;
	}

	public void setMatriz(String matriz) {
		this.matriz = matriz;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCalledNumber() {
		return calledNumber;
	}

	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	public SystemModule getSystemModule() {
		return systemModule;
	}

	public void setSystemModule(SystemModule systemModule) {
		this.systemModule = systemModule;
	}

	public TesterQA getTester() {
		return tester;
	}

	public void setTester(TesterQA tester) {
		this.tester = tester;
	}

	public Set<TestCaseEntity> getCases() {
		return cases;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
