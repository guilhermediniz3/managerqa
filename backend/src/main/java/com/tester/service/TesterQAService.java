package com.tester.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.TesterQADTO;
import com.tester.entity.TesterQA;
import com.tester.exception.ResourceNotFoundException;
import com.tester.repository.TesterQARepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TesterQAService {
	
	@Autowired
	private TesterQARepository testerQARepository;
	
	public TesterQADTO createTester (TesterQADTO testerQADTO) {
		
		TesterQA tester = new TesterQA();
	
		tester.setName(testerQADTO.getName());
		tester.setActive(testerQADTO.isActive());
		TesterQA savedTester = testerQARepository.save(tester);
		
		return new TesterQADTO(savedTester);
			
	}
	
	public List<TesterQADTO> getAllTesterQA(){
		
	   return testerQARepository.findAll().stream()
			   .map(TesterQA -> new TesterQADTO(TesterQA))
			   .collect(Collectors.toList());
		
		
	}
	
	public TesterQADTO getTesterQAById(Long id) {
		
	TesterQA testerQA =	testerQARepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tester not found id"+id));
		
		return new TesterQADTO(testerQA);
			
	}
	@Transactional
	public TesterQADTO updateTesterQA(Long id , TesterQADTO testerQADTO) {
		
		TesterQA testerQA =	testerQARepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tester not found id"+id));
	
		testerQA.setName(testerQADTO.getName());
		testerQA.setActive(testerQADTO.isActive());
		TesterQA updateTesterQA = testerQARepository.save(testerQA);
		
		return new TesterQADTO(updateTesterQA);
	
	}	
	
	public void  deleteTesterQA(Long id) {
		
		TesterQA testerQA = testerQARepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Tester not found id"+id));
		testerQARepository.delete(testerQA);
	
	}
	@Transactional
	
	public TesterQADTO patchTester(Long id, TesterQADTO testerDTO) {
	    return testerQARepository.findById(id)
	            .map(tester -> {
	                // Atualiza apenas os campos não nulos do testerDTO
	                if (testerDTO.getName() != null) {
	                    tester.setName(testerDTO.getName());
	                }
	                
	                if (testerDTO.isActive() != null) { // Verifica se o campo active foi enviado
	                    tester.setActive(testerDTO.isActive());
	                }

	                // Salva o objeto atualizado no repositório
	                TesterQA updatedTester = testerQARepository.save(tester);

	                // Converte o Tester atualizado para TesterQADTO
	                return new TesterQADTO(
	                        updatedTester.getId(),
	                        updatedTester.getName(),
	                        updatedTester.isActive()
	                );
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Tester não encontrado com o ID: " + id));
	}

	
}

