package com.tester.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.TestSuiteDTO;
import com.tester.entity.TestCaseEntity;
import com.tester.entity.TestPlan;
import com.tester.entity.TestSuite;
import com.tester.repository.TestCaseRepository;
import com.tester.repository.TestPlanRepository;
import com.tester.repository.TestSuiteRepository;

@Service
public class TestSuiteService {

	@Autowired
	private TestSuiteRepository testSuiteRepository;

	@Autowired
	private TestPlanRepository testPlanRepository;

	@Autowired
	private TestCaseRepository testCaseRepository;

	// Criar um novo TestSuite
	public TestSuiteDTO create(TestSuiteDTO dto) {
		TestSuite testSuite = new TestSuite();
		testSuite.setStatus(dto.getStatus());
		testSuite.setData(dto.getData());
		testSuite.setCodeSuite(dto.getCodeSuite());

		// Buscar e setar o TestPlan
		Optional<TestPlan> testPlanOpt = testPlanRepository.findById(dto.getTestPlanId());
		if (!testPlanOpt.isPresent()) {
			throw new RuntimeException("TestPlan não encontrado com ID: " + dto.getTestPlanId());
		}
		testSuite.setTestPlan(testPlanOpt.get());

		// Buscar e setar os TestCases
		Set<TestCaseEntity> testCases = dto.getTestCaseId().stream()
				.map(testCaseId -> testCaseRepository.findById(testCaseId)
						.orElseThrow(() -> new RuntimeException("TestCase não encontrado com ID: " + testCaseId)))
				.collect(Collectors.toSet());
		testSuite.setCases(testCases);

		// Salvar no repositório
		testSuite = testSuiteRepository.save(testSuite);
		return new TestSuiteDTO(testSuite);
	}

	// Buscar todos os TestSuites
	public List<TestSuiteDTO> findAll() {
		List<TestSuite> testSuites = testSuiteRepository.findAll();
		return testSuites.stream().map(testSuite -> new TestSuiteDTO(testSuite)).collect(Collectors.toList());
	}

	// Buscar um TestSuite por ID
	public TestSuiteDTO findById(Long id) {
		TestSuite testSuite = testSuiteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("TestSuite não encontrado com ID: " + id));
		return new TestSuiteDTO(testSuite);
	}

	// Atualizar um TestSuite
	public TestSuiteDTO update(Long id, TestSuiteDTO dto) {
		TestSuite testSuite = testSuiteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("TestSuite não encontrado com ID: " + id));

		testSuite.setStatus(dto.getStatus());
		testSuite.setData(dto.getData());
		testSuite.setCodeSuite(dto.getCodeSuite());

		// Atualizar TestPlan
		Optional<TestPlan> testPlanOpt = testPlanRepository.findById(dto.getTestPlanId());
		if (!testPlanOpt.isPresent()) {
			throw new RuntimeException("TestPlan não encontrado com ID: " + dto.getTestPlanId());
		}
		testSuite.setTestPlan(testPlanOpt.get());

		// Atualizar TestCases
		Set<TestCaseEntity> testCases = dto.getTestCaseId().stream()
				.map(testCaseId -> testCaseRepository.findById(testCaseId)
						.orElseThrow(() -> new RuntimeException("TestCase não encontrado com ID: " + testCaseId)))
				.collect(Collectors.toSet());
		testSuite.setCases(testCases);

		// Salvar mudanças
		testSuite = testSuiteRepository.save(testSuite);
		return new TestSuiteDTO(testSuite);
	}

	// Excluir um TestSuite
	public void delete(Long id) {
		if (!testSuiteRepository.existsById(id)) {
			throw new RuntimeException("TestSuite não encontrado com ID: " + id);
		}
		testSuiteRepository.deleteById(id);
	}

	public TestSuiteDTO clone(Long id) {
		// Buscar o TestSuite original
		TestSuite originalTestSuite = testSuiteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("TestSuite não encontrado com ID: " + id));

		// Criar um novo TestSuite
		TestSuite clonedTestSuite = new TestSuite();
		clonedTestSuite.setStatus(originalTestSuite.getStatus());
		clonedTestSuite.setData(originalTestSuite.getData());
		clonedTestSuite.setCodeSuite(originalTestSuite.getCodeSuite());
		clonedTestSuite.setCodeSuite(clonedTestSuite.getCodeSuite());
		// Buscar o TestPlan associado ao TestSuite original e associá-lo ao novo
		// TestSuite
		TestPlan originalTestPlan = originalTestSuite.getTestPlan();
		clonedTestSuite.setTestPlan(originalTestPlan);

		// Clonar os TestCases associados ao TestSuite original
		Set<TestCaseEntity> clonedTestCases = new HashSet<>();
		for (TestCaseEntity testCase : originalTestSuite.getCases()) {
			// Criar um novo TestCase
			TestCaseEntity clonedTestCase = new TestCaseEntity();
			clonedTestCase.setScenario(testCase.getScenario());
			clonedTestCase.setExpectedResult(testCase.getExpectedResult());
			clonedTestCase.setObtainedResult(testCase.getObtainedResult());
			clonedTestCase.setVideoEvidence(testCase.getVideoEvidence());
			clonedTestCase.setStatus(testCase.getStatus());
			clonedTestCase.setData(testCase.getData());

			// Adicionar o TestCase à coleção de TestCases clonados
			clonedTestCases.add(clonedTestCase);
		}

		// Salvar o novo TestSuite
		TestSuite finalClonedTestSuite = testSuiteRepository.save(clonedTestSuite);

		// Associar o TestSuite recém-criado aos TestCases clonados
		clonedTestCases.forEach(testCase -> testCase.setTestSuite(finalClonedTestSuite));

		// Salvar todos os TestCases clonados
		testCaseRepository.saveAll(clonedTestCases);

		// Retornar o DTO do TestSuite clonado
		return new TestSuiteDTO(finalClonedTestSuite);
	}
	

}
