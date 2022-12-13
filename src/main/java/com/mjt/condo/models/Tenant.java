package com.mjt.condo.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tenants")
public class Tenant implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalDate birthDate;

	@Column(unique = true)
	private String phoneNumber;

	@Column(unique = true)
	private String email;

	@OneToMany(
			mappedBy="tenant",
			cascade=CascadeType.ALL
			//orphanRemoval=true
			)
	private List<Apartment> apartments;
}