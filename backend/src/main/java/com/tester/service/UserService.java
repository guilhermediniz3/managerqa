package com.tester.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.UserDTO;
import com.tester.entity.User;
import com.tester.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;


	    // Criar um novo usuário
	    public UserDTO createUser(UserDTO userDTO) {
	        // Criando um novo usuário com o campo 'active'
	        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.isActive());
	        User savedUser = userRepository.save(user);
	        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPassword(), savedUser.isActive());
	    }

	    // Buscar todos os usuários
	    public List<UserDTO> getAllUsers() {
	        return userRepository.findAll().stream()
	                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), null, user.isActive()))
	                .collect(Collectors.toList());
	    }

	    // Buscar um usuário por ID
	    public Optional<UserDTO> getUserById(Long id) {
	        return userRepository.findById(id)
	                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), null, user.isActive()));
	    }

	    // Buscar um usuário por email
	    public Optional<UserDTO> getUserByEmail(String email) {
	        return userRepository.findByEmail(email)
	                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), null, user.isActive()));
	    }

	    // Atualizar um usuário
	    public UserDTO updateUser(Long id, UserDTO userDTO) {
	        return userRepository.findById(id)
	                .map(user -> {
	                    user.setName(userDTO.getName());
	                    user.setEmail(userDTO.getEmail());
	                    user.setPassword(userDTO.getPassword());
	                    user.setActive(userDTO.isActive());  
	                    User updatedUser = userRepository.save(user);
	                    return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getPassword(), updatedUser.isActive());
	                })
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	    }

	    // Excluir um usuário
	    public void deleteUser(Long id) {
	        if (userRepository.existsById(id)) {
	            userRepository.deleteById(id);
	        } else {
	            throw new RuntimeException("Usuário não encontrado");
	        }
	    }
		@Transactional
	    public UserDTO patchUser(Long id, UserDTO userDTO) {
	        return userRepository.findById(id)
	                .map(user -> {
	                    // Atualiza apenas os campos não nulos do userDTO
	                    if (userDTO.getName() != null) {
	                        user.setName(userDTO.getName());
	                    }
	                    if (userDTO.getEmail() != null) {
	                        user.setEmail(userDTO.getEmail());
	                    }
	                    if (userDTO.getPassword() != null) {
	                        user.setPassword(userDTO.getPassword());
	                    }
	                    if (userDTO.isActive() != null) { // Verifica se o campo active foi enviado
	                        user.setActive(userDTO.isActive());
	                    }

	                    User updatedUser = userRepository.save(user);
	                    // Converte o User atualizado para UserDTO usando o construtor
	                    return new UserDTO(
	                            updatedUser.getId(),
	                            updatedUser.getName(),
	                            updatedUser.getEmail(),
	                            updatedUser.getPassword(),
	                            updatedUser.isActive()
	                    );
	                })
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
	    }
		
		
		
		

	
}
