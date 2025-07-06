package com.pruebas.sistema_inventario.controllers;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/movements")
public class MovementsController {
	
	private final InventoryMovementService movementService;
	private final ProductService productService;
	
	@GetMapping("/home")
    public String movementsHome(
    		@RequestParam(required = false) LocalDate dateFrom,
    		@RequestParam(required = false) LocalDate dateTo,
    		@RequestParam(required = false) String type,
    		@RequestParam(required = false) Long productId,
    		@RequestParam(defaultValue = "0") int page,
    		Model model) {
		
		model.addAttribute("movement", new InventoryMovementDTO());
        model.addAttribute("movements", movementService.findAll());
        model.addAttribute("types", Arrays.asList(TypeMovement.values()));
        model.addAttribute("products", productService.findAll());
		model.addAttribute("currentPage", 0);
		// model.addAttribute("totalPages", filtered.getTotalPages());
		// model.addAttribute("hasNext", filtered.hasNext());
		// model.addAttribute("hasPrevious", filtered.hasPrevious());

        return "movements/principal";
    }
	
	@PostMapping("/add")
	public String addProduct(@ModelAttribute InventoryMovementDTO movementDto) {
		movementService.save(movementDto);
		return "redirect:/movements/home?add=ok";
	}
	
}
