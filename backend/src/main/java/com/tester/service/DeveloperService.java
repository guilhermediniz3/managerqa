package com.tester.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.DeveloperDTO;
import com.tester.entity.Developer;
import com.tester.entity.Technology;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.DeveloperRepository;
import com.tester.repository.TechnologyRepository;

import jakarta.transaction.Transactional;
@Service
public class DeveloperService {
	 @Autowired
	    private DeveloperRepository developerRepository;

	    @Autowired
	    private TechnologyRepository technologyRepository;

	    public List<DeveloperDTO> getAllDevelopers() {
	        return developerRepository.findAll().stream()
	                .map(developer -> new DeveloperDTO(developer)) // Usando lambda
	                .collect(Collectors.toList());
	    }

	    public DeveloperDTO getDeveloperById(Long id) {
	        Developer developer = developerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
	        return new DeveloperDTO(developer);
	    }

	    @Transactional
	    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
	        if (developerDTO.getTechnologyIds() == null || developerDTO.getTechnologyIds().isEmpty()) {
	            throw new IllegalArgumentException("Technology IDs cannot be null or empty");
	        }

	        Developer developer = new Developer();
	        developer.setName(developerDTO.getName());
	        developer.setActive(developerDTO.isActive());

	        developerDTO.getTechnologyIds().forEach(techId -> {
	            Technology technology = technologyRepository.findById(techId)
	                    .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + techId));
	            developer.addTechnology(technology);
	        });

	        Developer savedDeveloper = developerRepository.save(developer);
	        return new DeveloperDTO(savedDeveloper);
	    }

	    @Transactional
	    public DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO) {
	        if (developerDTO.getTechnologyIds() == null || developerDTO.getTechnologyIds().isEmpty()) {
	            throw new IllegalArgumentException("Tecnologia não pode ser vazia ou nula");
	        }

	        Developer existingDeveloper = developerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));

	        // Atualiza os dados do desenvolvedor
	        existingDeveloper.setName(developerDTO.getName());
	        existingDeveloper.setActive(developerDTO.isActive());

	        // Adiciona as novas tecnologias sem remover as já existentes
	        developerDTO.getTechnologyIds().forEach(techId -> {
	            // Verifica se a tecnologia já não está associada ao desenvolvedor
	            if (existingDeveloper.getTechnologies().stream().noneMatch(tech -> tech.getId().equals(techId))) {
	                Technology technology = technologyRepository.findById(techId)
	                        .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + techId));
	                existingDeveloper.addTechnology(technology); // Adiciona a nova tecnologia
	            }
	        });

	        // Salva as alterações no desenvolvedor
	        developerRepository.save(existingDeveloper);

	        return new DeveloperDTO(existingDeveloper);
	    }
	    @Transactional
	    public DeveloperDTO removeTechnologyFromDeveloper(Long developerId, Long technologyId) {
	        Developer existingDeveloper = developerRepository.findById(developerId)
	                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + developerId));

	        Technology technology = technologyRepository.findById(technologyId)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + technologyId));

	        existingDeveloper.removeTechnology(technology); // Remover tecnologia

	        developerRepository.save(existingDeveloper); // Salvar as alterações

	        return new DeveloperDTO(existingDeveloper);
	    }

	    @Transactional
	    public void deleteDeveloper(Long id) {
	        Developer developer = developerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
	        developerRepository.delete(developer);
	    }
}
