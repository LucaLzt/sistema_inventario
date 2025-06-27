package com.pruebas.sistema_inventario.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	boolean existsByCode(String code);
	List<Product> findByCategory_IdIn(List<Long> ids);
	List<Product> findByNameContains(String name);
	List<Product> findByNameContainsIgnoreCaseAndCategory_IdIn(String search, List<Long> categories);
	
	Page<Product> findByCategory_IdIn(List<Long> ids, Pageable pageable);
	Page<Product> findByNameContains(String name, Pageable pageable);
	Page<Product> findByNameContainsIgnoreCaseAndCategory_IdIn(String search, List<Long> categories, Pageable pageable);
	
}
