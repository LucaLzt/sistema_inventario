package com.pruebas.sistema_inventario.entities;

public enum TypeMovement {

	IN("Enter of merchandise"),
	OUT("Exit of merchandise"),
	ADJUSTMENT("Adjustment of stock");
	
	private final String description;
	
	TypeMovement(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
