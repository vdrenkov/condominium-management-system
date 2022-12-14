package com.mjt.condo.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDTO {

	private Long number;
	private int numberOfRooms;
	private double rent;
}