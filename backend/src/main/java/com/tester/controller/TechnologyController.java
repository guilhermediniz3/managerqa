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
import com.tester.dto.TechnologyDTO;
import com.tester.service.TechnologyService;

@RestController
@RequestMapping("/technologies")
public class TechnologyController {
	
	 @Autowired
	    private TechnologyService technologyService;


	    @GetMapping
	    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies() {
	        List<TechnologyDTO> technologies = technologyService.getAllTechnologies();
	        return ResponseEntity.ok(technologies);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<TechnologyDTO> getTechnologyById(@PathVariable Long id) {
	        TechnologyDTO technology = technologyService.getTechnologyById(id);
	        return ResponseEntity.ok(technology);
	    }

	
	    @PostMapping
	    public ResponseEntity<TechnologyDTO> createTechnology(@RequestBody TechnologyDTO technologyDTO) {
	        TechnologyDTO createdTechnology = technologyService.createTechnology(technologyDTO);
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdTechnology);
	    }


	    @PutMapping("/{id}")
	    public ResponseEntity<TechnologyDTO> updateTechnology(@PathVariable Long id, @RequestBody TechnologyDTO technologyDTO) {
	        TechnologyDTO updatedTechnology = technologyService.updateTechnology(id, technologyDTO);
	        return ResponseEntity.ok(updatedTechnology);
	    }

	  
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {
	        technologyService.deleteTechnology(id);
	        return ResponseEntity.noContent().build();
	    }
	    
	    @PatchMapping("/{id}")
	    public ResponseEntity<TechnologyDTO> patchDeveloper(@PathVariable Long id, @RequestBody TechnologyDTO technologyDTO) {
	    	TechnologyDTO updateTechnology = technologyService.patchTecnology(id, technologyDTO);
	        return ResponseEntity.ok(updateTechnology);
	    }

}
