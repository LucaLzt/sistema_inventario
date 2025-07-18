package com.pruebas.sistema_inventario.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pruebas.sistema_inventario.entities.Product;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ProductByBranchSpecification {
	
	public static Specification<Product> filterByBranch(
				Long branchId,
				String name,
				List<Long> categoriesIds
			) {
		
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction();
			
			// MANDATORY filter by branch
			if (branchId != null) {
				Join<Object, Object> movementJoin = root.join("movements", JoinType.INNER);
				predicate = cb.and(predicate, cb.equal(movementJoin.get("branch").get("id"), branchId));
				query.distinct(true);
			}
			
			// Filter by name
			if (name != null && !name.isBlank()) {
				Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
				predicate = cb.and(predicate, namePredicate);
			}
			
			// Filter by categories
			if (categoriesIds != null && !categoriesIds.isEmpty()) {
				predicate = cb.and(predicate, root.get("category").get("id").in(categoriesIds));
			}
			
			return predicate;		
		};
	}
}
