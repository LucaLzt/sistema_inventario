package com.pruebas.sistema_inventario.service.interfaces;

import java.util.List;

import com.pruebas.sistema_inventario.dtos.CategoryDTO;

public interface CategoryService {
	
	CategoryDTO save(CategoryDTO categoryDto);
	
	CategoryDTO findById(Long id);
	
	List<CategoryDTO> findAll();
	
	CategoryDTO update(Long id, CategoryDTO categoryDto);
	
	void deleteById(Long id);

}
