package com.pruebas.sistema_inventario.service.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;

public interface InventoryMovementService {
	
	InventoryMovementDTO save(InventoryMovementDTO movementDTO);
	
	InventoryMovementDTO findById(Long id);
	
	List<InventoryMovementDTO> findAll();
	
	InventoryMovementDTO update(Long id, InventoryMovementDTO movementDto);
	
	void deleteById(Long id);

	Page<InventoryMovementDTO> filtered(LocalDate dateFrom, LocalDate dateTo, 
			TypeMovement type, Long productId, int page, int size);

	List<InventoryMovementDTO> lastestMovements();

	List<InventoryMovementDTO> findByBranch(Long id);

	Page<InventoryMovementDTO> findByBranchWithFilters(Long branchId, LocalDate dateFrom, LocalDate dateTo,
			TypeMovement type, Long productId, int page, int size);
	
}
