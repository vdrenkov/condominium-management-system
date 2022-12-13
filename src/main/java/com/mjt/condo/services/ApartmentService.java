package com.mjt.condo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.mjt.condo.exceptions.ApartmentNotFoundException;
import com.mjt.condo.models.Apartment;
import com.mjt.condo.repositories.ApartmentRepository;

@Service
public class ApartmentService {

	@Autowired
	private ApartmentRepository apartmentRepo;

	public List<Apartment> findAllApartments() {
		return apartmentRepo.findAll();
	}

	public Apartment findApartmentById(Long id) throws ApartmentNotFoundException {
		return apartmentRepo.findById(id)
				.orElseThrow(() -> new ApartmentNotFoundException("The apartment you are looking for does not exist."));
	}

	public Apartment saveApartment(Apartment apartment) {
		return apartmentRepo.save(apartment);
	}

	public Apartment updateApartment(Long id, Apartment updatedApartment) throws ApartmentNotFoundException {
		Apartment oldApartment = apartmentRepo.findById(id).orElseThrow(
				() -> new ApartmentNotFoundException("The apartment you are trying to modify does not exist."));

		apartmentRepo.delete(oldApartment);
		apartmentRepo.save(updatedApartment);
		return updatedApartment;
	}

	public void deleteApartmentById(Long id) throws ApartmentNotFoundException {
		try {
			apartmentRepo.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ApartmentNotFoundException("The apartment you are trying to delete does not exist.");
		}
	}
}
