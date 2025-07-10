package com.pruebas.sistema_inventario.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.sistema_inventario.dtos.BranchDTO;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;

import lombok.Builder;

@Controller @Builder
@RequestMapping("/branches")
public class BranchController {
	
	private final BranchService branchService;
	
	@GetMapping("/home")
	public String branchHome(Model model, @RequestParam(required = false, defaultValue="") String filter) {
		List<BranchDTO> branches = branchService.findByFilter(filter);
		model.addAttribute("branch", new BranchDTO());
		model.addAttribute("branches", branches);
		return "branch/principal";
	}
	
	@GetMapping("/{id}/details")
	public String branchDetails(Model model, @PathVariable Long id) {
		BranchDTO branch = branchService.findById(id);
		model.addAttribute("branch", branch);
		return "branch/details";
	}
	
	@PostMapping("/add")
	public String addBranch(Model model, @ModelAttribute BranchDTO branchDto) {
		branchService.save(branchDto);
		return "redirect:/branches/home?save=ok";
	}
	
}
