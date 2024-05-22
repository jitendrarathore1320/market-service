package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoversDetails {
	
	private Long instrumentId;
	
	private String logo;

	private String symbol;
	
	private String name;
	
	private String currency;
		
	private String country;
	
	private String instrumentType;
	
	private Boolean favorite;

	private String last;
	
	private String change;
	
	private String percent_change;
	
	private String exchange;
	
}
