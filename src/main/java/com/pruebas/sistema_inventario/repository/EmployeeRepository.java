package com.pruebas.sistema_inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Optional<Employee> findByEmail(String email);
	
}
