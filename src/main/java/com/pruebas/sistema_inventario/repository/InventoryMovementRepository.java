package com.pruebas.sistema_inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.InventoryMovement;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
	
	@Query("SELECT m FROM InventoryMovement m ORDER BY m.movementDate DESC")
	List<InventoryMovement> findAllOrderByMovementDateAsc();
	
}
