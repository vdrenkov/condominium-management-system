package com.mjt.condo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="apartments")
public class Apartment {
	
	@Id	
	@Column(unique=true, nullable=false, updatable=false)
 private Long number;
	
	@Column(nullable=false)
 private int numberOfRooms;
	
	@Column()
 private double rent;
	
@ManyToOne(cascade=CascadeType.ALL)
@JoinColumn(name="tenant_id")
private Tenant tenant;
}