package com.pruebas.sistema_inventario.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.entities.Employee;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.repository.EmployeeRepository;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;

import lombok.Builder;

@Service @Builder
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	
	@Override
	public EmployeeDTO save(RegisterDTO registerDto) {
		// Create Employee Entity
		Employee employee = new Employee();
		employee.setEmail(registerDto.getEmail());
		employee.setFullName(registerDto.getFullName());
		employee.setMobile(registerDto.getMobile());
		employee.setRole(Role.EMPLOYEE);
		employee.setActive(false);
		employee.setApproved(false);
		
		// Encode Password
		employee.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		// Save Employee Entity
		employeeRepository.save(employee);
		
		// Convert to DTO and return
		EmployeeDTO employeeDto = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDto;
	}

	@Override
	public EmployeeDTO findById(Long id) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
		
		// Convert to EmployeeDTO and return
		EmployeeDTO employeeDto = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDto;
	}

	@Override
	public EmployeeDTO update(Long id, EmployeeDTO employeeDto) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
		
		// Update Employee fields
		employee.setFullName(employeeDto.getFullName());
		employee.setEmail(employeeDto.getEmail());
		employee.setMobile(employeeDto.getMobile());
		
		// Save updated Employee Entity
		Employee updatedEmployee = employeeRepository.save(employee);
		
		// Convert to EmployeeDTO and return
		EmployeeDTO updatedEmployeeDto = modelMapper.map(updatedEmployee, EmployeeDTO.class);
		return updatedEmployeeDto;
	}

	@Override
	public void deleteById(Long id) {
		// Check if Employee exists
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
		
		// Check if Employee is not active
		if (employee.isActive()) {
			throw new RuntimeException("Cannot delete active employee with id: " + id);
		}
		
		// Delete Employee by ID
		employeeRepository.delete(employee);
	}

	@Override
	public EmployeeDTO findByEmail(String email) {
		// Find Employee by email
		Employee employee = employeeRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
		
		// Convert to EmployeeDTO and return
		EmployeeDTO employeeDto = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDto;
	}

	@Override
	public void changePassword(Long id, PasswordChangeDTO passwordChange) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
		
		// Check if current email matches
		if (!employee.getEmail().equals(passwordChange.getEmail())) {
			throw new RuntimeException("Email does not match the employee's email");
		}
		
		// Check if current password matches
		if (!passwordEncoder.matches(passwordChange.getCurrentPassword(), employee.getPassword())) {
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
		employee.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
		employeeRepository.save(employee);
		
	}
	
}
