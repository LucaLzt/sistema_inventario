package com.pruebas.sistema_inventario.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.entities.Category;
import com.pruebas.sistema_inventario.entities.Product;
import com.pruebas.sistema_inventario.repository.ProductRepository;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Service @Builder
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public ProductDTO save(ProductDTO productDto) {
		Product product = modelMapper.map(productDto, Product.class);
		if(product == null || product.getCategory() == null) {
			throw new RuntimeException();
		}
		Product saved = productRepository.save(product);
		return modelMapper.map(saved, ProductDTO.class);
	}

	@Override
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public List<ProductDTO> findAll() {
		return productRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, ProductDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProductDTO update(Long id, ProductDTO productDto) {
		Product product = productRepository.findById(id)
				.orElseThrow();
		product.setName(productDto.getName());
		product.setDescription(productDto.getDescription());
		product.setPriceUnit(productDto.getPriceUnit());
		product.setStockActual(productDto.getStockActual());
		product.setStockMinimum(productDto.getStockMinimum());
		product.setActive(productDto.isActive());
		product.setCategory(modelMapper.map(productDto.getCategory(), Category.class));
		Product updated = productRepository.save(product);
		return modelMapper.map(updated, ProductDTO.class);
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}
	
	@Override
	public Page<ProductDTO> findFiltered(String search, List<Long> categories, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Product> products;
		
		if((search == null || search.isBlank()) && (categories == null || categories.isEmpty())) {
			products = productRepository.findAll(pageable);
		}
		// Only Categories
		else if((search == null || search.isBlank()) && categories != null && !categories.isEmpty()) {
			products = productRepository.findByCategory_IdIn(categories, pageable);
		}
		// Only Search
		else if((categories == null || categories.isEmpty()) && search != null && !search.isBlank()) {
			products = productRepository.findByNameContains(search, pageable);
		}
		// Both Filters
		else {
			products = productRepository.findByNameContainsIgnoreCaseAndCategory_IdIn(search, categories, pageable);
		}
		
		return products.map(object -> modelMapper.map(object, ProductDTO.class));
	}

}
