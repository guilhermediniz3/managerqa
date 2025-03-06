package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.DeveloperDTO;
import com.tester.dto.TechnologyDTO;
import com.tester.entity.Technology;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.TechnologyRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
@Service
public class TechnologyService {
	
	 @Autowired
	    private TechnologyRepository technologyRepository;

	    public List<TechnologyDTO> getAllTechnologies() {
	        return technologyRepository.findAll().stream()
	                .map(technology -> new TechnologyDTO(technology))  // Usando lambda
	                .collect(Collectors.toList());
	    }

	    public TechnologyDTO getTechnologyById(Long id) {
	        Technology technology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        return new TechnologyDTO(technology);  // Usando o construtor que recebe a entidade
	    }

	    public TechnologyDTO createTechnology(TechnologyDTO technologyDTO) {
	        Technology technology = new Technology();
	        technology.setName(technologyDTO.getName());
	        technology.setActive(technologyDTO.isActive());

	        Technology savedTechnology = technologyRepository.save(technology);
	        return new TechnologyDTO(savedTechnology);  // Usando o construtor que recebe a entidade
	    }
	    
		@Transactional
	    public TechnologyDTO updateTechnology(Long id, TechnologyDTO technologyDTO) {
	        Technology existingTechnology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        existingTechnology.setName(technologyDTO.getName());
	        existingTechnology.setActive(technologyDTO.isActive());

	        Technology updatedTechnology = technologyRepository.save(existingTechnology);
	        return new TechnologyDTO(updatedTechnology);  // Usando o construtor que recebe a entidade
	    }

	    public void deleteTechnology(Long id) {
	        Technology technology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        technologyRepository.delete(technology);
	    }
	    
	    @Transactional
	    // refatorar os outros patchs nesse padrao

	    public TechnologyDTO patchTecnology(Long id, TechnologyDTO technologyDTO) {
	        // Recupera a entidade existente do banco de dados
	        Technology existingTechnology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id));

	        // Atualiza apenas os campos presentes no DTO recebido
	        if (technologyDTO.getName() != null && !technologyDTO.getName().isEmpty()) {
	            existingTechnology.setName(technologyDTO.getName());
	        }
	        if (technologyDTO.isActive() != null) {
	            existingTechnology.setActive(technologyDTO.isActive());
	        }

	        // Salva a entidade atualizada no banco de dados
	        Technology updatedTechnology = technologyRepository.save(existingTechnology);

	        // Converte a entidade atualizada de volta para DTO e retorna
	        return new TechnologyDTO(updatedTechnology);
	    }
}
