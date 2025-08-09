package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PasswordChangeDTO {
	
	private String email;
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
	
}
