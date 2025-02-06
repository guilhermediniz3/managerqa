package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.DeveloperDTO;
import com.tester.entity.Developer;
import com.tester.entity.Technology;
import com.tester.repository.DeveloperRepository;
import com.tester.repository.TechnologyRepository;

@Service
public class DeveloperService {

	@Autowired
	private DeveloperRepository developerRepository;
	 @Autowired
	    private TechnologyRepository technologyRepository;

	public List<DeveloperDTO> getAllDevelopers() {
		return developerRepository.findAll().stream()
				.map(developer -> new DeveloperDTO(developer))
				.collect(Collectors.toList());

	}
	
	public DeveloperDTO getDeveloperById(Long id) {
		Developer developer =  developerRepository.findById(id) 
				                                  .orElseThrow(() -> new RuntimeException("Desenvolvedor não encontrado"));
			return new DeveloperDTO(developer);                          
				                    
 	}
	
	public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
		
		Developer developer = new Developer(developerDTO.getName(),developerDTO.isActive());
		developerRepository.save(developer);
		return new DeveloperDTO(developer);
	}
	
	public DeveloperDTO updateDeveloper(Long id, DeveloperDTO developerDTO) {
		Developer developer = developerRepository.findById(id)
		                                         .orElseThrow(() -> new RuntimeException("Desenvolvedor não encontrado"));
		
		          developer.setName(developerDTO.getName());    
		          developer.setActive(developerDTO.isActive());
		          developerRepository.save(developer);
		          return new DeveloperDTO(developer);
	}
	
	public void deletaeDeveloper(Long id) {
		developerRepository.deleteById(id);
	}
	
	public void addTechnologyToDeveloper(Long developerId, Long technologyId) {
		
		Developer developer = developerRepository.findById(developerId)
	     		                                 .orElseThrow(()-> new RuntimeException("Desenvolvedor não encontrado"));
	 		Technology tecnology = technologyRepository.findById(technologyId)
                                                 .orElseThrow(()-> new RuntimeException("Desenvolvedor não encontrado"));
		developer.getTechnologies().add(tecnology);
		developerRepository.save(developer);
	}
	
	 // Lógica de remover tecnologia de um developer
    public void removeTechnologyFromDeveloper(Long developerId, Long technologyId) {
        Developer developer = developerRepository.findById(developerId)
                                                  .orElseThrow(() -> new RuntimeException("Developer not found"));
        Technology technology = technologyRepository.findById(technologyId)
                                                   .orElseThrow(() -> new RuntimeException("Technology not found"));

        developer.getTechnologies().remove(technology);
        developerRepository.save(developer);
    }

}
