package com.pruebas.sistema_inventario.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.entities.InventoryMovement;
import com.pruebas.sistema_inventario.entities.Product;
import com.pruebas.sistema_inventario.repository.InventoryMovementRepository;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;

import lombok.Builder;

@Service @Builder
public class InventoryMovementServiceImpl implements InventoryMovementService {
	
	private final InventoryMovementRepository inventoryMovementRepository;
	private final ModelMapper modelMapper;

	@Override
	public InventoryMovementDTO save(InventoryMovementDTO movementDTO) {
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
	
	
	
}
