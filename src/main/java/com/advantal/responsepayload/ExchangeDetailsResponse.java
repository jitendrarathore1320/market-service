package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeDetailsResponse {

	private String symbol;
	
	private String name;
	
	private String high;

	private String low;
	
	private String volume;
	
	private String open;
	
	private String change;

	private String percent_change;
	
	private FiftyTwoWeek fifty_two_week;
		
	private String previous_close;
	
	private String datetime;
	

}
