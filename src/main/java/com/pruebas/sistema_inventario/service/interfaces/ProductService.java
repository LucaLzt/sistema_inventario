package com.pruebas.sistema_inventario.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.pruebas.sistema_inventario.dtos.ProductDTO;

public interface ProductService {
	
	ProductDTO save(ProductDTO productDto);
	
	ProductDTO findById(Long id);
	
	List<ProductDTO> findAll();
	
	ProductDTO update(Long id, ProductDTO productDto);
	
	void deleteById(Long id);

	Page<ProductDTO> findFiltered(String search, List<Long> categories, int page, int size);

	
}
