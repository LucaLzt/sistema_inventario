package com.pruebas.sistema_inventario.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.entities.Admin;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.repository.AdminRepository;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;

import lombok.Builder;

@Service @Builder
public class AdminServiceImpl implements AdminService {
	
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	
	@Override
	public AdminDTO save(RegisterDTO registerDto) {
		// Create Admin Entity
		Admin admin = new Admin();
		admin.setEmail(registerDto.getEmail());
		admin.setFullName(registerDto.getFullName());
		admin.setMobile(registerDto.getMobile());
		admin.setRole(Role.ADMIN);
		admin.setActive(false);
		admin.setApproved(false);
		
		// Encode Password
		admin.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		// Save Admin Entity
		adminRepository.save(admin);
		
		// Convert to DTO and return
		AdminDTO adminDto = modelMapper.map(admin, AdminDTO.class);
		return adminDto;
	}

	@Override
	public AdminDTO findById(Long id) {
		// Find Admin by ID
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
	
		// Convert to AdminDTO and return
		AdminDTO adminDto = modelMapper.map(admin, AdminDTO.class);
		return adminDto;
	}

	@Override
	public AdminDTO update(Long id, AdminDTO adminDto) {
		// Find Admin by ID
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
		
		// Update Admin fields
		admin.setEmail(adminDto.getEmail());
		admin.setFullName(adminDto.getFullName());
		admin.setMobile(adminDto.getMobile());
		
		// Save updated Admin entity
		Admin updatedAdmin = adminRepository.save(admin);
		
		// Convert to AdminDTO and return
		AdminDTO updatedAdminDto = modelMapper.map(updatedAdmin, AdminDTO.class);
		return updatedAdminDto;
	}

	@Override
	public void deleteById(Long id) {
		// Check if Admin exists
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
		
		// Check if Admin is not active
		if (admin.isActive()) {
			throw new RuntimeException("Cannot delete active admin with id: " + id);
		}
		
		// Delete Admin by ID
		adminRepository.deleteById(id);
	}

	@Override
	public AdminDTO findByEmail(String name) {
		// Find Admin by email
		Admin admin = adminRepository.findByEmail(name)
				.orElseThrow(() -> new RuntimeException("Admin not found with email: " + name));
		
		// Convert to AdminDTO and return
		AdminDTO adminDto = modelMapper.map(admin, AdminDTO.class);
		return adminDto;
	}
	
}
