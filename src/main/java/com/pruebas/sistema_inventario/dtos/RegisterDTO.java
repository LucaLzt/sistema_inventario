package com.pruebas.sistema_inventario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterDTO {
	
	@NotBlank(message = "Type is required")
	private String type; // "ADMIN" or "CLIENT"
	
	@NotBlank(message = "Email is required.")
	@Email(message = "Email must be valid.", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	@Size(min = 10, max = 50, message = "Email must be between 10 and 50 characters.")
	private String email;
	
	@NotBlank(message = "Password is required.")
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
	private String password;
	
	@NotBlank(message = "Full name is required.")
	@Size(min = 5, max = 50, message = "Full name must be less than 50 characters.")
	private String fullName;
	
	@NotNull(message = "Mobile number is required.")
	@Min(value = 1000000000L, message = "Mobile number must be at least 10 digits.")
	@Max(value = 9999999999L, message = "Mobile number must be at most 10 digits.")
	private Long mobile;
	
	private Long branchId; // Only for CLIENT type
	
}
