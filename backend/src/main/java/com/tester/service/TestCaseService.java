package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.TestCaseDTO;
import com.tester.entity.TestCaseEntity;
import com.tester.entity.TestSuite;
import com.tester.repository.TestCaseRepository;
import com.tester.repository.TestSuiteRepository;

@Service
public class TestCaseService {
	
	@Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    // Criar um novo TestCase
    public TestCaseDTO create(TestCaseDTO dto) {
        TestSuite testSuite = testSuiteRepository.findById(dto.getTestSuiteId())
                .orElseThrow(() -> new RuntimeException("TestSuite não encontrado com ID: " + dto.getTestSuiteId()));

        TestCaseEntity testCase = new TestCaseEntity();
        testCase.setCodeCase(dto.getCodeCase());
        testCase.setScenario(dto.getScenario());
        testCase.setExpectedResult(dto.getExpectedResult());
        testCase.setObtainedResult(dto.getObtainedResult());
        testCase.setVideoEvidence(dto.getVideoEvidence());
        testCase.setStatus(dto.getStatus());
        testCase.setData(dto.getData());
        testCase.setTestSuite(testSuite);

        testCase = testCaseRepository.save(testCase);
        return new TestCaseDTO(testCase);
    }

    // Buscar todos os TestCases
    public List<TestCaseDTO> findAll() {
        return testCaseRepository.findAll().stream()
                .map(TestCaseDTO::new)
                .collect(Collectors.toList());
    }

    // Buscar um TestCase por ID
    public TestCaseDTO findById(Long id) {
        TestCaseEntity testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestCase não encontrado com ID: " + id));
        return new TestCaseDTO(testCase);
    }

    // Atualizar um TestCase
    public TestCaseDTO update(Long id, TestCaseDTO dto) {
        TestCaseEntity testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestCase não encontrado com ID: " + id));

        TestSuite testSuite = testSuiteRepository.findById(dto.getTestSuiteId())
                .orElseThrow(() -> new RuntimeException("TestSuite não encontrado com ID: " + dto.getTestSuiteId()));
        
        testCase.setScenario(dto.getScenario());
        testCase.setExpectedResult(dto.getExpectedResult());
        testCase.setObtainedResult(dto.getObtainedResult());
        testCase.setVideoEvidence(dto.getVideoEvidence());
        testCase.setStatus(dto.getStatus());
        testCase.setData(dto.getData());
        testCase.setTestSuite(testSuite);

        testCase = testCaseRepository.save(testCase);
        return new TestCaseDTO(testCase);
    }

    // Excluir um TestCase
    public void delete(Long id) {
        if (!testCaseRepository.existsById(id)) {
            throw new RuntimeException("TestCase não encontrado com ID: " + id);
        }
        testCaseRepository.deleteById(id);
    }
    public List<TestCaseDTO> getTestCasesByTestPlanAndTestSuite(Long testPlanId, Long testSuiteId) {
        return testCaseRepository.findTestCasesByTestPlanAndTestSuite(testPlanId, testSuiteId);
    }

}
