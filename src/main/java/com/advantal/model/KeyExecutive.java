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
public class KeyExecutive {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String symbol;

	private String country;

	private String exchange;

	private String name;

	private String title;
	
//	private Long pay;

	private Date creationDate;

	private Date updationDate;
	
//	private String currencyPay;//

	private String gender;//

//	private Integer year_born;

	private Integer yearBorn;

//	private Integer age;

	private Long titleSince;//

}
