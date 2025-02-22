package com.tester.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.TestPlanDTO;
import com.tester.entity.Developer;
import com.tester.entity.SystemModule;
import com.tester.entity.TestPlan;
import com.tester.entity.TestSuite;
import com.tester.entity.TesterQA;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.DeveloperRepository;
import com.tester.repository.SystemModuleRepository;
import com.tester.repository.TestPlanRepository;
import com.tester.repository.TestSuiteRepository;
import com.tester.repository.TesterQARepository;

@Service
public class TestPlanService {

	@Autowired
	private TestPlanRepository testPlanRepository;

	@Autowired
	private TestSuiteRepository testSuiteRepository;

	@Autowired
	private DeveloperRepository developerRepository;

	@Autowired
	private SystemModuleRepository systemModuleRepository;

	@Autowired
	private TesterQARepository testerRepository;

	public TestPlanDTO createTestPlan(TestPlanDTO testPlanDTO) {
		// Criando a entidade a partir do DTO
		TestPlan testPlan = new TestPlan();
		testPlan.setName(testPlanDTO.getName());
		testPlan.setObservation(testPlanDTO.getObservation());
		testPlan.setStatus(testPlanDTO.getStatus());
		testPlan.setTaskStatus(testPlanDTO.getTaskStatus());
		testPlan.setJira(testPlanDTO.getJira());
		testPlan.setData(testPlanDTO.getData());
		testPlan.setDeliveryData(testPlanDTO.getDeliveryData());
		testPlan.setMatriz(testPlanDTO.getMatriz());
		testPlan.setUserName(testPlanDTO.getUserName());
		testPlan.setPassword(testPlanDTO.getPassword());
		testPlan.setCallNumber(testPlanDTO.getCallNumber());

		// Mapeando relacionamentos a partir dos IDs do DTO
		if (testPlanDTO.getDeveloperId() != null) {
			Developer developer = developerRepository.findById(testPlanDTO.getDeveloperId())
					.orElseThrow(() -> new ResourceNotFoundException("Desenvolvedor não encontrado"));
			testPlan.setDeveloper(developer);
		}

		if (testPlanDTO.getSystemModuleId() != null) {
			SystemModule systemModule = systemModuleRepository.findById(testPlanDTO.getSystemModuleId())
					.orElseThrow(() -> new ResourceNotFoundException("Módulo do sistema não encontrado"));
			testPlan.setSystemModule(systemModule);
		}

		if (testPlanDTO.getTesterId() != null) {
			TesterQA tester = testerRepository.findById(testPlanDTO.getTesterId())
					.orElseThrow(() -> new ResourceNotFoundException("Tester não encontrado"));
			testPlan.setTester(tester);
		}

		if (testPlanDTO.getTesteSuiteId() != null && !testPlanDTO.getTesteSuiteId().isEmpty()) {
			Set<TestSuite> testSuites = new HashSet<>(testSuiteRepository.findAllById(testPlanDTO.getTesteSuiteId()));

			if (testSuites.size() != testPlanDTO.getTesteSuiteId().size()) {
				throw new ResourceNotFoundException("Uma ou mais TestSuites não foram encontradas");
			}

			testPlan.setTesteSuite(testSuites);
		}

		// Salvando no banco
		TestPlan savedTestPlan = testPlanRepository.save(testPlan);

		// Retornando um DTO atualizado com os dados persistidos
		return new TestPlanDTO(savedTestPlan);
	}

	public TestPlanDTO updateTestPlan(Long id, TestPlanDTO testPlanDTO) {
		// Buscando o TestPlan pelo ID
		TestPlan testPlan = testPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plano de teste não encontrado"));

		// Atualizando os campos
		testPlan.setName(testPlanDTO.getName());
		testPlan.setObservation(testPlanDTO.getObservation());
		testPlan.setStatus(testPlanDTO.getStatus());
		testPlan.setTaskStatus(testPlanDTO.getTaskStatus());
		testPlan.setJira(testPlanDTO.getJira());
		testPlan.setData(testPlanDTO.getData());
		testPlan.setDeliveryData(testPlanDTO.getDeliveryData());
		testPlan.setMatriz(testPlanDTO.getMatriz());
		testPlan.setUserName(testPlanDTO.getUserName());
		testPlan.setPassword(testPlanDTO.getPassword());
		testPlan.setCallNumber(testPlanDTO.getCallNumber());

		// Atualizando relacionamentos
		if (testPlanDTO.getDeveloperId() != null) {
			Developer developer = developerRepository.findById(testPlanDTO.getDeveloperId())
					.orElseThrow(() -> new ResourceNotFoundException("Desenvolvedor não encontrado"));
			testPlan.setDeveloper(developer);
		}

		if (testPlanDTO.getSystemModuleId() != null) {
			SystemModule systemModule = systemModuleRepository.findById(testPlanDTO.getSystemModuleId())
					.orElseThrow(() -> new ResourceNotFoundException("Módulo do sistema não encontrado"));
			testPlan.setSystemModule(systemModule);
		}

		if (testPlanDTO.getTesterId() != null) {
			TesterQA tester = testerRepository.findById(testPlanDTO.getTesterId())
					.orElseThrow(() -> new ResourceNotFoundException("Tester não encontrado"));
			testPlan.setTester(tester);
		}

		if (testPlanDTO.getTesteSuiteId() != null && !testPlanDTO.getTesteSuiteId().isEmpty()) {
			Set<TestSuite> testSuites = new HashSet<>(testSuiteRepository.findAllById(testPlanDTO.getTesteSuiteId()));

			if (testSuites.size() != testPlanDTO.getTesteSuiteId().size()) {
				throw new ResourceNotFoundException("Uma ou mais TestSuites não foram encontradas");
			}

			testPlan.setTesteSuite(testSuites);
		}

		// Salvando atualização
		TestPlan updatedTestPlan = testPlanRepository.save(testPlan);
		return new TestPlanDTO(updatedTestPlan);
	}

	public void deleteTestPlan(Long id) {
		TestPlan testPlan = testPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plano de teste não encontrado"));
		testPlanRepository.delete(testPlan);
	}

	public List<TestPlanDTO> getAllTestPlans() {
		List<TestPlan> testPlans = testPlanRepository.findAll();
		return testPlans.stream().map(TestPlanDTO::new).collect(Collectors.toList());
	}

	public TestPlanDTO getTestPlanById(Long id) {
		TestPlan testPlan = testPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plano de teste não encontrado"));
		return new TestPlanDTO(testPlan);
	}
	 // Método para buscar todos os detalhes dos planos de teste com paginação
    public Page<TestPlanDTO> findAllTestPlanDetails(Pageable pageable) {
        return testPlanRepository.findAllTestPlanDetails(pageable);
    }

    // Método para buscar detalhes filtrados com OR e paginação
    public Page<TestPlanDTO> findAllTestPlanDetailsWithOrFilters(
            String testerName,
            String jira,
            String callNumber,
            String status,
            String developerName,
            String moduleName,
            Pageable pageable) {
        return testPlanRepository.findAllTestPlanDetailsWithOrFilters(
                testerName, jira, callNumber, status, developerName, moduleName, pageable);
    }

}
