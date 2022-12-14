package com.mjt.condo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.mjt.condo.converters.ApartmentConverter;
import com.mjt.condo.exceptions.ApartmentNotFoundException;
import com.mjt.condo.models.Apartment;
import com.mjt.condo.models.ApartmentDTO;
import com.mjt.condo.repositories.ApartmentRepository;

@Service
public class ApartmentService {

	@Autowired
	private ApartmentRepository apartmentRepo;

	@Autowired
	private ApartmentConverter converter;

	public Long apartmentCount(Long id) {
		long counter = 0;
		List<Apartment> apartments = findAllApartments();

		for (Apartment apartment : apartments) {
			if (apartment.getTenant_id() == id)
				counter++;
		}
		return counter;
	}

	public List<Apartment> showAllApartments(Long id) {
		List<Apartment> source = findAllApartments();
		List<Apartment> target = new ArrayList<Apartment>();

		for (Apartment apartment : source) {
			if (apartment.getTenant_id() == id)
				target.add(apartment);
		}

		return target;
	}

	public List<Apartment> findAllApartments() {
		return apartmentRepo.findAll();
	}

	public List<ApartmentDTO> findAllApartmentDTOs() {
		List<ApartmentDTO> target = new ArrayList<ApartmentDTO>();
		List<Apartment> apartments = apartmentRepo.findAll();

		for (Apartment apartment : apartments) {
			target.add(converter.ApartmentToApartmentDTO(apartment));
		}

		return target;
	}

	public Apartment findApartmentById(Long id) throws ApartmentNotFoundException {
		return apartmentRepo.findById(id)
				.orElseThrow(() -> new ApartmentNotFoundException("The apartment you are looking for does not exist."));
	}

	public ApartmentDTO findApartmentDTOById(Long id) throws ApartmentNotFoundException {
		return converter.ApartmentToApartmentDTO(apartmentRepo.findById(id).orElseThrow(
				() -> new ApartmentNotFoundException("The apartment you are looking for does not exist.")));
	}

	public Apartment saveApartment(Apartment apartment) throws ApartmentNotFoundException {
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
