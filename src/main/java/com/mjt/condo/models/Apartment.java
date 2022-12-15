package com.mjt.condo.models;

import javax.persistence.*;

import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="apartments")
public class Apartment {
	
	@Id	
	@Column(unique=true, nullable=false, insertable = false, updatable = false)
 private Long number;
	
	@Column(nullable=false)
 private int numberOfRooms;
	
	@Column(nullable=false)
 private double rent;
	
@Column(nullable=true,updatable=true)
private Long tenant_id;
}