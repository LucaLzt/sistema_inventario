package com.pruebas.sistema_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
