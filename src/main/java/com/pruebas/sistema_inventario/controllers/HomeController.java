package com.pruebas.sistema_inventario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/")
public class HomeController {
	
	private final InventoryMovementService movementService;
	private final ProductService productService;
	
	@GetMapping("/")
	public String redirectToHome() {
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
        
        // List of products to avoid errors
        model.addAttribute("lastMovements", movementService.lastestMovements());
        model.addAttribute("mostMovedProducts", productService.popularProducts());
		
		return "home/index";
	}
	
}
