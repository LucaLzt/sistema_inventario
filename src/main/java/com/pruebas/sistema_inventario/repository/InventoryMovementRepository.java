package com.pruebas.sistema_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.InventoryMovement;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long>,
	JpaSpecificationExecutor<InventoryMovement> {
}
