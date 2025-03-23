package com.tester.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tester.dto.CreatedByDTO;
import com.tester.dto.LastCodeSuiteDTO;
import com.tester.dto.TestPlanDTO;
import com.tester.dto.TestPlanListagemDTO;
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
	@Autowired
	private TestSuiteRepository TestSuiteRepository;

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
		testPlan.setCreated(testPlanDTO.getCreated());

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
	    testPlan.setCreated(testPlanDTO.getCreated()); // Certifique-se de que isso está presente
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

	        // Limpa a coleção existente
	        testPlan.getTesteSuite().clear();

	        // Adiciona as novas TestSuites
	        testPlan.getTesteSuite().addAll(testSuites);

	        // Atualiza o lado inverso do relacionamento (TestSuite -> TestPlan)
	        for (TestSuite suite : testSuites) {
	            suite.setTestPlan(testPlan);
	        }
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
	
	

    public Page<TestPlanListagemDTO> findAllTestPlans(
            String name, String observation, String status, String taskStatus,
            String jira, LocalDate dataInicio, LocalDate dataFim,
            LocalDate deliveryDataInicio, LocalDate deliveryDataFim,
            String matriz, String userName, String callNumber,
            String developerName, String systemModuleName, String testerQAName,
            Pageable pageable) {

        // Busca os TestPlans do banco de dados
        Page<TestPlan> testPlans = testPlanRepository.findAllTestPlans(
                name, observation, status, taskStatus, jira,
                dataInicio, dataFim, deliveryDataInicio, deliveryDataFim,
                matriz, userName, callNumber, developerName, systemModuleName, testerQAName,
                pageable
        );

        // Mapeia os TestPlans para TestPlanListagemDTO
        return testPlans.map(testPlan -> new TestPlanListagemDTO(testPlan));
    }
    public TestPlanDTO patchTestPlan(Long id, TestPlanDTO testPlanDTO) {
        // Buscando o TestPlan pelo ID
        TestPlan testPlan = testPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano de teste não encontrado"));

        // Atualizando os campos apenas se não forem nulos no DTO
        if (testPlanDTO.getName() != null) {
            testPlan.setName(testPlanDTO.getName());
        }
        if (testPlanDTO.getCreated() != null) { // Certifique-se de que isso está presente
            testPlan.setCreated(testPlanDTO.getCreated());
        }
        if (testPlanDTO.getObservation() != null) {
            testPlan.setObservation(testPlanDTO.getObservation());
        }
        if (testPlanDTO.getStatus() != null) {
            testPlan.setStatus(testPlanDTO.getStatus());
        }
        if (testPlanDTO.getTaskStatus() != null) {
            testPlan.setTaskStatus(testPlanDTO.getTaskStatus());
        }
        if (testPlanDTO.getJira() != null) {
            testPlan.setJira(testPlanDTO.getJira());
        }
        if (testPlanDTO.getData() != null) {
            testPlan.setData(testPlanDTO.getData());
        }
        if (testPlanDTO.getDeliveryData() != null) {
            testPlan.setDeliveryData(testPlanDTO.getDeliveryData());
        }
        if (testPlanDTO.getMatriz() != null) {
            testPlan.setMatriz(testPlanDTO.getMatriz());
        }
        if (testPlanDTO.getUserName() != null) {
            testPlan.setUserName(testPlanDTO.getUserName());
        }
        if (testPlanDTO.getPassword() != null) {
            testPlan.setPassword(testPlanDTO.getPassword());
        }
        if (testPlanDTO.getCallNumber() != null) {
            testPlan.setCallNumber(testPlanDTO.getCallNumber());
        }

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
            // Busca as novas TestSuites
            Set<TestSuite> testSuites = new HashSet<>(testSuiteRepository.findAllById(testPlanDTO.getTesteSuiteId()));

            if (testSuites.size() != testPlanDTO.getTesteSuiteId().size()) {
                throw new ResourceNotFoundException("Uma ou mais TestSuites não foram encontradas");
            }

            // Limpa a coleção existente
            testPlan.getTesteSuite().clear();

            // Adiciona as novas TestSuites
            testPlan.getTesteSuite().addAll(testSuites);

            // Atualiza o lado inverso do relacionamento (TestSuite -> TestPlan)
            for (TestSuite suite : testSuites) {
                suite.setTestPlan(testPlan);
            }
        }

        // Salvando atualização
        TestPlan updatedTestPlan = testPlanRepository.save(testPlan);
        return new TestPlanDTO(updatedTestPlan);
    }
    public LastCodeSuiteDTO getLastCodeSuiteByTestPlanId(Long testPlanId) {
        // Busca o último TestSuite associado ao testPlanId
        Optional<TestSuite> testSuiteOptional = testSuiteRepository.findLastTestSuiteByTestPlanId(testPlanId);

        // Se não houver TestSuite, retorna um valor padrão
        if (testSuiteOptional.isEmpty()) {
            LastCodeSuiteDTO lastCodeSuiteDTO = new LastCodeSuiteDTO();
            lastCodeSuiteDTO.setCodeSuite(0L); // Valor padrão
            lastCodeSuiteDTO.setTestPlanId(testPlanId); // Define o testPlanId
            return lastCodeSuiteDTO;
        }

        // Se houver TestSuite, retorna o DTO com os dados
        TestSuite testSuite = testSuiteOptional.get();
        LastCodeSuiteDTO lastCodeSuiteDTO = new LastCodeSuiteDTO(testSuite);
        return lastCodeSuiteDTO;
    }

    
    
    // EXIBIR QUEM CRIOU A TAREFA (UL)
    public CreatedByDTO getCreatedByByTestPlanId(Long testPlanId) {
        return testPlanRepository.findCreatedByByTestPlanId(testPlanId);
    }
    

}
