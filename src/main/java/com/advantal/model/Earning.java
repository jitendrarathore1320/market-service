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
public class Earning {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	private String symbol;
	
	private String country;
	
	private String companyName;
	
	private Double eps;
	
	private Double epsEstimated;
	
	private String date;
	
	private String updatedFromDate;
	
	private Short status;
	
	private Date creationDate;
	
	private Date updationDate;

}
