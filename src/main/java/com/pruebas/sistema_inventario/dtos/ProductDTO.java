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
	private Integer stockActual;
	private Integer stockMinimum;
	private boolean active;
	private CategoryDTO category;
	
}
