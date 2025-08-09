package com.pruebas.sistema_inventario.dtos;

import java.time.LocalDateTime;

import com.pruebas.sistema_inventario.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
	
	private Long id;
	private String email;
	private String password;
	private String fullName;
	private Long mobile;
	private Role role;
	private LocalDateTime registeredAt;
	private LocalDateTime updatedAt;
	
}
