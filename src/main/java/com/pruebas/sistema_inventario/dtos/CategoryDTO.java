package com.pruebas.sistema_inventario.dtos;

import lombok.*;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoryDTO {
	
	private Long id;
	private String name;
	private String description;
	private boolean active;
	
}
