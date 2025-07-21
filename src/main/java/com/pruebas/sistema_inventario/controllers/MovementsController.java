package com.pruebas.sistema_inventario.controllers;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/movements")
public class MovementsController {
	
	private final InventoryMovementService movementService;
	private final BranchService branchService;
	private final ProductService productService;
	
	@GetMapping("/home")
    public String movementsHome(
    		@RequestParam(required = false) LocalDate dateFrom,
    		@RequestParam(required = false) LocalDate dateTo,
    		@RequestParam(required = false) TypeMovement type,
    		@RequestParam(required = false) Long productId,
    		@RequestParam(defaultValue = "0") int page,
    		Model model) {
		int pageSize = 11;
		Page<InventoryMovementDTO> filtered = movementService.filtered(dateFrom, dateTo, type, "system", productId, page, pageSize);
		model.addAttribute("movement", new InventoryMovementDTO());
        model.addAttribute("movements", filtered);
        model.addAttribute("types", Arrays.asList(TypeMovement.values()));
        model.addAttribute("products", productService.findAll());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", filtered.getTotalPages());
		model.addAttribute("hasNext", filtered.hasNext());
		model.addAttribute("hasPrevious", filtered.hasPrevious());
		model.addAttribute("branches", branchService.findAll());
		
		model.addAttribute("dateFrom", dateFrom);
		model.addAttribute("dateTo", dateTo);
		model.addAttribute("selectedType", type);
		model.addAttribute("productId", productId);

        return "movements/principal";
    }
	
	@PostMapping("/add")
	public String addMovement(@ModelAttribute InventoryMovementDTO movementDto) {
		movementService.save(movementDto);
		return "redirect:/movements/home?add=ok";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		movementService.deleteById(id);
		return "redirect:/movements/home?delete=ok";
	}
	
}
