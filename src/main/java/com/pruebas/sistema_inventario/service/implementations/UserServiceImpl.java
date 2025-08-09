package com.pruebas.sistema_inventario.service.implementations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.entities.User;
import com.pruebas.sistema_inventario.repository.UserRepository;
import com.pruebas.sistema_inventario.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, 
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
		return user;
	}
	
	@Override
	public void changePassword(Long id, PasswordChangeDTO passwordChange) {
		// Find User by ID
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
		
		// Check if current email matches
		if (!user.getEmail().equals(passwordChange.getEmail())) {
			throw new RuntimeException("Email does not match the user's email");
		}
		
		// Check if current password matches
		if (!passwordEncoder.matches(passwordChange.getCurrentPassword(), user.getPassword())) {
			throw new RuntimeException("Old password does not match");
		}
		
		// Check if new password is the same as current password
		if (passwordChange.getNewPassword().equals(passwordChange.getCurrentPassword())) {
			throw new RuntimeException("New password cannot be the same as the current password");
		}
		
		// Check if new password matches confirmation
		if (!passwordChange.getNewPassword().equals(passwordChange.getConfirmNewPassword())) {
			throw new RuntimeException("New password and confirmation do not match");
		}
		
		// Encode new password and set it
		user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
		userRepository.save(user);
	}

}
