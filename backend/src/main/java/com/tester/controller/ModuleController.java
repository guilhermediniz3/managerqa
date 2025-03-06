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

import com.tester.dto.SystemModuleDTO;
import com.tester.service.SystemModuleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/modules")
public class ModuleController {

	@Autowired
	private SystemModuleService moduleService;
	@GetMapping
	public ResponseEntity<List<SystemModuleDTO>> getAllTesterQA() {

		List<SystemModuleDTO> modules = moduleService.getModuleAll();

		return ResponseEntity.ok(modules);
	
	}

	@GetMapping("/{id}")
	public ResponseEntity<SystemModuleDTO> getModuleById(@PathVariable Long id) {
		SystemModuleDTO module = moduleService.getModuleById(id);
		return ResponseEntity.ok(module);

	}
	
    @PostMapping 
    public ResponseEntity<SystemModuleDTO> createModule(@Valid @RequestBody SystemModuleDTO moduleDTO){
    	
    	SystemModuleDTO module = moduleService.createModule(moduleDTO);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(module);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<SystemModuleDTO> updateModule(@PathVariable Long id,@Valid @RequestBody SystemModuleDTO moduleDTO) {
    	SystemModuleDTO updatedTester = moduleService.updateModule(id, moduleDTO);
        return ResponseEntity.ok(updatedTester);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
    	moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<SystemModuleDTO> patchModule(@PathVariable Long id, @RequestBody SystemModuleDTO moduleDTO) {
        try {
            // Chama o serviço para atualizar parcialmente o módulo
            SystemModuleDTO updatedModule = moduleService.patchModule(id, moduleDTO);
            return ResponseEntity.ok(updatedModule); // Retorna o módulo atualizado
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o módulo não for encontrado
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // Retorna 500 em caso de erro interno
        }
    }
    

}
