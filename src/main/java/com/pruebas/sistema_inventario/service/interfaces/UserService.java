package com.pruebas.sistema_inventario.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;
import com.pruebas.sistema_inventario.dtos.UserDTO;
import com.pruebas.sistema_inventario.entities.Role;

public interface UserService {
	
	void changePassword(Long id, PasswordChangeDTO passwordChange);

	List<UserDTO> findAllPendingRequests();

	void approveRequest(Long id);

	void rejectRequest(Long id);

	Page<UserDTO> findByFilters(Boolean approved, Role role, String search, int page, int size);
	
	boolean deleteById(Long id, boolean isActive);

	UserDTO findById(Long id);
	
}
