package com.mjt.condo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mjt.condo.exceptions.ApartmentNotFoundException;
import com.mjt.condo.models.Apartment;
import com.mjt.condo.services.ApartmentService;

import lombok.SneakyThrows;

@RestController
@RequestMapping("/apartments")
public class ApartmentController {

	@Autowired
	private ApartmentService apartmentService;

	@GetMapping()
	public ResponseEntity<List<Apartment>> getAllApartments() {
		return ResponseEntity.ok(apartmentService.findAllApartments());
	}

	@SneakyThrows
	@GetMapping("/{id}")
	public ResponseEntity<Apartment> getApartmentById(@PathVariable Long id) throws ApartmentNotFoundException {
		Apartment apartment = apartmentService.findApartmentById(id);
		return ResponseEntity.ok(apartment);
	}

	@SneakyThrows
	@PostMapping()
	public ResponseEntity<Apartment> addApartment(@RequestBody Apartment apartment) {
		apartmentService.saveApartment(apartment);
		return new ResponseEntity<Apartment>(apartment, HttpStatus.CREATED);
	}

	@SneakyThrows
	@PutMapping("/{id}")
	public ResponseEntity<Apartment> updateApartment(@PathVariable Long id, @RequestBody Apartment apartment)
			throws ApartmentNotFoundException {
		Apartment updatedApartment = apartmentService.updateApartment(id, apartment);
		return ResponseEntity.ok(updatedApartment);
	}

	@SneakyThrows
	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteApartment(@PathVariable Long id) throws ApartmentNotFoundException {
		apartmentService.deleteApartmentById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ExceptionHandler(value = ApartmentNotFoundException.class)
	private ResponseEntity<String> handleApartmentNotFoundException(ApartmentNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}