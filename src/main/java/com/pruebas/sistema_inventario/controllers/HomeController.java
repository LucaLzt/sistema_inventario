package com.pruebas.sistema_inventario.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/")
public class HomeController {
	
	@GetMapping("/")
	public String redirectToHome() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
        // Principal Metrics
        model.addAttribute("totalProducts", 1234);
        model.addAttribute("stockAvailable", 8567);
        model.addAttribute("lowStock", 23);
        model.addAttribute("outOfStock", 5);
        
        // Lista vacía para evitar errores
        model.addAttribute("productsStockLow", List.of());
        model.addAttribute("lastMovements", List.of());
        model.addAttribute("mostMovedProducts", List.of());
		
		return "home/index";
	}
	
}
