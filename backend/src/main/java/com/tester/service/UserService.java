package com.tester.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.dto.UserDTO;
import com.tester.entity.User;
import com.tester.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserDTO createUser(UserDTO userDTO) {
		User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
		User savedUser = userRepository.save(user);
		return new UserDTO(savedUser.getName(), savedUser.getEmail(), null);
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(user -> new UserDTO(user.getName(), user.getEmail(), null))
				.collect(Collectors.toList());
	}

	public Optional<UserDTO> getUserById(Long id) {
		return userRepository.findById(id).map(user -> new UserDTO(user.getName(), user.getEmail(), null));
	}

	public Optional<UserDTO> getUserByEmail(String email) {

		return userRepository.findByEmail(email).map(user -> new UserDTO(user.getName(), user.getEmail(), null));
	}

	public UserDTO updateUser(Long id, UserDTO userDTO) {
		return userRepository.findById(id).map(user -> {
			user.setName(userDTO.getName());
			user.setEmail(userDTO.getEmail());
			user.setPassword(userDTO.getPassword());
			User updateUser = userRepository.save(user);
			return new UserDTO(updateUser.getName(), updateUser.getEmail(), null);
		}).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	}

	public void deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new RuntimeException("Usuário não encontrado");
		}
	}

}
