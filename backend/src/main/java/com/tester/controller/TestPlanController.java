package com.tester.controller;

import java.awt.PageAttributes.MediaType;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.tester.dto.CreatedByDTO;
import com.tester.dto.LastCodeSuiteDTO;
import com.tester.dto.TestPlanDTO;
import com.tester.dto.TestPlanListagemDTO;
import com.tester.service.ExportSprintExcelService;
import com.tester.service.ExportSprintPDFService;
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
	public ResponseEntity<TestPlanDTO> updateTestPlan(@PathVariable Long id,
			@Valid @RequestBody TestPlanDTO testPlanDTO) {
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

	@GetMapping("/all")
	public ResponseEntity<Page<TestPlanListagemDTO>> getAllTestPlansDetails(@RequestParam(required = false) String name,
			@RequestParam(required = false) String observation, @RequestParam(required = false) String status,
			@RequestParam(required = false) String taskStatus, @RequestParam(required = false) String jira,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDataFim,
			@RequestParam(required = false) String matriz, @RequestParam(required = false) String userName,
			@RequestParam(required = false) String callNumber, @RequestParam(required = false) String developerName,
			@RequestParam(required = false) String systemModuleName,
			@RequestParam(required = false) String testerQAName,
			@PageableDefault(size = 50, page = 0) Pageable pageable) {

		Page<TestPlanListagemDTO> testPlans = testPlanService.findAllTestPlans(name, observation, status, taskStatus,
				jira, dataInicio, dataFim, deliveryDataInicio, deliveryDataFim, matriz, userName, callNumber,
				developerName, systemModuleName, testerQAName, pageable);

		return ResponseEntity.ok(testPlans);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<TestPlanDTO> patchTestPlan(@PathVariable Long id, @RequestBody TestPlanDTO testPlanDTO) {
		TestPlanDTO updatedTestPlan = testPlanService.patchTestPlan(id, testPlanDTO);
		return ResponseEntity.ok(updatedTestPlan);
	}

	@GetMapping("/{testPlanId}/last-code-suite")
	public ResponseEntity<LastCodeSuiteDTO> getLastCodeSuiteByTestPlanId(@PathVariable Long testPlanId) {
		LastCodeSuiteDTO lastCodeSuiteDTO = testPlanService.getLastCodeSuiteByTestPlanId(testPlanId);
		return ResponseEntity.ok(lastCodeSuiteDTO);
	}

	@GetMapping("/{testPlanId}/created-by")
	public CreatedByDTO getCreatedByByTestPlanId(@PathVariable Long testPlanId) {
		return testPlanService.getCreatedByByTestPlanId(testPlanId);
	}

	// endoint teste para deletar se nao funcionar

	@GetMapping("/search")
	public Page<TestPlanListagemDTO> searchTestPlans(@RequestParam(required = false) String searchValue,
			Pageable pageable) {
		return testPlanService.searchTestPlans(searchValue, pageable);
	}
	
	// endpoints dashboard -------------------------------------------------------------------------
	
	
	
	 // Endpoint para contar tarefas CONCLUÍDAS
    @GetMapping("/count-concluidas")
    public Long countConcluidas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {
        return testPlanService.getCountConcluidas(dataDe, dataAte);
    }

    // Endpoint para contar tarefas com RETORNO
    @GetMapping("/count-retorno")
    public Long countRetorno(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {
        return testPlanService.getCountRetorno(dataDe, dataAte);
    }

    // Endpoint para contar tarefas com IMPEDIMENTO
    @GetMapping("/count-impedimento")
    public Long countImpedimento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {
        return testPlanService.getCountImpedimento(dataDe, dataAte);
    }

    // Endpoint para contar tarefas EM PROGRESSO
    @GetMapping("/count-em-progresso")
    public Long countEmProgresso(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {
        return testPlanService.getCountEmProgresso(dataDe, dataAte);
    }

    // Endpoint para contar registros onde created = TRUE dentro de um intervalo de datas
    @GetMapping("/count-created-true")
    public Long countCreatedTrue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {
        return testPlanService.getCountCreatedTrue(dataDe, dataAte);
    }
    
   
}
