package com.pruebas.sistema_inventario.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.entities.Admin;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.exceptions.ActiveUserException;
import com.pruebas.sistema_inventario.exceptions.EmailException;
import com.pruebas.sistema_inventario.exceptions.EntityNotFoundException;
import com.pruebas.sistema_inventario.repository.AdminRepository;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;

import lombok.Builder;

@Service @Builder
public class AdminServiceImpl implements AdminService {
	
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailServiceImpl emailService;
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
		Admin savedAdmin = adminRepository.save(admin);
		
		// Send registration email
		try {
			emailService.sendRegistrationEmail(savedAdmin.getEmail(), savedAdmin.getFullName());
		} catch (Exception e) {
			throw new RuntimeException("Error sending registration email", e);
		}
		
		// Convert to DTO and return
		AdminDTO adminDto = modelMapper.map(savedAdmin, AdminDTO.class);
		return adminDto;
	}

	@Override
	public AdminDTO findById(Long id) {
		// Find Admin by ID
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
	
		// Convert to AdminDTO and return
		AdminDTO adminDto = modelMapper.map(admin, AdminDTO.class);
		return adminDto;
	}

	@Override
	public AdminDTO update(Long id, AdminDTO adminDto) {
		// Find Admin by ID
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
		
		// Check if email is being changed
		boolean flag = false;
		String auxEmail = "";
		String auxFullName = "";
		if(!admin.getEmail().equals(adminDto.getEmail())) {
			flag = true;
			auxEmail = admin.getEmail();
			auxFullName = admin.getFullName();
		}
		
		// Update Admin fields
		admin.setEmail(adminDto.getEmail());
		admin.setFullName(adminDto.getFullName());
		admin.setMobile(adminDto.getMobile());
		
		// Save updated Admin entity
		Admin updatedAdmin = adminRepository.save(admin);
		
		// If email was changed, send notification
		if(flag) {
			try {
				emailService.sendEmailChangeEmail(auxEmail, auxFullName, auxEmail, updatedAdmin.getEmail());
				emailService.sendEmailChangeEmail(updatedAdmin.getEmail(), updatedAdmin.getFullName(), auxEmail, updatedAdmin.getEmail());
			} catch (Exception e) {
				throw new EmailException("Error sending email change notification.");
			}
		}
		
		// Convert to AdminDTO and return
		AdminDTO updatedAdminDto = modelMapper.map(updatedAdmin, AdminDTO.class);
		return updatedAdminDto;
	}

	@Override
	public void deleteById(Long id) {
		// Check if Admin exists
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
		
		// Check if Admin is not active
		if (admin.isActive()) {
			throw new ActiveUserException("Cannot delete active admin with id: " + id);
		}
		
		// Delete Admin by ID
		adminRepository.deleteById(id);
	}

	@Override
	public AdminDTO findByEmail(String name) {
		// Find Admin by email
		Admin admin = adminRepository.findByEmail(name)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found with name: " + name));
		
		// Convert to AdminDTO and return
		AdminDTO adminDto = modelMapper.map(admin, AdminDTO.class);
		return adminDto;
	}

	@Override
	public void updateSuperAdminStatus(Long id, Boolean superAdmin) {
		// Find Admin by ID
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
		
		// Save the super admin status in an auxiliary
		boolean auxSuperAdmin = admin.isSuperAdmin();
		
		// Update superAdmin status
		admin.setSuperAdmin(superAdmin);
		
		// If superAdmin status was changed, send notification
		if (auxSuperAdmin != superAdmin) {
			try {
				emailService.sendPrivilegedUpdatedEmail(admin.getEmail(), admin.getFullName(), 
						auxSuperAdmin == true ? "Super Admin" : "Regular Admin", 
						superAdmin == true ? "Super Admin" : "Regular Admin");
			} catch (Exception e) {
				throw new EmailException("Error sending super admin status notification.");
			}
		}
		
		// Save updated Admin entity
		adminRepository.save(admin);
	}
	
}
