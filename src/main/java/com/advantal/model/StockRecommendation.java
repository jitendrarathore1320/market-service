package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class StockRecommendation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String symbol;
	
	private String country;
	
	private String exchange;

	private Long buy;
	
	private Long hold;
	
	private Long sell;
		
	private Short rating;
	
	private Short status;
	
	private String reportedDate;
	
	private Date creationDate;

	private Date updationDate;

}
