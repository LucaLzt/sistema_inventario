 package com.pruebas.sistema_inventario.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.entities.User;

import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
	
	public static Specification<User> filter(
			Role role,
			String search
	) {
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction();
			
			// Only users who are not approved
			predicate = cb.and(predicate, cb.isFalse(root.get("approved")));
			
			// Filter by role
			if (role != null) {
				predicate = cb.and(predicate, cb.equal(root.get("role"), role));
			}
			
			// Filter by search (name or email)
			if (search != null && !search.isBlank()) {
				String searchPattern = "%" + search.toLowerCase() + "%";
				Predicate fullNamePredicate = cb.like(cb.lower(root.get("fullName")), searchPattern);
				Predicate emailPredicate = cb.like(cb.lower(root.get("email")), searchPattern);
				predicate = cb.and(predicate, cb.or(fullNamePredicate, emailPredicate));
			}
			
			return predicate;
			
		};
	}
	
}
