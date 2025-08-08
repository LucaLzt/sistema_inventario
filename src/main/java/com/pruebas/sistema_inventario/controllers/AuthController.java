package com.pruebas.sistema_inventario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.RegisterDTO;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/auth")
public class AuthController {
	
	private final AdminService adminService;
	private final EmployeeService employeeService;
	private final BranchService branchService;
	
	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("registerDto", new RegisterDTO());
        model.addAttribute("branches", branchService.findAll());
		return "auth/register";
	}
	
	@PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDto") RegisterDTO dto, Model model) {
        if ("EMPLOYEE".equalsIgnoreCase(dto.getType())) {
        	employeeService.save(dto);
        } else if ("ADMIN".equalsIgnoreCase(dto.getType())) {
        	adminService.save(dto);
        } else {
            model.addAttribute("error", "Invalid user type");
            model.addAttribute("branches", branchService.findAll());
            return "register";
        }
        return "redirect:/login";
    }
	
}
