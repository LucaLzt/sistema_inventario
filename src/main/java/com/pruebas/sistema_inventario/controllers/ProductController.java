package com.pruebas.sistema_inventario.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;
import com.pruebas.sistema_inventario.service.interfaces.ProductService;
import com.pruebas.sistema_inventario.utils.ProductCodeGenerator;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final ProductCodeGenerator productCodeGenerator;
	
	@GetMapping("/home")
	public String products(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) List<Long> categories,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		int pageSize = 11;
		Page<ProductDTO> filtered = productService.findFiltered(search, categories, page, pageSize);
		model.addAttribute("product", new ProductDTO());
		model.addAttribute("products", filtered);
		model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", filtered.getTotalPages());
		model.addAttribute("hasNext", filtered.hasNext());
		model.addAttribute("hasPrevious", filtered.hasPrevious());

		return "products/principal";
	}
	
	@PostMapping("/add")
	public String addProduct(@ModelAttribute ProductDTO product) {
		product.setCode(productCodeGenerator.generateUserCode("LucaLzt"));
		product.setSellingPorcentage(product.getSellingPorcentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(BigDecimal.ONE));
		productService.save(product);
		return "redirect:/products/home?add=ok";
	}
	
	@PostMapping("/edit")
	public String editProduct(@ModelAttribute ProductDTO productDto) {
		System.out.println(productDto.toString());
		productService.update(productDto.getId(), productDto);
		return "redirect:/products/home?update=ok";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteById(id);
		return "redirect:/products/home?delete=ok";
	}
	
}
