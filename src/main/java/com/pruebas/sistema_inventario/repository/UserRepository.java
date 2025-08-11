package com.pruebas.sistema_inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pruebas.sistema_inventario.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, 
	JpaSpecificationExecutor<User> {

	Optional<User> findByEmail(String email);
	List<User> findAllByApproved(boolean approved);

}
