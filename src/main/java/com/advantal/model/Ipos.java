package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ipos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String country;
	
	private String symbol;
	
	private String companyName; 

	private Long shares;
	
	private Double priceRange;
	
	private String exchange;
	
	private String date;

	private Short status;

	private Date creationDate;

	private Date updationDate;
}
