package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StockMoversRequest {
	

	@NotEmpty(message = "Output size can't be empty !!")
	private String outputsize;
	
	@NotEmpty(message = "Country name can't be empty !!")
	private String country;
	
	@NotEmpty(message = "Direction name can't be empty !!")
	private String direction;//default: gainers
	
	
	@NotNull(message = "UserId can't be null !!")
	private Long userId;//

//	@NotNull(message = "InstrumentId can't be null !!")
//	private Long instrumentId;
//	
//	private String symbol;//
	
//	@NotEmpty(message = "Instrument type can't be empty !!")
//	private String instrumentType;//
	
				
}
