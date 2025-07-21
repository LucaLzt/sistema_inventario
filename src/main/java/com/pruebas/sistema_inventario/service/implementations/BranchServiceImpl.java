package com.pruebas.sistema_inventario.service.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.pruebas.sistema_inventario.dtos.BranchDTO;
import com.pruebas.sistema_inventario.entities.Branch;
import com.pruebas.sistema_inventario.repository.BranchRepository;
import com.pruebas.sistema_inventario.service.interfaces.BranchService;

import lombok.Builder;

@Service @Builder
public class BranchServiceImpl implements BranchService {
	
	private final BranchRepository branchRepository;
	private final ModelMapper modelMapper;

	@Override
	public BranchDTO save(BranchDTO branchDto) {
		Branch branch = modelMapper.map(branchDto, Branch.class);
		Branch saved = branchRepository.save(branch);
		return modelMapper.map(saved, BranchDTO.class);
	}

	@Override
	public BranchDTO findById(Long id) {
		Branch branch = branchRepository.findById(id)
				.orElseThrow();
		return modelMapper.map(branch, BranchDTO.class);
	}

	@Override
	public List<BranchDTO> findAll() {
		return branchRepository.findAll()
				.stream()
				.map(object -> modelMapper.map(object, BranchDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public BranchDTO update(Long id, BranchDTO branchDto) {
		Branch branch = branchRepository.findById(id)
				.orElseThrow();
		branch.setName(branchDto.getName());
		branch.setAddress(branchDto.getAddress());
		Branch updated = branchRepository.save(branch);
		return modelMapper.map(updated, BranchDTO.class);
	}

	@Override
	public void deleteById(Long id) {
		branchRepository.deleteById(id);
	}
	
	@Override
	public List<BranchDTO> findByFilter(String filter) {
		List<Branch> branches = branchRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(filter, filter);
		return branches.stream()
				.map(object -> modelMapper.map(object, BranchDTO.class))
				.collect(Collectors.toList());
	}

}
