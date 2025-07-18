package com.pruebas.sistema_inventario.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.pruebas.sistema_inventario.dtos.ProductDTO;
import com.pruebas.sistema_inventario.entities.InventoryMovement;
import com.pruebas.sistema_inventario.entities.TypeMovement;

import jakarta.persistence.criteria.Predicate;

public class InventoryMovementSpecification {
	
	public static Specification<InventoryMovement> filter(
			LocalDate dateFrom,
			LocalDate dateTo,
	        TypeMovement type,
	        String user,
	        ProductDTO product
	) {
	    return (root, query, cb) -> {
	        Predicate predicate = cb.conjunction();

	        if (dateFrom != null && dateTo != null) {
	            predicate = cb.and(predicate, cb.between(root.get("movementDate"), dateFrom, dateTo));
	        } else if (dateFrom != null) {
	            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("movementDate"), dateFrom));
	        } else if (dateTo != null) {
	            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("movementDate"), dateTo));
	        }

	        if (type != null) {
	            predicate = cb.and(predicate, cb.equal(root.get("typeMovement"), type));
	        }
	        if (user != null && !user.isBlank()) {
	            predicate = cb.and(predicate, cb.equal(root.get("registeredUser"), user));
	        }
	        if (product != null && product.getId() != null) {
	            predicate = cb.and(predicate, cb.equal(root.get("product").get("id"), product.getId()));
	        }
	        return predicate;
	    };
	}
	
	
	public static Specification<InventoryMovement> filterByBranch(
			Long branchId,
			LocalDate dateFrom,
			LocalDate dateTo,
			TypeMovement type,
			String user,
			ProductDTO product
	) {
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction();
			
			if (branchId != null) {
				predicate = cb.and(cb.equal(root.get("branch").get("id"), branchId));
			}
			
			if (dateFrom != null && dateTo != null) {
	            predicate = cb.and(predicate, cb.between(root.get("movementDate"), dateFrom, dateTo));
	        } else if (dateFrom != null) {
	            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("movementDate"), dateFrom));
	        } else if (dateTo != null) {
	            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("movementDate"), dateTo));
	        }

	        if (type != null) {
	            predicate = cb.and(predicate, cb.equal(root.get("typeMovement"), type));
	        }
	        if (user != null && !user.isBlank()) {
	            predicate = cb.and(predicate, cb.equal(root.get("registeredUser"), user));
	        }
	        if (product != null && product.getId() != null) {
	            predicate = cb.and(predicate, cb.equal(root.get("product").get("id"), product.getId()));
	        }
	        return predicate;
		};
	}
	
}
