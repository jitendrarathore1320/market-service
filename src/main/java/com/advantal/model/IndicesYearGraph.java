package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicesYearGraph {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String symbol;

	private String exchange;

	private String country;
	
	private String open;

	private String high;

	private String low;

	private String close;
	
	private String date;
	
	private Date creationDate;
	
//	private Date updationDate;

}
