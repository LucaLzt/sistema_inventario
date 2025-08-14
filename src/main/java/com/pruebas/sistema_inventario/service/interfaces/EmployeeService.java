package com.pruebas.sistema_inventario.service.interfaces;

import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;

public interface EmployeeService {
	
	EmployeeDTO save(RegisterDTO registerDto);
	EmployeeDTO findById(Long id);
	EmployeeDTO update(Long id, EmployeeDTO employeeDto);
	void deleteById(Long id);
	EmployeeDTO findByEmail(String email);
	void updateBranch(Long id, Long branchId);
	
}
