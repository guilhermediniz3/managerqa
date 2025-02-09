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
                .map(developer -> new DeveloperDTO(
                        developer.getId(),
                        developer.getName(),
                        developer.isActive(),
                        developer.getTechnologies().stream()
                                .map(Technology::getId)
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    public DeveloperDTO getDeveloperById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
        return new DeveloperDTO(
                developer.getId(),
                developer.getName(),
                developer.isActive(),
                developer.getTechnologies().stream()
                        .map(Technology::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Transactional
    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        if (developerDTO.getTechnologyIds() == null || developerDTO.getTechnologyIds().isEmpty()) {
            throw new IllegalArgumentException("Technology IDs cannot be null or empty");
        }

        Developer developer = new Developer();
        developer.setName(developerDTO.getName());
        developer.setActive(developerDTO.isActive());

        for (Long techId : developerDTO.getTechnologyIds()) {
            Technology technology = technologyRepository.findById(techId)
                    .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + techId));
            developer.addTechnology(technology);
        }

        Developer savedDeveloper = developerRepository.save(developer);
        return new DeveloperDTO(
                savedDeveloper.getId(),
                savedDeveloper.getName(),
                savedDeveloper.isActive(),
                savedDeveloper.getTechnologies().stream()
                        .map(Technology::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Transactional
    public DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO) {
        if (developerDTO.getTechnologyIds() == null || developerDTO.getTechnologyIds().isEmpty()) {
            throw new IllegalArgumentException("Technology IDs cannot be null or empty");
        }

        Developer existingDeveloper = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
        existingDeveloper.setName(developerDTO.getName());
        existingDeveloper.setActive(developerDTO.isActive());

        // Limpa as tecnologias existentes
        new HashSet<>(existingDeveloper.getTechnologies()).forEach(tech -> existingDeveloper.removeTechnology(tech));

        // Adiciona as novas tecnologias
        for (Long techId : developerDTO.getTechnologyIds()) {
            Technology technology = technologyRepository.findById(techId)
                    .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + techId));
            existingDeveloper.addTechnology(technology);
        }

        Developer updatedDeveloper = developerRepository.save(existingDeveloper);
        return new DeveloperDTO(
                updatedDeveloper.getId(),
                updatedDeveloper.getName(),
                updatedDeveloper.isActive(),
                updatedDeveloper.getTechnologies().stream()
                        .map(Technology::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Transactional
    public void deleteDeveloper(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found with id " + id));
        developerRepository.delete(developer);
    }
}
