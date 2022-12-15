package com.mjt.condo.models;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "privileges")

public class Privilege {
	@Id
	@Column(unique = true)
	private String name;
}
