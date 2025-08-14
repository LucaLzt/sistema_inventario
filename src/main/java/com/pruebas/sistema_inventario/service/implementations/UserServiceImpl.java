package com.pruebas.sistema_inventario.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.dtos.UserDTO;
import com.pruebas.sistema_inventario.entities.Admin;
import com.pruebas.sistema_inventario.entities.Employee;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.entities.User;
import com.pruebas.sistema_inventario.exceptions.EmailException;
import com.pruebas.sistema_inventario.exceptions.EntityNotFoundException;
import com.pruebas.sistema_inventario.exceptions.PasswordException;
import com.pruebas.sistema_inventario.repository.UserRepository;
import com.pruebas.sistema_inventario.service.interfaces.UserService;
import com.pruebas.sistema_inventario.specifications.UserSpecification;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	
	public UserServiceImpl(UserRepository userRepository, 
			PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User principal = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
		if (principal instanceof Admin) {
			Admin admin = (Admin) principal;
			return admin;
		} else if (principal instanceof Employee) {
			return principal;
		} else {
			throw new UsernameNotFoundException("User type not recognized");
		}
	}
	
	@Override
	public void changePassword(Long id, PasswordChangeDTO passwordChange) {
		// Find User by ID
		User user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
		
		// Check if current email matches
		if (!user.getEmail().equals(passwordChange.getEmail())) {
			throw new EmailException("Email does not match the user's email");
		}
		// Check if current password matches
		if (!passwordEncoder.matches(passwordChange.getCurrentPassword(), user.getPassword())) {
			throw new PasswordException("Current password does not match");
		}
		// Check if new password is the same as current password
		if (passwordChange.getNewPassword().equals(passwordChange.getCurrentPassword())) {
			throw new PasswordException("New password cannot be the same as the current password");
		}
		// Check if new password matches confirmation
		if (!passwordChange.getNewPassword().equals(passwordChange.getConfirmNewPassword())) {
			throw new PasswordException("New password and confirmation do not match");
		}
		// Encode new password and set it
		user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
		userRepository.save(user);
	}

	@Override
	public List<UserDTO> findAllPendingRequests() {
		return userRepository.findAllByApproved(false)
				.stream()
				.map(object -> modelMapper.map(object, UserDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public void approveRequest(Long id) {
		// Find User by ID
		User user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
		
		// Set the user as approved
		user.setApproved(true);
		
		// Save the updated user
		userRepository.save(user);
	}

	@Override
	public void rejectRequest(Long id) {
		// Find User by ID
		User user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
		
		// Delete the user
		userRepository.delete(user);
	}
	
	@Override
	public Page<UserDTO> findByFilters(Boolean approved, Role role, String search, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "registeredAt"));
		var spec = UserSpecification.filter(approved, role, search);
		Page<User> users = userRepository.findAll(spec, pageable);
		
		// Polymorphism mapping
		return users.map(user -> {
			if (user instanceof Employee) {
				return modelMapper.map(user, EmployeeDTO.class);
			} else if (user instanceof Admin) {
				return modelMapper.map(user, AdminDTO.class);
			} else {
				return modelMapper.map(user, UserDTO.class);
			}
		});
	}
	
	@Override
	public boolean deleteById(Long id, boolean isActive) {
		if(isActive) {
			// If the user is active, return false
			return false;
		} else {
			// If the user is not active, delete by ID
			userRepository.deleteById(id);
			return true;
		}
	}

	@Override
	public UserDTO findById(Long id) {
		// Find User by ID
		User user = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
		
		// Polymorphism mapping
		if (user instanceof Employee) {
			return modelMapper.map(user, EmployeeDTO.class);
		} else if (user instanceof Admin) {
			return modelMapper.map(user, AdminDTO.class);
		} else {
			return modelMapper.map(user, UserDTO.class);
		}
	}

}
