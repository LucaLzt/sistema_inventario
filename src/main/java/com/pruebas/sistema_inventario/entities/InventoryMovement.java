package com.pruebas.sistema_inventario.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "inventory_movements")
@NoArgsConstructor @AllArgsConstructor
public class InventoryMovement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type_movement", nullable = false)
	private TypeMovement typeMovement;
	
	@Column(name = "amount", nullable = false)
	private Integer amount;
	
	@Column(name = "price_unit", precision = 10, scale = 2)
	private BigDecimal priceUnit;
	
	@Column(name = "motive", nullable = true)
	private String motive;
	
	@Column(name = "before_stock", nullable = false)
	private Integer beforeStock;
	
	@Column(name = "after_stock", nullable = false)
	private Integer afterStock;
	
	@Column(name = "registered_user", nullable = false)
	private String registeredUser = "system";
	
	@CreationTimestamp
	@Column(name = "movement_date", nullable = false)
	private LocalDateTime movementDate;
	
	
}
