package com.pruebas.sistema_inventario.dtos;

import java.math.BigDecimal;

import lombok.*;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long id;
	private String code;
	private String name;
	private String description;
	private BigDecimal priceUnit;
	private BigDecimal sellingPorcentage;
	@Builder.Default Integer stockActual = 0;
	private Integer stockMinimum;
	@Builder.Default private boolean active = true;
	private CategoryDTO category;
	
}
