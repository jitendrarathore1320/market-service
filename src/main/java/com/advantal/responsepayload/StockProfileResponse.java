package com.advantal.responsepayload;

import java.util.Date;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockProfileResponse {
	
	private String symbol;

	private String companyName;
	
	private String isin;
	
	private String cusip;
	
	private String exchange;
	
	private String exchangeShortName;//
	
	private String industry;
	
	private String website;
	
	private String description;
	
	private String ceo;
	
	private String sector;
	
	private String country;

	private String fullTimeEmployees;//

	private String phone;

	private String address;

	private String city;

	private String state;

	private String zip;
	
	private String image;//
	
	private Boolean isActivelyTrading;
	
	private String ipoDate;//

	private Date creationDate;
	
	private Date updationDate;

}
