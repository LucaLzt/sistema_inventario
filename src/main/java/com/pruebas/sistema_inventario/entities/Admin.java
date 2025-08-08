package com.pruebas.sistema_inventario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
	
	@Column(name = "super_admin", nullable = false)
	private boolean superAdmin;
	
}
