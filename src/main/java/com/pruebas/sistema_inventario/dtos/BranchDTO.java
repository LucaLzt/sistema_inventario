package com.pruebas.sistema_inventario.dtos;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {

	private Long id;
	
	@NotBlank(message = "Branch name is required.")
	@Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters.")
	private String name;
	
	@NotBlank(message = "Address is required.")
	@Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters.")
	private String address;
	
	@Builder.Default 
	private List<InventoryMovementDTO> inventoryMovements = new ArrayList<>();
	
}
