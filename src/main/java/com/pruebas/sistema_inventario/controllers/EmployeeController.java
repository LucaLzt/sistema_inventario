package com.pruebas.sistema_inventario.controllers;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.BranchDTO;
import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;
import com.pruebas.sistema_inventario.service.interfaces.UserService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/employees")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	private final BranchService branchService;
	private final UserService userService;
	
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
	
	@GetMapping("/profile/edit")
	public String showEditProfile(Principal principal, Model model) {
		// Search for the employee through the principal's email and add it to the model
		EmployeeDTO employee = employeeService.findByEmail(principal.getName());
		model.addAttribute("employee", employee);
		
		// Search for the employee's branch and add them to the model
		if (employee.getBranchId() != null) {
			BranchDTO branch = branchService.findById(employee.getBranchId());
			model.addAttribute("branch", branch);
		}
		
		// Return the edit profile view
		return "employee/edit-profile";
	}
	
	@PostMapping("/profile/edit")
	public String editProfile(Principal principal, 
			@Valid @ModelAttribute("employee") EmployeeDTO employee,
			BindingResult result,
			Model model) {
		// Search for the employee through the principal's email
		EmployeeDTO currentEmployee = employeeService.findByEmail(principal.getName());
		
		// Validate the employee's data
		if (result.hasErrors()) {
			// If there are validation errors, return to the edit profile view with errors
			employee.setRole(currentEmployee.getRole());
			employee.setRegisteredAt(currentEmployee.getRegisteredAt());
			employee.setUpdatedAt(currentEmployee.getUpdatedAt());
			employee.setBranchId(currentEmployee.getBranchId());
			if (employee.getBranchId() != null) {
				BranchDTO branch = branchService.findById(employee.getBranchId());
				model.addAttribute("branch", branch);
			}
			model.addAttribute("employee", employee);
			return "employee/edit-profile";
		}

		
		// Verify that the employee's email matches the principal's email otherwise update and logout
		if (!currentEmployee.getEmail().equals(employee.getEmail())) {
			employeeService.update(currentEmployee.getId(), employee);
			SecurityContextHolder.clearContext();
			return "redirect:/auth/login?emailChanged=ok";
		}
		
		// Update the employee's profile
		employeeService.update(employee.getId(), employee);
		
		// Redirect to the profile view after editing
		return "redirect:/employees/profile?updated=ok";
	}
	
	@GetMapping("/profile/change-password")
	public String showChangePassword(Principal principal, Model model) {
		// Search for the employee through the principal's email and add it to the model
		EmployeeDTO employee = employeeService.findByEmail(principal.getName());
		model.addAttribute("employee", employee);
		
		// Add an empty PasswordChangeDTO to the model
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		
		// Return the change password view
		return "employee/change-password";
	}
	
	@PostMapping("/profile/change-password")
	public String changePassword(Principal principal, 
			@Valid @ModelAttribute("passwordChange") PasswordChangeDTO passwordChange,
			BindingResult result,
			Model model) {
		// Validate the password change data
		if (result.hasErrors()) {
			// If there are validation errors, return to the change password view with errors
			model.addAttribute("passwordChange", passwordChange);
			EmployeeDTO employee = employeeService.findByEmail(principal.getName());
			model.addAttribute("employee", employee);
			return "employee/change-password";
		}
		
		// Search for the employee through the principal's email and change their password
		EmployeeDTO employee = employeeService.findByEmail(principal.getName());
		userService.changePassword(employee.getId(), passwordChange);
		
		// Redirect to the profile view after changing the password
		return "redirect:/employees/profile?passwordChanged=ok";
	}
	
}
