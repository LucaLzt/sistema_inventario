package com.pruebas.sistema_inventario.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pruebas.sistema_inventario.entities.TypeMovement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class InventoryMovementDTO {
	
	private Long id;
	private ProductDTO product;
	private TypeMovement typeMovement;
	private Integer amount;
	private BigDecimal priceUnit;
	private String motive;
	private Integer beforeStock;
	private Integer afterStock;
	@Builder.Default private String registeredUser = "System";
	private BranchDTO branch;
	private LocalDateTime movementDate;
	
}
