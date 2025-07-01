package com.pruebas.sistema_inventario.service.interfaces;

import java.util.List;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;

public interface InventoryMovementService {
	
	InventoryMovementDTO save(InventoryMovementDTO movementDTO);
	
	InventoryMovementDTO findById(Long id);
	
	List<InventoryMovementDTO> findAll();
	
	InventoryMovementDTO update(Long id, InventoryMovementDTO movementDto);
	
	void deleteById(Long id);
	
}
