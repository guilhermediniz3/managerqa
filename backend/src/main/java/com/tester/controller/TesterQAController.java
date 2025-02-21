package com.tester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tester.dto.TesterQADTO;
import com.tester.service.TesterQAService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/testers")
public class TesterQAController {

	@Autowired
	private TesterQAService testerQAService;

	@GetMapping
	public ResponseEntity<List<TesterQADTO>> getAllTesterQA() {

		List<TesterQADTO> testers = testerQAService.getAllTesterQA();

		return ResponseEntity.ok(testers);

	}

	@GetMapping("/{id}")
	public ResponseEntity<TesterQADTO> getTesterQAById(@PathVariable Long id) {
		TesterQADTO tester = testerQAService.getTesterQAById(id);
		return ResponseEntity.ok(tester);

	}
	
    @PostMapping 
    public ResponseEntity<TesterQADTO> createTesterQA(@Valid @RequestBody TesterQADTO testerQADTO){
    	
    	TesterQADTO tester = testerQAService.createTester(testerQADTO);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(tester);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<TesterQADTO> updateTester(@PathVariable Long id,@Valid @RequestBody TesterQADTO testerQADTO) {
        TesterQADTO updatedTester = testerQAService.updateTesterQA(id, testerQADTO);
        return ResponseEntity.ok(updatedTester);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTesterQA(@PathVariable Long id) {
        testerQAService.deleteTesterQA(id);
        return ResponseEntity.noContent().build();
    }
    
    
    @PatchMapping("/{id}")
    public ResponseEntity<TesterQADTO> patchTester(@PathVariable Long id, @RequestBody TesterQADTO testerDTO) {
        try {
          
            TesterQADTO updatedTester = testerQAService.patchTester(id, testerDTO);
            return ResponseEntity.ok(updatedTester); 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); 
        }
    }

}
