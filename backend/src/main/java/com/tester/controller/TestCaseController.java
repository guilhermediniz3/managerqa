package com.tester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tester.dto.TestCaseDTO;
import com.tester.service.TestCaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/testcases")
public class TestCaseController {
	
	
	  @Autowired
	    private TestCaseService testCaseService;

	    // Criar um novo TestCase
	    @PostMapping
	    public ResponseEntity<TestCaseDTO> create(@Valid @RequestBody TestCaseDTO dto) {
	        TestCaseDTO createdTestCase = testCaseService.create(dto);
	        return ResponseEntity.ok(createdTestCase);
	    }

	    // Buscar todos os TestCases
	    @GetMapping
	    public ResponseEntity<List<TestCaseDTO>> findAll() {
	        List<TestCaseDTO> testCases = testCaseService.findAll();
	        return ResponseEntity.ok(testCases);
	    }

	    // Buscar um TestCase por ID
	    @GetMapping("/{id}")
	    public ResponseEntity<TestCaseDTO> findById(@PathVariable Long id) {
	        TestCaseDTO testCase = testCaseService.findById(id);
	        return ResponseEntity.ok(testCase);
	    }

	    // Atualizar um TestCase
	    @PutMapping("/{id}")
	    public ResponseEntity<TestCaseDTO> update(@PathVariable Long id, @Valid @RequestBody TestCaseDTO dto) {
	        TestCaseDTO updatedTestCase = testCaseService.update(id, dto);
	        return ResponseEntity.ok(updatedTestCase);
	    }

	    // Excluir um TestCase
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        testCaseService.delete(id);
	        return ResponseEntity.noContent().build();
	    }

}
