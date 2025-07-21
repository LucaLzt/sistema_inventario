package com.pruebas.sistema_inventario.service.implementations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.entities.InventoryMovement;
import com.pruebas.sistema_inventario.entities.Product;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.repository.InventoryMovementRepository;
import com.pruebas.sistema_inventario.repository.ProductRepository;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;
import com.pruebas.sistema_inventario.specifications.InventoryMovementSpecification;

import lombok.Builder;

@Service @Builder
public class InventoryMovementServiceImpl implements InventoryMovementService {
	
	private final InventoryMovementRepository inventoryMovementRepository;
	private final ProductRepository productRepository;
	private final ProductService productService;
	private final ModelMapper modelMapper;

	@Override
	public InventoryMovementDTO save(InventoryMovementDTO movementDTO) {
		Product product = productRepository.findById(movementDTO.getProduct().getId())
				.orElseThrow();
		movementDTO.setBeforeStock(product.getStockActual());
		
		if(movementDTO.getTypeMovement() == TypeMovement.IN) {
			product.setStockActual(product.getStockActual() + movementDTO.getAmount());
		}
		else if(movementDTO.getTypeMovement() == TypeMovement.OUT) {
			int newStock = product.getStockActual() - movementDTO.getAmount();
			if(newStock < 0) {
				throw new IllegalArgumentException("There is not enough stock to complete the shipment.");
			}
			product.setStockActual(newStock);
		}
		else if(movementDTO.getTypeMovement() == TypeMovement.ADJUSTMENT) {
			product.setStockActual(movementDTO.getAmount());
		}
		
		ProductDTO updated = productService.update(product.getId(), modelMapper.map(product, ProductDTO.class));
		movementDTO.setAfterStock(updated.getStockActual());
	    movementDTO.setPriceUnit(updated.getPriceUnit());
		
	    // If date is null, we set the current date
	    if (movementDTO.getMovementDate() == null) {
	        movementDTO.setMovementDate(LocalDateTime.now());
	    }
	    
		InventoryMovement movement = modelMapper.map(movementDTO, InventoryMovement.class);
		InventoryMovement saved = inventoryMovementRepository.save(movement);
		return modelMapper.map(saved, InventoryMovementDTO.class);
	}

	@Override
	public InventoryMovementDTO findById(Long id) {
		InventoryMovement movement = inventoryMovementRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(movement, InventoryMovementDTO.class);
	}

	@Override
	public List<InventoryMovementDTO> findAll() {
		return inventoryMovementRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, InventoryMovementDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public InventoryMovementDTO update(Long id, InventoryMovementDTO movementDto) {
		InventoryMovement movement = inventoryMovementRepository.findById(id)
				.orElseThrow();
		movement.setProduct(modelMapper.map(movementDto.getProduct(), Product.class));
		movement.setTypeMovement(movementDto.getTypeMovement());
		movement.setAmount(movementDto.getAmount());
		movement.setPriceUnit(movementDto.getPriceUnit());
		movement.setMotive(movementDto.getMotive());
		movement.setBeforeStock(movementDto.getBeforeStock());
		movement.setAfterStock(movementDto.getAfterStock());
		movement.setRegisteredUser(movementDto.getRegisteredUser());
		InventoryMovement updated = inventoryMovementRepository.save(movement);
		return modelMapper.map(updated, InventoryMovementDTO.class);
	}

	@Override
	public void deleteById(Long id) {
		inventoryMovementRepository.deleteById(id);
	}
	
	@Override
	public Page<InventoryMovementDTO> filtered(LocalDate dateFrom, LocalDate dateTo, 
			TypeMovement type, String user, Long productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));
		ProductDTO product = null;
		if (productId != null) {
		    product = productService.findById(productId);
		}
	    var spec = InventoryMovementSpecification.filter(dateFrom, dateTo, type, user, product);
	    Page<InventoryMovement> movements = inventoryMovementRepository.findAll(spec, pageable);
	    return movements.map(object -> modelMapper.map(object, InventoryMovementDTO.class));
	}
	
	@Override
	public List<InventoryMovementDTO> lastestMovements() {
		return inventoryMovementRepository.findTop5ByOrderByMovementDateDesc()
				.stream()
				.map(object -> modelMapper.map(object, InventoryMovementDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<InventoryMovementDTO> findByBranch(Long id) {
		return inventoryMovementRepository.findByBranch_Id(id)
				.stream()
				.map(object -> modelMapper.map(object, InventoryMovementDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public Page<InventoryMovementDTO> findByBranchWithFilters(Long branchId, LocalDate dateFrom, LocalDate dateTo, TypeMovement type,
			String user, Long productId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		ProductDTO product = null;
		if (productId != null) {
		    product = productService.findById(productId);
		}
		Specification<InventoryMovement> spec = InventoryMovementSpecification.filterByBranch(branchId, dateFrom, dateTo, 
				type, user, product);
		Page<InventoryMovement> movements = inventoryMovementRepository.findAll(spec, pageable);
	    return movements.map(object -> modelMapper.map(object, InventoryMovementDTO.class));
	}
	
}
