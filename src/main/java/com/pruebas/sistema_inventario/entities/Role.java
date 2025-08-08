package com.pruebas.sistema_inventario.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	ADMIN,
	EMPLOYEE;

	@Override
	public String getAuthority() {
		return "ROLE_" + this.name();
	}

}
