package com.pruebas.sistema_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
