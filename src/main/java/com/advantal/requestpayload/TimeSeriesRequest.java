package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TimeSeriesRequest {
			
	@NotEmpty(message = "Symbol name can't be empty !!")
	@NotNull(message = "Symbol can't be null !!")
	private String symbol;
	
//	@NotEmpty(message = "Exchange name can't be empty !!")
//	@NotNull(message = "Exchange can't be null !!")
//	private String exchange;
	
//	@NotEmpty(message = "currency name can't be empty !!")
//	@NotNull(message = "Currency can't be null !!")
//	private String currency;

//	@NotEmpty(message = "InstrumentType can't be empty !!")
//	@NotNull(message = "InstrumentType can't be null !!")
//	private String instrumentType;//
	
//	@NotNull(message = "Outputsize can't be null !!")
//	private Integer outputsize;
	
	@NotEmpty(message = "Interval can't be empty !!")
	@NotNull(message = "Interval can't be null !!")
	private String interval;
	
	@NotEmpty(message = "Country name can't be empty !!")
	@NotNull(message = "Country can't be null !!")
	private String country;//new
	
}
