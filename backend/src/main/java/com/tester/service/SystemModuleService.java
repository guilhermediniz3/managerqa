package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tester.dto.SystemModuleDTO;
import com.tester.entity.SystemModule;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.SystemModuleRepository;





@Service
public class SystemModuleService {
	
	@Autowired
   private SystemModuleRepository moduleRepository;
	@Transactional
	public SystemModuleDTO createModule(SystemModuleDTO moduleDTO) {
		SystemModule SystemModule = new SystemModule();
		SystemModule.setName(moduleDTO.getName());
		SystemModule.setActive(moduleDTO.isActive());
		SystemModule saveModule = moduleRepository.save(SystemModule);
		return new SystemModuleDTO(saveModule);
		
	
	}
		 @Transactional(readOnly=true)
	public List<SystemModuleDTO> getModuleAll(){
		
	return  moduleRepository.findAll().stream().map(module -> new SystemModuleDTO(module)).collect(Collectors.toList());
		
			
	}
		 @Transactional(readOnly=true)
	public SystemModuleDTO getModuleById(Long id) {
		
		SystemModule module = moduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado com o id "+ id));
		return new SystemModuleDTO(module);
		
	}
		 
	@Transactional
	public SystemModuleDTO updateModule(Long id, SystemModuleDTO moduleDTO) {
		
	  SystemModule module = moduleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Módulo não encontrado com o id "+ id));
	  
	  module.setName(moduleDTO.getName());
	  module.setActive(moduleDTO.isActive());
	  SystemModule saveModule = moduleRepository.save(module);
	  return new SystemModuleDTO(saveModule);
	  
	  	
		
	}
	
	@Transactional
	public void deleteModule(Long id) {
	    if (!moduleRepository.existsById(id)) {
	        throw new ResourceNotFoundException("Módulo não encontrado com o ID " + id);
	    }
	    moduleRepository.deleteById(id);
	}



}