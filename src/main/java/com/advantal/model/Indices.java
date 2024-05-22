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
public class Indices {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String symbol;

	private String name;

	private String country;

	private String currency;

	private String exchange;

	private String fullName;

	private String type;

	private Short status;
	
	private String close;//
	
	private String percent_change;
	
	private String price_change;//
	
	private String previousClose;
	
	private String high;
	
	private String low;
	
	private String ftw_high;

	private String ftw_low;
	
	private String volume;
	
	private String avgVolume;
	
	private String open;
	
	private Long timestamp;

	private Date creationDate;

	private Date updationDate;

	private Date lastUpdatedMarketData;

}
