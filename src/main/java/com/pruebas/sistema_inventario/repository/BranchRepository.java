package com.pruebas.sistema_inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
	
	List<Branch> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String filter, String filter2);
	
}
