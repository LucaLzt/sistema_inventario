package com.pruebas.sistema_inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.InventoryMovement;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long>,
	JpaSpecificationExecutor<InventoryMovement> {
	
	List<InventoryMovement> findTop5ByOrderByMovementDateDesc();
	
}
