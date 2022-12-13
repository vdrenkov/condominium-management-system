package com.mjt.condo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mjt.condo.models.Apartment;

@Repository
public interface ApartmentRepository extends CrudRepository<Apartment, Long> {

	List<Apartment> findAll();
	Apartment findApartmentByNumber(Long number);
}
