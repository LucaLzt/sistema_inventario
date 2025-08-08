package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
	
	private String email;
	private String password;
	private String fullName;
	private Long mobile;
	
}
