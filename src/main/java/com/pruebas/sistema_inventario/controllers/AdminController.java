package com.pruebas.sistema_inventario.controllers;

import java.security.Principal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;
import com.pruebas.sistema_inventario.service.interfaces.UserService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/admins")
public class AdminController {
	
	private final AdminService adminService;
	private final UserService userService;
	
	@GetMapping("/profile")
	public String getAdminProfile(Principal principal, Model model) {
		// Search for the admin through the principal's email and add it to the model
		AdminDTO admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admin", admin);
		
		// Return the profile view
		return "admin/profile";
	}
	
	@GetMapping("/profile/edit")
	public String showEditProfile(Principal principal, Model model) {
		// Search for the admin through the principal's email and add it to the model
		AdminDTO admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admin", admin);
		
		// Return the edit profile view
		return "admin/edit-profile";
	}
	
	@PostMapping("/profile/edit")
	public String editProfile(Principal principal, 
			@Valid @ModelAttribute("admin") AdminDTO admin,
			BindingResult result,
			Model model) {
		// Search for the admin through the principal's email and add it to the model
		AdminDTO currentAdmin = adminService.findByEmail(principal.getName());
		
		// Validate the admin's data
		if (result.hasErrors()) {
			// If there are validation errors, return to the edit profile view with errors
			admin.setRole(currentAdmin.getRole());
			admin.setRegisteredAt(currentAdmin.getRegisteredAt());
			admin.setUpdatedAt(currentAdmin.getUpdatedAt());
			admin.setSuperAdmin(currentAdmin.isSuperAdmin());
			model.addAttribute("admin", admin);
			return "admin/edit-profile";
		}
		
		// Verify that the admin's email is the same as the principal's email otherwise update and logout
		if (!admin.getEmail().equals(currentAdmin.getEmail())) {
			adminService.update(currentAdmin.getId(), admin);
			SecurityContextHolder.clearContext();
			return "redirect:/auth/login?emailChanged=ok";
		}
		
		// Update the admin's profile
		adminService.update(currentAdmin.getId(), admin);
		
		// Redirect to the profile view after editing
		return "redirect:/admins/profile?updated=ok";
	}
	
	@GetMapping("/profile/change-password")
	public String showChangePassword(Principal principal, Model model) {
		// Search for the admin through the principal's email and add it to the model
		AdminDTO admin = adminService.findByEmail(principal.getName());
		model.addAttribute("admin", admin);
		
		// Add an empty PasswordChangeDTO to the model
		model.addAttribute("passwordChange", new PasswordChangeDTO());
		
		// Return the change password view
		return "admin/change-password";
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
			AdminDTO admin = adminService.findByEmail(principal.getName());
			model.addAttribute("admin", admin);
			return "admin/change-password";
		}
		
		// Search for the admin through the principal's email and change their password
		AdminDTO admin = adminService.findByEmail(principal.getName());
		userService.changePassword(admin.getId(), passwordChange);
		
		// Redirect to the profile view after changing the password
		return "redirect:/admins/profile?passwordChanged=ok";
	}

}
