package com.pruebas.sistema_inventario.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long id;
	
	// Unique code generated automatically
	private String code;
	
	@NotBlank(message = "Product name is required.")
	@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters.")
	private String name;
	
	@NotBlank(message = "Description is required.")
	@Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters.")
	private String description;
	
	@NotNull(message = "Price per unit is required.")
	@DecimalMin(value = "0.01", message = "Price per unit must be greater than 0.")
	private BigDecimal priceUnit;
	
	@NotNull(message = "Selling percentage is required.")
	@DecimalMin(value = "0.01", message = "Selling percentage must be greater than 0.")
	private BigDecimal sellingPercentage;
	
	@Builder.Default Integer stockActual = 0;
	
	@NotNull(message = "Stock minimum is required.")
	@DecimalMin(value = "0", message = "Stock minimum must be greater than or equal to 0.")
	private Integer stockMinimum;
	
	@Builder.Default private boolean active = true;
	
	@NotNull(message = "Category is required.")
	private CategoryDTO category;
	
}
