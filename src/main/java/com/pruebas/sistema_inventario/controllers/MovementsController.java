package com.pruebas.sistema_inventario.controllers;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.CategoryDTO;
import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/movements")
public class MovementsController {
	
	private static final int PAGE_SIZE = 11; // Default page size for pagination
	private final BranchService branchService;
	private final ProductService productService;
	private final CategoryService categoryService;
	private final InventoryMovementService movementService;
	
	@GetMapping("/home")
    public String movementsHome(
    		@RequestParam(required = false) LocalDate dateFrom,
    		@RequestParam(required = false) LocalDate dateTo,
    		@RequestParam(required = false) TypeMovement type,
    		@RequestParam(required = false) Long productId,
    		@RequestParam(defaultValue = "0") int page,
    		Model model) {
		Page<InventoryMovementDTO> filtered = movementService.filtered(dateFrom, dateTo, type, "system", productId, page, PAGE_SIZE);
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
	public String addMovement(@Valid @ModelAttribute("movement") InventoryMovementDTO movementDto,
			BindingResult result,
			@RequestParam String fromView,
			@RequestParam(required = false) LocalDate dateFrom,
    		@RequestParam(required = false) LocalDate dateTo,
    		@RequestParam(required = false) TypeMovement type,
    		@RequestParam(required = false) Long productId,
    		@RequestParam(defaultValue = "0") int page,
			Model model) {
		
		if (result.hasErrors()) {
			
			// If there are validation errors, show the modal to add a new movement
			model.addAttribute("showAddMovementModal", true);
			
			if(fromView.equals("home")) {
				// Principal Metrics
		        model.addAttribute("totalProducts", productService.countAll());
		        model.addAttribute("stockAvailable", productService.countStockAvailable());
		        model.addAttribute("lowStock", productService.countLowStock());
		        model.addAttribute("outOfStock", productService.countOutOfStock());
		        
		        // Alerts and Warnings
		        model.addAttribute("productsStockLow", productService.findByLowStock());
		        
		        // Latest Movements and Popular Products
		        model.addAttribute("lastMovements", movementService.lastestMovements());
		        model.addAttribute("mostMovedProducts", productService.popularProducts());
		        
		        // Objects for modals in Quick Actions
		        model.addAttribute("product", new ProductDTO());
		        model.addAttribute("category", new CategoryDTO());
		        
		        // List of Categories for Add Product Modal
		        model.addAttribute("categories", categoryService.findAll());
		        
		        // List of Products, Branches and Types for Add Movement Modal
		        model.addAttribute("types", Arrays.asList(TypeMovement.values()));
		        model.addAttribute("products", productService.findAll());
		        model.addAttribute("branches", branchService.findAll());
		        
		        // Redirect to the home view with the modal open
				return "home/index";
			} else if(fromView.equals("movement")) {
				Page<InventoryMovementDTO> filtered = movementService.filtered(dateFrom, dateTo, type, "system", productId, page, PAGE_SIZE);
				
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
			
		}
		movementService.save(movementDto);
		
		// If the product was added successfully, redirect to the appropriate view
		return "redirect:" + (fromView.equals("home") ? "/home" : "/movements/home") + "?addMovement=ok";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		movementService.deleteById(id);
		return "redirect:/movements/home?delete=ok";
	}
	
}
