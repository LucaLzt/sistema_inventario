package com.pruebas.sistema_inventario.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.BranchDTO;
import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	private final BranchService branchService;
	
	@GetMapping("/profile")
	public String showProfile(Principal principal, Model model) {
		// Search for the employee through the principal's email and add it to the model
		EmployeeDTO employee = employeeService.findByEmail(principal.getName());
		model.addAttribute("employee", employee);
		
		// Search for the employee's branch and add them to the model
		if (employee.getBranchId() != null) {
			BranchDTO branch = branchService.findById(employee.getBranchId());
			model.addAttribute("branch", branch);
		}
		
		// Return the profile view
		return "employee/profile";
	}
	
}
