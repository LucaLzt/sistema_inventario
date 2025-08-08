package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AdminDTO extends UserDTO {
	
	private boolean superAdmin;
	
}
