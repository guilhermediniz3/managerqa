package com.tester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tester.dto.TestSuiteDTO;
import com.tester.service.TestSuiteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/test-suites")
public class TestSuiteController {
	
	
	 @Autowired
	    private TestSuiteService testSuiteService;

	    // Criar um novo TestSuite
	    @PostMapping
	    public ResponseEntity<TestSuiteDTO> create(@Valid @RequestBody TestSuiteDTO dto) {
	        TestSuiteDTO createdTestSuite = testSuiteService.create(dto);
	        return ResponseEntity.ok(createdTestSuite);
	    }

	    // Buscar todos os TestSuites
	    @GetMapping
	    public ResponseEntity<List<TestSuiteDTO>> findAll() {
	        List<TestSuiteDTO> testSuites = testSuiteService.findAll();
	        return ResponseEntity.ok(testSuites);
	    }

	    // Buscar um TestSuite por ID
	    @GetMapping("/{id}")
	    public ResponseEntity<TestSuiteDTO> findById(@PathVariable Long id) {
	        TestSuiteDTO testSuite = testSuiteService.findById(id);
	        return ResponseEntity.ok(testSuite);
	    }

	    // Atualizar um TestSuite
	    @PutMapping("/{id}")
	    public ResponseEntity<TestSuiteDTO> update(@PathVariable Long id, @Valid @RequestBody TestSuiteDTO dto) {
	        TestSuiteDTO updatedTestSuite = testSuiteService.update(id, dto);
	        return ResponseEntity.ok(updatedTestSuite);
	    }

	    // Excluir um TestSuite
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        testSuiteService.delete(id);
	        return ResponseEntity.noContent().build();
	    }

	    
	    @PostMapping("/clone/{id}")
	    public ResponseEntity<TestSuiteDTO> cloneTestSuite(@PathVariable Long id) {
	        try {
	            TestSuiteDTO clonedTestSuite = testSuiteService.clone(id); // Chamando o método de clonagem
	            return new ResponseEntity<>(clonedTestSuite, HttpStatus.CREATED); // Retorna o TestSuite clonado com status 201
	        } catch (RuntimeException e) {
	            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Caso não encontre o TestSuite
	        }
	    }
	    
	    @GetMapping("/testplan/{testPlanId}")
	    public ResponseEntity<List<TestSuiteDTO>> getTestSuitesByTestPlanId(@PathVariable Long testPlanId) {
	        List<TestSuiteDTO> testSuites = testSuiteService.getTestSuitesByTestPlanId(testPlanId);
	        return ResponseEntity.ok(testSuites);
	    }

	    

}
