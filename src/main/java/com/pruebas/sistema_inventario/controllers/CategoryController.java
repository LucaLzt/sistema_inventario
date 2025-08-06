package com.pruebas.sistema_inventario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.CategoryDTO;
import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/categories")
public class CategoryController {
	
	private final InventoryMovementService movementService;
	private final CategoryService categoryService;
	private final ProductService productService;
	
	@PostMapping("/add")
	public String addCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDto, 
			BindingResult result,
			Model model) {
		
		if(result.hasErrors()) {
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
	        model.addAttribute("movement", new InventoryMovementDTO());
	        
	        // If there are validation errors, show the modal to add a new category
	     	model.addAttribute("showAddCategoryModal", true);
	        
	        return "home/index";
		}
		
		categoryService.save(categoryDto);
		
		return "redirect:/home?addCategory=ok";
	}
	
}
