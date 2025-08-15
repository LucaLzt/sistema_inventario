package com.pruebas.sistema_inventario.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pruebas.sistema_inventario.entities.TypeMovement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class InventoryMovementDTO {
	
	private Long id;
	
	@NotNull(message = "Product cannot be null.")
	private ProductDTO product;
	
	@NotNull(message = "Type of movement cannot be null.")
	private TypeMovement typeMovement;
	
	@NotNull(message = "Amount cannot be null.")
	@Min(value = 1, message = "Amount must be at least 1.")
	private Integer amount;
	
	private BigDecimal priceUnit;
	
	private BigDecimal sellingPrice;
	
	@NotNull(message = "Motive cannot be null.")
	@Size(min = 1, max = 100, message = "Motive must be between 1 and 100 characters.")
	private String motive;
	
	private Integer beforeStock;
	private Integer afterStock;
	
	@NotNull(message = "Registered user cannot be null.")
	@Builder.Default private String registeredUser = "System.";
	
	@NotNull(message = "Branch cannot be null.")
	private BranchDTO branch;
	
	// @NotNull(message = "Movement date cannot be null")
	private LocalDateTime movementDate;
	
}
