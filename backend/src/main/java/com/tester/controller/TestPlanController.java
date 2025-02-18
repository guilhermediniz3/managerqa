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

import com.tester.dto.TestPlanDTO;
import com.tester.service.TestPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/testplans")
public class TestPlanController {

	@Autowired
	private TestPlanService testPlanService;

	@PostMapping
	public ResponseEntity<TestPlanDTO> createTestPlan(@Valid @RequestBody TestPlanDTO testPlanDTO) {
		TestPlanDTO createdTestPlan = testPlanService.createTestPlan(testPlanDTO);
		return ResponseEntity.ok(createdTestPlan);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TestPlanDTO> updateTestPlan(@PathVariable Long id, @Valid  @RequestBody TestPlanDTO testPlanDTO) {
		TestPlanDTO updatedTestPlan = testPlanService.updateTestPlan(id, testPlanDTO);
		return ResponseEntity.ok(updatedTestPlan);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTestPlan(@PathVariable Long id) {
		testPlanService.deleteTestPlan(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<TestPlanDTO>> getAllTestPlans() {
		List<TestPlanDTO> testPlans = testPlanService.getAllTestPlans();
		return ResponseEntity.ok(testPlans);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TestPlanDTO> getTestPlanById(@PathVariable Long id) {
		TestPlanDTO testPlan = testPlanService.getTestPlanById(id);
		return ResponseEntity.ok(testPlan);
	}

}
