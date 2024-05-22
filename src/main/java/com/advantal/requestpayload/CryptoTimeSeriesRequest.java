package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTimeSeriesRequest {
	@NotEmpty(message = "Symbol name can't be empty !!")
	private String symbol;
//	
	@NotEmpty(message = "currency name can't be empty !!")
	private String currency;
	
	@NotNull(message = "Output size can't be null !!")
	private Integer outputsize;
	
	@NotEmpty(message = "Interval can't be empty !!")
	private String interval;
	
//	@NotEmpty(message = "Start date can't be empty !!")
	private String start_date;
	
//	@NotEmpty(message = "End date can't be empty !!")
	private String end_date;

}
