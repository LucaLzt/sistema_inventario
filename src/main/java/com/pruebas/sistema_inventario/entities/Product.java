package com.pruebas.sistema_inventario.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="product")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String code;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "price_unit", precision = 10, scale = 2, nullable = false)
	private BigDecimal priceUnit;

	@Column(name = "selling_percentage", precision = 10, scale = 2, nullable = false)
	private BigDecimal sellingPercentage;
	
	@Column(name = "selling_price", precision = 10, scale = 2, nullable = true)
	private BigDecimal sellingPrice;
	
	@Column(name = "stock_actual", nullable = false)
	private Integer stockActual;
	
	@Column(name = "stock_minimum", nullable = false)
	private Integer stockMinimum;
	
	@Column(name = "active")
	private boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@OneToMany(mappedBy = "product")
	private List<InventoryMovement> movements;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
