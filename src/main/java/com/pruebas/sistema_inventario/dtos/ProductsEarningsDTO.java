package com.pruebas.sistema_inventario.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
@AllArgsConstructor
public class ProductsEarningsDTO {
	
	private ProductDTO product;
	private BigDecimal earnings;
	
}
