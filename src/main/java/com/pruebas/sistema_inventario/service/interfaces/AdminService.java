package com.pruebas.sistema_inventario.service.interfaces;

import com.pruebas.sistema_inventario.dtos.AdminDTO;
import com.pruebas.sistema_inventario.dtos.RegisterDTO;

public interface AdminService {
	
	AdminDTO save(RegisterDTO registerDto);
	AdminDTO findById(Long id);
	AdminDTO update(Long id, AdminDTO adminDto);
	void deleteById(Long id);
	
}
