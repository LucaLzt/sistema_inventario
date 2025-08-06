package com.pruebas.sistema_inventario.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

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
import com.pruebas.sistema_inventario.utils.ProductCodeGenerator;

import jakarta.validation.Valid;
import lombok.Builder;

@Controller @Builder
@RequestMapping("/products")
public class ProductController {
	
	private static final int PAGE_SIZE = 11; // Default page size for pagination
	private final BranchService branchService;
	private final ProductService productService;
	private final CategoryService categoryService;
	private final InventoryMovementService movementService;
	private final ProductCodeGenerator productCodeGenerator;
	
	@GetMapping("/home")
	public String products(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) List<Long> categoriesIds,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		Page<ProductDTO> filtered = productService.findFiltered(search, categoriesIds, page, PAGE_SIZE);
		model.addAttribute("product", new ProductDTO());
		model.addAttribute("products", filtered);
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", filtered.getTotalPages());
		model.addAttribute("hasNext", filtered.hasNext());
		model.addAttribute("hasPrevious", filtered.hasPrevious());
	    model.addAttribute("categoriesIds", categoriesIds);
	    model.addAttribute("search", search);

		return "products/principal";
	}
	
	@PostMapping("/add")
	public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDto,
			BindingResult result, 
			@RequestParam String fromView,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) List<Long> categoriesIds,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		if (result.hasErrors()) {
			
			// If there are validation errors, show the modal to add a new product
			model.addAttribute("showAddProductModal", true);
			
			// Maintain the context of where the user was
			if (fromView.equals("home")) {
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
		        model.addAttribute("movement", new InventoryMovementDTO()); 
		        model.addAttribute("category", new CategoryDTO());
		        
		        // List of Categories for Add Product Modal
		        model.addAttribute("categories", categoryService.findAll());
		        
		        // List of Products, Branches and Types for Add Movement Modal
		        model.addAttribute("types", Arrays.asList(TypeMovement.values()));
		        model.addAttribute("products", productService.findAll());
		        model.addAttribute("branches", branchService.findAll());
		        
				// Redirect to the home view with the modal open
				return "home/index";
			} else if (fromView.equals("product")) {
				Page<ProductDTO> filtered = productService.findFiltered(search, categoriesIds, page, PAGE_SIZE);
				
				model.addAttribute("products", filtered);
				model.addAttribute("categories", categoryService.findAll());
				model.addAttribute("totalPages", filtered.getTotalPages());
				model.addAttribute("hasNext", filtered.hasNext());
				model.addAttribute("hasPrevious", filtered.hasPrevious());
				model.addAttribute("search", search);
				model.addAttribute("currentPage", page);
				model.addAttribute("categoriesIds", categoriesIds);
				
				// Return to the products view with the modal open
				return "products/principal";
			}
		}
		
		// If there are no validation errors, proceed to save the product
		productDto.setCode(productCodeGenerator.generateUserCode("LucaLzt"));
		productDto.setSellingPercentage(productDto.getSellingPercentage()
				.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
				.add(BigDecimal.ONE));
		productService.save(productDto);
		
		// If the product was added successfully, redirect to the appropriate view
		return "redirect:" + (fromView.equals("home") ? "/home" : "/products/home") + "?addProduct=ok";
	}
	
	@PostMapping("/edit")
	public String editProduct(@Valid @ModelAttribute("product") ProductDTO productDto,
			BindingResult result, 
			@RequestParam(required = false) String search,
			@RequestParam(required = false) List<Long> categoriesIds,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		if (result.hasErrors()) {
			Page<ProductDTO> filtered = productService.findFiltered(search, categoriesIds, page, PAGE_SIZE);
			
			model.addAttribute("products", filtered);
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("totalPages", filtered.getTotalPages());
			model.addAttribute("hasNext", filtered.hasNext());
			model.addAttribute("hasPrevious", filtered.hasPrevious());
			model.addAttribute("search", search);
			model.addAttribute("currentPage", page);
			model.addAttribute("categoriesIds", categoriesIds);
			
			// If there are validation errors, show the modal to edit the product
			model.addAttribute("showEditProductModal", true);
			return "products/principal";
		}
		productService.update(productDto.getId(), productDto);
		return "redirect:/products/home?update=ok";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteById(id);
		return "redirect:/products/home?delete=ok";
	}
	
}
