package com.pruebas.sistema_inventario.service.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.entities.Branch;
import com.pruebas.sistema_inventario.entities.Employee;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.exceptions.ActiveUserException;
import com.pruebas.sistema_inventario.exceptions.EmailException;
import com.pruebas.sistema_inventario.exceptions.EntityNotFoundException;
import com.pruebas.sistema_inventario.repository.BranchRepository;
import com.pruebas.sistema_inventario.repository.EmployeeRepository;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;

import lombok.Builder;

@Service @Builder
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	private final BranchRepository branchRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailServiceImpl emailService;
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
		Employee savedEmployee = employeeRepository.save(employee);
		
		// Send registration email
		try {
			emailService.sendRegistrationEmail(savedEmployee.getEmail(), savedEmployee.getFullName());
		} catch (Exception e) {
			throw new RuntimeException("Error sending registration email", e);
		}
		
		// Convert to DTO and return
		EmployeeDTO employeeDto = modelMapper.map(savedEmployee, EmployeeDTO.class);
		return employeeDto;
	}

	@Override
	public EmployeeDTO findById(Long id) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
		
		// Convert to EmployeeDTO and return
		EmployeeDTO employeeDto = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDto;
	}

	@Override
	public EmployeeDTO update(Long id, EmployeeDTO employeeDto) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
		
		// Check if email is being changed
		boolean flag = false;
		String auxEmail = "";
		String auxFullName = "";
		if(!employee.getEmail().equals(employeeDto.getEmail())) {
			flag = true;
			auxEmail = employee.getEmail();
			auxFullName = employee.getFullName();
		}
		
		// Update Employee fields
		employee.setFullName(employeeDto.getFullName());
		employee.setEmail(employeeDto.getEmail());
		employee.setMobile(employeeDto.getMobile());
		
		// Save updated Employee Entity
		Employee updatedEmployee = employeeRepository.save(employee);
		
		// If email was changed, send notification
		if(flag) {
			try {
				emailService.sendEmailChangeEmail(auxEmail, auxFullName, auxEmail, updatedEmployee.getEmail());
				emailService.sendEmailChangeEmail(updatedEmployee.getEmail(), updatedEmployee.getFullName(), auxEmail, updatedEmployee.getEmail());
			} catch (Exception e) {
				throw new EmailException("Error sending email change notification.");
			}
		}
		
		// Convert to EmployeeDTO and return
		EmployeeDTO updatedEmployeeDto = modelMapper.map(updatedEmployee, EmployeeDTO.class);
		return updatedEmployeeDto;
	}

	@Override
	public void deleteById(Long id) {
		// Check if Employee exists
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
		
		// Check if Employee is not active
		if (employee.isActive()) {
			throw new ActiveUserException("Cannot delete active employee with id: " + id);
		}
		
		// Delete Employee by ID
		employeeRepository.delete(employee);
	}

	@Override
	public EmployeeDTO findByEmail(String email) {
		// Find Employee by email
		Employee employee = employeeRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
		
		// Convert to EmployeeDTO and return
		EmployeeDTO employeeDto = modelMapper.map(employee, EmployeeDTO.class);
		return employeeDto;
	}
	
	@Override
	public void updateBranch(Long id, Long branchId) {
		// Find Employee by ID
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
		
		// Save the branch in an auxiliary
		String auxBranch = employee.getBranch() != null ? (employee.getBranch().getName() + " " + employee.getBranch().getAddress()) : "None";
		
		// Find Branch by ID
		Branch branch = branchRepository.findById(branchId)
				.orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
		
		// Update Employee's branch ID
		employee.setBranch(branch);
		
		// Save the updated Employee
		employeeRepository.save(employee);
		
		// Send notification email about workplace change
		try {
			emailService.sendWorkplaceChangedEmail(employee.getEmail(), employee.getFullName(), 
					auxBranch, 
					(branch.getName() + " " + branch.getAddress()));
		} catch (Exception e) {
			throw new EmailException("Error sending workplace change notification.");
		}
	};
	
}
