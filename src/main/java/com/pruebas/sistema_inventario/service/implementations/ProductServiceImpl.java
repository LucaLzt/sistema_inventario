package com.pruebas.sistema_inventario.service.implementations;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.dtos.ProductMovementsDTO;
import com.pruebas.sistema_inventario.entities.Category;
import com.pruebas.sistema_inventario.entities.InventoryMovement;
import com.pruebas.sistema_inventario.entities.Product;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.repository.ProductRepository;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;
import com.pruebas.sistema_inventario.specifications.ProductByBranchSpecification;
import com.pruebas.sistema_inventario.specifications.ProductSpecification;

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
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category_name"));
		var spec = ProductSpecification.filter(search, categories);
		Page<Product> products = productRepository.findAll(spec, pageable);
		return products.map(object -> modelMapper.map(object, ProductDTO.class));
	}

	@Override
	public long countAll() {
		return productRepository.count();
	}

	@Override
	public long countStockAvailable() {
		List<ProductDTO> products = findAll();
		long total = 0;
		for(ProductDTO product : products) {
			total += product.getStockActual();
		}
		return total;
	}

	@Override
	public long countLowStock() {
		return productRepository.countLowStock();
	}

	@Override
	public long countOutOfStock() {
		return productRepository.countOutOfStock();
	}

	@Override
	public List<ProductDTO> findByLowStock() {
		return productRepository.findByLowStock()
				.stream()
				.map(object -> modelMapper.map(object, ProductDTO.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ProductMovementsDTO> popularProducts() {
		return productRepository.findTop5PopularProducts()
				.stream()
				.limit(5)
				.map(object -> ProductMovementsDTO.builder()
						.product(modelMapper.map(object.getProduct(), ProductDTO.class))
						.totalMovements(object.getTotalMovements())
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public Page<ProductDTO> findByBranchWithFilters(Long id, String filter, List<Long> categories, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Specification<Product> spec = ProductByBranchSpecification.filterByBranch(id, filter, categories);
		Page<Product> products = productRepository.findAll(spec, pageable);
		return products.map(object -> modelMapper.map(object, ProductDTO.class));
	};
	
	@Override
	public BigDecimal earningPerProduct(Long id, ProductDTO productDto) {
		BigDecimal earnings = BigDecimal.ZERO;
		Product product = productRepository.findById(productDto.getId())
				.orElseThrow();
		for(InventoryMovement mov : product.getMovements()) {
			BigDecimal movementTotal = BigDecimal.ZERO;
			
			if(mov.getTypeMovement() == TypeMovement.OUT) { // MovementTotal = (PriceUnit * SellingPercentage) * Amount
				movementTotal = mov.getPriceUnit()
						.multiply(productDto.getSellingPorcentage())
						.multiply(BigDecimal.valueOf(mov.getAmount()));
				earnings = earnings.add(movementTotal);
			} 
			else if(mov.getTypeMovement() == TypeMovement.IN) { // MovementTotal = PriceUnit * Amount
				movementTotal = mov.getPriceUnit()
						.multiply(BigDecimal.valueOf(mov.getAmount()));
				earnings = earnings.subtract(movementTotal);
			}	
		}
		return earnings;
	}

}
