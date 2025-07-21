package com.pruebas.sistema_inventario.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.dtos.ProductMovementsDTO;

public interface ProductService {
	
	ProductDTO save(ProductDTO productDto);
	
	ProductDTO findById(Long id);
	
	List<ProductDTO> findAll();
	
	ProductDTO update(Long id, ProductDTO productDto);
	
	void deleteById(Long id);

	Page<ProductDTO> findFiltered(String search, List<Long> categories, int page, int size);
	
	long countAll();
	
	long countStockAvailable();

	long countLowStock();
	
	long countOutOfStock();
	
	List<ProductDTO> findByLowStock();

	List<ProductMovementsDTO> popularProducts();

	Page<ProductDTO> findByBranchWithFilters(Long id, String filter, List<Long> categories, int page, int size);

	BigDecimal earningPerProduct(Long id, ProductDTO productDto);
	
}
