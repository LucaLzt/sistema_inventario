package com.pruebas.sistema_inventario.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.EmployeeDTO;
import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.dtos.UserDTO;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.service.interfaces.AdminService;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.EmployeeService;
import com.pruebas.sistema_inventario.service.interfaces.UserService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	private static final int PAGE_SIZE = 11; // Default page size for pagination
	private final EmployeeService employeeService;
	private final BranchService branchService;
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
	
	@GetMapping("/users")
	public String showUsers(
			@RequestParam(required = false) Role roleName,
			@RequestParam(required = false) String search, 
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		// Search for the users and add them to the model
		Page<UserDTO> users = userService.findByFilters(null, roleName, search, page, PAGE_SIZE);
		model.addAttribute("users", users);
		
		// Contruct a lists of PAGE_SIZE elements
		List<UserDTO> usersFixed = new ArrayList<>(users.getContent());
		while (usersFixed.size() < 11) {
		    usersFixed.add(null);
		}
		model.addAttribute("usersFixed", usersFixed);
		
		// Add the search term and pagination attributes to the model
		model.addAttribute("roleName", roleName);
		model.addAttribute("search", search);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		model.addAttribute("hasNext", users.hasNext());
		model.addAttribute("hasPrevious", users.hasPrevious());
		
		// Add the roles and branches to the model
		model.addAttribute("roles", Role.values());
		model.addAttribute("branches", branchService.findAll());
		
		// Add the principal
		model.addAttribute("principal", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		// Return the users view
		return "admin/users";
	}
	
	@PostMapping("/users/view/update-or-delete")
	public String updateOrDeleteUser(
	        @RequestParam Long id,
	        @RequestParam(required = false) String action,
	        @RequestParam(required = false) Boolean isActive,
	        @RequestParam(required = false) Long branchId,
	        @RequestParam(required = false, defaultValue = "false") Boolean superAdmin) {

	    if ("update".equals(action)) {
	        updateUser(id, branchId, superAdmin);
	        return "redirect:/admins/users?update=ok";
	    } else if ("delete".equals(action)) {
	        deleteUser(id, isActive);
	        return "redirect:/admins/users?delete=ok";
	    }
	    return "redirect:/admins/users?error=invalidAction";
	}
	
	private String updateUser(Long id, Long branchId, Boolean superAdmin) {
		// Find the user by ID
		UserDTO user = userService.findById(id);
		
		// Check if the user is an instance of AdminDTO or EmployeeDTO and update accordingly
		if (user instanceof AdminDTO) {
			adminService.updateSuperAdminStatus(id, superAdmin);
			return "redirect:/admins/users?update=ok";
		} else if (user instanceof EmployeeDTO) {
			employeeService.updateBranch(id, branchId);
			return "redirect:/admins/users?update=ok";
		}
		
		return "redirect:/admins/users?update=error";
	}
	
	private String deleteUser(Long id, boolean isActive) {
		if (userService.deleteById(id, isActive)) {
			// If the user was successfully deleted, return a success message
			return "redirect:/admins/users?deleted=ok";
		} else {
			// If the user was not deleted, return an error message
			return "redirect:/admins/users?deleted=error";
		}
	}
	
	@GetMapping("/requests")
	public String showRequests(
			@RequestParam(required = false) Role roleName,
			@RequestParam(required = false) String search, 
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		// Search for the users who have requested to be approved
		Page<UserDTO> requests = userService.findByFilters(false, roleName, search, page, PAGE_SIZE);
		model.addAttribute("requests", requests);
		
		// Contruct a lists of PAGE_SIZE elements
		List<UserDTO> requestsFixed = new ArrayList<>(requests.getContent());
		while (requestsFixed.size() < 11) {
		    requestsFixed.add(null);
		}
		model.addAttribute("requestsFixed", requestsFixed);
		
		// Add the search term and pagination attributes to the model
		model.addAttribute("roleName", roleName);
		model.addAttribute("search", search);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", requests.getTotalPages());
		model.addAttribute("hasNext", requests.hasNext());
		model.addAttribute("hasPrevious", requests.hasPrevious());
		
		// Add the roles to the model
		model.addAttribute("roles", Role.values());
		
		// Return the requests view
		return "admin/requests";
	}
	
	@PostMapping("/requests/approve/{id}")
	public String approveRequest(@PathVariable Long id) {
		// Approve the user request by their ID
		userService.approveRequest(id);
		
		// Redirect to the requests view after approving
		return "redirect:/admins/requests?approved=ok";
	}
	
	@PostMapping("/requests/reject/{id}")
	public String rejectRequest(@PathVariable Long id) {
		// Reject the user request by their ID
		userService.rejectRequest(id);
		
		// Redirect to the requests view after rejecting
		return "redirect:/admins/requests?rejected=ok";
	}

}
