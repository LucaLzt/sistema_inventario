package com.pruebas.sistema_inventario.service.interfaces;

import com.pruebas.sistema_inventario.dtos.PasswordChangeDTO;

public interface UserService {
	
	void changePassword(Long id, PasswordChangeDTO passwordChange);
	
}
