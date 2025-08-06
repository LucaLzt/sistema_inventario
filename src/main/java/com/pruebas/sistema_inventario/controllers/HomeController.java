package com.pruebas.sistema_inventario.controllers;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.dtos.CategoryDTO;
import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/")
public class HomeController {
	
	private final InventoryMovementService movementService;
	private final CategoryService categoryService;
	private final ProductService productService;
	private final BranchService branchService;
	
	@GetMapping("/")
	public String redirectToHome() {
		return "redirect:/home";
	}
	
	@GetMapping("/index")
	public String redirectIndexToHome() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
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
        model.addAttribute("category", new CategoryDTO());
        
        // List of Categories for Add Product Modal
        model.addAttribute("categories", categoryService.findAll());
        
        // List of Products, Branches and Types for Add Movement Modal
        model.addAttribute("types", Arrays.asList(TypeMovement.values()));
        model.addAttribute("products", productService.findAll());
        model.addAttribute("branches", branchService.findAll());
		
		return "home/index";
	}
	
}
