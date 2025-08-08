package com.pruebas.sistema_inventario.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
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
		employee.setEmail(employeeDto.getEmail());
		employee.setFullName(employeeDto.getFullName());
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
	
}
