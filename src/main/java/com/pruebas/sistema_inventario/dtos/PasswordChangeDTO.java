package com.pruebas.sistema_inventario.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PasswordChangeDTO {
	
	@NotBlank(message = "Email is required.")
	@Email(message = "Email must be valid.", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	@Size(min = 10, max = 100, message = "Email must be between 10 and 100 characters.")
	private String email;
	
	@NotBlank(message = "Current password is required.")
	@Size(min = 8, max = 100, message = "Current password must be between 8 and 100 characters.")
	private String currentPassword;
	
	@NotBlank(message = "New password is required.")
	@Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters.")
	private String newPassword;
	
	@NotBlank(message = "Confirm new password is required.")
	@Size(min = 8, max = 100, message = "Confirm new password must be between 8 and 100 characters.")
	private String confirmNewPassword;
	
}
