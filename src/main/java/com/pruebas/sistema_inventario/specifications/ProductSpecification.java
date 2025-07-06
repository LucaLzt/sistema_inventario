package com.pruebas.sistema_inventario.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pruebas.sistema_inventario.entities.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
	public static Specification<Product> filter(
            String name,
            List<Long> categoryIds
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            
            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicate = cb.and(predicate, root.get("category").get("id").in(categoryIds));
            }
            
            return predicate;
        };
    }
}
