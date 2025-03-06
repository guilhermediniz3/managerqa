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

import com.tester.dto.DeveloperDTO;
import com.tester.service.DeveloperService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
	


	    @Autowired
	    private DeveloperService developerService;

	    @GetMapping
	    public ResponseEntity<List<DeveloperDTO>> getAllDevelopers() {
	        List<DeveloperDTO> developers = developerService.getAllDevelopers();
	        return ResponseEntity.ok(developers);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<DeveloperDTO> getDeveloperById(@PathVariable Long id) {
	        DeveloperDTO developer = developerService.getDeveloperById(id);
	        return ResponseEntity.ok(developer);
	    }

	    @PostMapping
	    public ResponseEntity<DeveloperDTO> createDeveloper( @Valid @RequestBody DeveloperDTO developerDTO) {
	        DeveloperDTO createdDeveloper = developerService.createDeveloper(developerDTO);
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeveloper);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<DeveloperDTO> updateDeveloper( @PathVariable Long id, @Valid @RequestBody DeveloperDTO developerDTO) {
	        DeveloperDTO updatedDeveloper = developerService.updateDeveloper(id, developerDTO);
	        return ResponseEntity.ok(updatedDeveloper);
	    }
       // remove tecnolgia do dev
	    @DeleteMapping("/{developerId}/technology/{technologyId}")
	    public ResponseEntity<DeveloperDTO> removeTechnologyFromDeveloper(
	            @PathVariable Long developerId, @PathVariable Long technologyId) {
	        DeveloperDTO updatedDeveloper = developerService.removeTechnologyFromDeveloper(developerId, technologyId);
	        return ResponseEntity.ok(updatedDeveloper); // Retorna o desenvolvedor atualizado
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
	        developerService.deleteDeveloper(id);
	        return ResponseEntity.noContent().build();
	    }
	    
	    
	    
	    @PatchMapping("/{id}")
	    public ResponseEntity<DeveloperDTO> patchDeveloper(@PathVariable Long id, @RequestBody DeveloperDTO developerDTO) {
	        DeveloperDTO updatedDeveloper = developerService.patchDeveloper(id, developerDTO);
	        return ResponseEntity.ok(updatedDeveloper);
	    }
}
	