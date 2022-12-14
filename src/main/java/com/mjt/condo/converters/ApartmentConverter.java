package com.mjt.condo.converters;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mjt.condo.models.Apartment;
import com.mjt.condo.models.ApartmentDTO;

@Component
public class ApartmentConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public Apartment ApartmentDTOToApartment(ApartmentDTO dto)
	{
		return modelMapper.map(dto, Apartment.class);
	}
	
	public ApartmentDTO ApartmentToApartmentDTO(Apartment apartment)
	{
		return modelMapper.map(apartment, ApartmentDTO.class);
	}
}
