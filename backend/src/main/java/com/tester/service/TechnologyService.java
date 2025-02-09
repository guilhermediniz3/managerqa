package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.TechnologyDTO;
import com.tester.entity.Technology;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.TechnologyRepository;
@Service
public class TechnologyService {
	
	 @Autowired
	    private TechnologyRepository technologyRepository;

	    public List<TechnologyDTO> getAllTechnologies() {
	        return technologyRepository.findAll().stream()
	                .map(technology -> new TechnologyDTO(
	                        technology.getId(),
	                        technology.getName(),
	                        technology.isActive()
	                ))
	                .collect(Collectors.toList());
	    }

	    public TechnologyDTO getTechnologyById(Long id) {
	        Technology technology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        return new TechnologyDTO(
	                technology.getId(),
	                technology.getName(),
	                technology.isActive()
	        );
	    }

	    public TechnologyDTO createTechnology(TechnologyDTO technologyDTO) {
	        Technology technology = new Technology();
	        technology.setName(technologyDTO.getName());
	        technology.setActive(technologyDTO.isActive());

	        Technology savedTechnology = technologyRepository.save(technology);
	        return new TechnologyDTO(
	                savedTechnology.getId(),
	                savedTechnology.getName(),
	                savedTechnology.isActive()
	        );
	    }

	    public TechnologyDTO updateTechnology(Long id, TechnologyDTO technologyDTO) {
	        Technology existingTechnology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        existingTechnology.setName(technologyDTO.getName());
	        existingTechnology.setActive(technologyDTO.isActive());

	        Technology updatedTechnology = technologyRepository.save(existingTechnology);
	        return new TechnologyDTO(
	                updatedTechnology.getId(),
	                updatedTechnology.getName(),
	                updatedTechnology.isActive()
	        );
	    }

	    public void deleteTechnology(Long id) {
	        Technology technology = technologyRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id " + id));
	        technologyRepository.delete(technology);
	    }

}
