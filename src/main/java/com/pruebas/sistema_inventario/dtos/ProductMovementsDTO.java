package com.pruebas.sistema_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMovementsDTO {
	
	private ProductDTO product;
	private long totalMovements;
	
}
