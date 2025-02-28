package com.tester.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
		Developer developer = new Developer();
		developer.setName(developerDTO.getName());
		developer.setActive(developerDTO.isActive());

		if (developerDTO.getTechnologyIds() != null && !developerDTO.getTechnologyIds().isEmpty()) {
			Set<Technology> technologies = new HashSet<>(
					technologyRepository.findAllById(developerDTO.getTechnologyIds()));
			if (technologies.size() != developerDTO.getTechnologyIds().size()) {
				throw new ResourceNotFoundException("Uma ou mais tecnologias não foram encontradas");
			}
			developer.setTechnologies(technologies);
		}

		Developer savedDeveloper = developerRepository.save(developer);
		return new DeveloperDTO(savedDeveloper);
	}

	@Transactional
	public DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO) {
		Developer existingDeveloper = developerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));

		existingDeveloper.setName(developerDTO.getName());
		existingDeveloper.setActive(developerDTO.isActive());

		if (developerDTO.getTechnologyIds() != null) {
			Set<Technology> technologies = new HashSet<>(
					technologyRepository.findAllById(developerDTO.getTechnologyIds()));
			if (technologies.size() != developerDTO.getTechnologyIds().size()) {
				throw new ResourceNotFoundException("One or more technologies not found");
			}
			existingDeveloper.setTechnologies(technologies);
		}

		developerRepository.save(existingDeveloper);
		return new DeveloperDTO(existingDeveloper);
	}

	public DeveloperDTO getDeveloperById(Long id) {
		Developer developer = developerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
		return new DeveloperDTO(developer);
	}

	public List<DeveloperDTO> getAllDevelopers() {
		return developerRepository.findAll().stream().map(developer -> new DeveloperDTO(developer))
				.collect(Collectors.toList());
	}

	@Transactional
	public DeveloperDTO removeTechnologyFromDeveloper(Long developerId, Long technologyId) {
		Developer developer = developerRepository.findById(developerId)
				.orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + developerId));

		Technology technology = technologyRepository.findById(technologyId)
				.orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + technologyId));

		if (!developer.getTechnologies().contains(technology)) {
			throw new IllegalStateException("Developer does not have this technology assigned");
		}

		developer.getTechnologies().remove(technology);
		developerRepository.save(developer);

		return new DeveloperDTO(developer);
	}

	@Transactional
	public void deleteDeveloper(Long id) {
		Developer developer = developerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
		developerRepository.delete(developer);
	}
	
	@Transactional
	public DeveloperDTO patchDeveloper(Long id, DeveloperDTO developerDTO) {
		
		
	    // Cria um DTO temporário com apenas os campos que foram fornecidos
	    DeveloperDTO updatedDeveloperDTO = new DeveloperDTO();
	    updatedDeveloperDTO.setId(id); // Garante que o ID seja preservado

	    // Atualiza apenas os campos presentes no DeveloperDTO recebido
	    if (developerDTO.getName() != null) {
	        updatedDeveloperDTO.setName(developerDTO.getName());
	    }
	    if (developerDTO.isActive() != null) {
	        updatedDeveloperDTO.setActive(developerDTO.isActive());
	    }
	    if (developerDTO.getTechnologyIds() != null) {
	        updatedDeveloperDTO.setTechnologyIds(developerDTO.getTechnologyIds());
	    }

	    // Chama o método updateDeveloper para realizar a atualização
	    return updateDeveloper(id, updatedDeveloperDTO);
	}
}
