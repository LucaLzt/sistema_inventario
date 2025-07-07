package com.pruebas.sistema_inventario.dtos;

import java.util.List;

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
	private String name;
	private String address;
	private List<InventoryMovementDTO> inventoryMovements;
	
}
