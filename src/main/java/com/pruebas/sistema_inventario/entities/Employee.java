package com.pruebas.sistema_inventario.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {
	
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = true)
	private Branch branch;
	
}