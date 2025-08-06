package com.pruebas.sistema_inventario.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoryDTO {
	
	private Long id;
	
	@NotBlank(message = "Name cannot be blank.")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
	private String name;
	
	@NotBlank(message = "Description cannot be blank.")
	@Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters.")
	private String description;
	
}
