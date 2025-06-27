package com.pruebas.sistema_inventario.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.CategoryDTO;
import com.pruebas.sistema_inventario.entities.Category;
import com.pruebas.sistema_inventario.repository.CategoryRepository;
import com.pruebas.sistema_inventario.service.interfaces.CategoryService;

import lombok.Builder;

@Service @Builder
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public CategoryDTO save(CategoryDTO categoryDto) {
		Category category = modelMapper.map(categoryDto, Category.class);
		Category saved = categoryRepository.save(category);
		return modelMapper.map(saved, CategoryDTO.class);
	}

	@Override
	public CategoryDTO findById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, CategoryDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public CategoryDTO update(Long id, CategoryDTO categoryDto) {
		Category category = categoryRepository.findById(id)
				.orElseThrow();
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		category.setActive(categoryDto.isActive());
		Category updated = categoryRepository.save(category);
		return modelMapper.map(updated, CategoryDTO.class);
	}

	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

}
