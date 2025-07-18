package com.pruebas.sistema_inventario.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.BranchDTO;
import com.pruebas.sistema_inventario.dtos.InventoryMovementDTO;
import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.dtos.ProductsEarningsDTO;
import com.pruebas.sistema_inventario.entities.TypeMovement;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;
import com.pruebas.sistema_inventario.service.interfaces.InventoryMovementService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/branches")
public class BranchController {
	
	private final BranchService branchService;
	private final ProductService productService;
	private final CategoryService categoryService;
	private final InventoryMovementService movementService;
	
	@GetMapping("/home")
	public String branchHome(Model model, @RequestParam(required = false, defaultValue="") String filter) {
		List<BranchDTO> branches = branchService.findByFilter(filter);
		model.addAttribute("branch", new BranchDTO());
		model.addAttribute("branches", branches);
		return "branch/principal";
	}
	
	@GetMapping("/{id}/details")
	public String branchDetails(@PathVariable Long id,
			// Filters and Pagination for Products
			@RequestParam(required = false, defaultValue="") String searchPr,
			@RequestParam(required = false) List<Long> categoriesPr,
			@RequestParam(defaultValue = "0") int pagePr,
			
			// Filters and Pagination for Movements
			@RequestParam(required = false) LocalDate dateFromMv,
    		@RequestParam(required = false) LocalDate dateToMv,
			@RequestParam(required = false) TypeMovement typeMv,
			@RequestParam(required = false) Long productIdMv,
			@RequestParam(defaultValue = "0") int pageMv,
			
			Model model) {

		BranchDTO branch = branchService.findById(id);
		model.addAttribute("branch", branch);
		
		// Setup Static Data
		setupStaticData(model);
		
		// Setup Products Data
		setupProductsData(id, searchPr, categoriesPr, pagePr, model);
		
		// Setup Movements Data
		setupMovementsData(id, dateFromMv, dateToMv, typeMv, productIdMv, pageMv, model);
		
		return "branch/details";
	}
	
	// Private Method for Products
	private void setupProductsData(Long branchId, String search, List<Long> categories, int page, Model model) {
		int pageSize = 5;
		
		String searchParam = (search != null && !search.isBlank()) ? null : search;
		
		Page<ProductDTO> productsFiltered = productService.findByBranchWithFilters(branchId, searchParam, categories, page, pageSize);
		
		// Calculate earnings for each product
		List<ProductsEarningsDTO> productEarnings = productsFiltered.stream()
				.map(object -> {
					BigDecimal earning = productService.earningPerProduct(branchId, object);
					return new ProductsEarningsDTO(object, earning);})
				.collect(Collectors.toList());
		
		// Add attributes to the model with prefix "pr"
		model.addAttribute("productEarnings", productEarnings);
		model.addAttribute("currentPagePr", page);
		model.addAttribute("totalPagesPr", productsFiltered.getTotalPages());
		model.addAttribute("hasNextPr", productsFiltered.hasNext());
		model.addAttribute("hasPreviousPr", productsFiltered.hasPrevious());
		
		// Add params to the model
		model.addAttribute("searchPr", search);
		model.addAttribute("categoriesPr", categories);
	}
	
	// Private Method for Movements
	private void setupMovementsData(Long branchId, LocalDate dateFrom, LocalDate dateTo, TypeMovement type, Long productId,
			int page, Model model) {
		
		Page<InventoryMovementDTO> movementsFiltered = movementService.findByBranchWithFilters(branchId, dateFrom, dateTo, 
				type, "system", productId, page, 5);
		
		// Add attributes to the model with prefix "mv"
		model.addAttribute("movements", movementsFiltered.getContent());
		model.addAttribute("currentPageMv", page);
		model.addAttribute("totalPagesMv", movementsFiltered.getTotalPages());
		model.addAttribute("hasNextMv", movementsFiltered.hasNext());
		model.addAttribute("hasPreviousMv", movementsFiltered.hasPrevious());
		
		// Add params to the model
		model.addAttribute("dateFromMv", dateFrom);
		model.addAttribute("dateToMv", dateTo);
		model.addAttribute("typeMv", type);
		model.addAttribute("productIdMv", productId);
	}	
	
	// Private method for Static Data
	private void setupStaticData(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("types", Arrays.asList(TypeMovement.values()));
		model.addAttribute("products", productService.findAll());
	}
	
	@PostMapping("/add")
	public String addBranch(Model model, @ModelAttribute BranchDTO branchDto) {
		branchService.save(branchDto);
		return "redirect:/branches/home?save=ok";
	}
	
}
