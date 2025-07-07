package com.pruebas.sistema_inventario.dtos;

import com.pruebas.sistema_inventario.entities.Product;


public class ProductMovementsFlatDTO {
	
	private Product product;
	private long totalMovements;

	public ProductMovementsFlatDTO(Product product, long totalMovements) {
		this.product = product;
		this.totalMovements = totalMovements;
	}

	public Product getProduct() {
		return product;
	}

	public long getTotalMovements() {
		return totalMovements;
	}
}
