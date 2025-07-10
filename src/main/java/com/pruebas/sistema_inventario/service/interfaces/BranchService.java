package com.pruebas.sistema_inventario.service.interfaces;

import java.util.List;

import com.pruebas.sistema_inventario.dtos.BranchDTO;

public interface BranchService {
	
	BranchDTO save(BranchDTO branchDto);
	
	BranchDTO findById(Long id);
	
	List<BranchDTO> findAll();
	
	BranchDTO update(Long id, BranchDTO branchDto);
	
	void deleteById(Long id);

	List<BranchDTO> findByFilter(String filter);	
	
}
