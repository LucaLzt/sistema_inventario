package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EmployeeDTO extends UserDTO {
	
	private Long branchId;
	
}
