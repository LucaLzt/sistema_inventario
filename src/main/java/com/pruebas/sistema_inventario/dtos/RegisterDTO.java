package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterDTO {
	
	private String type; // "ADMIN" or "CLIENT"
	private String email;
	private String password;
	private String fullName;
	private Long mobile;
	private Long branchId; // Only for CLIENT type
	private boolean superAdmin; // Only for ADMIN type
	
}
