package com.pruebas.sistema_inventario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/auth")
public class AuthController {
	
	private final AdminService adminService;
	private final EmployeeService employeeService;
	private final BranchService branchService;
	
	@GetMapping("/login")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "logout", required = false) String logout,
			Model model) {
		
		// Verify if there is an error or logout message
		if (error != null) {
			model.addAttribute("error", error);
		}
		if (logout != null) {
			model.addAttribute("logout", logout);
		}
		
		return "auth/login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("registerDto", new RegisterDTO());
        model.addAttribute("branches", branchService.findAll());
		return "auth/register";
	}
	
	@PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDto") RegisterDTO registerDto, 
    		BindingResult result, 
    		Model model) {
		
		// // Validate the user's data
		if (result.hasErrors()) {
			model.addAttribute("registerDto", registerDto);
			model.addAttribute("branches", branchService.findAll());
			return "auth/register";
		}
		
        if ("EMPLOYEE".equalsIgnoreCase(registerDto.getType())) {
        	employeeService.save(registerDto);
        } else if ("ADMIN".equalsIgnoreCase(registerDto.getType())) {
        	adminService.save(registerDto);
        } else {
            model.addAttribute("error", "Invalid user type");
            model.addAttribute("branches", branchService.findAll());
            return "auth/register";
        }
        return "redirect:/auth/login";
    }
	
	@GetMapping("/auth/forgot-password")
	public String showForgotPasswordForm() {
		
		// This method is used to display the forgot password form (Build)
		
		return "auth/forgot-password";
	}
	
}
