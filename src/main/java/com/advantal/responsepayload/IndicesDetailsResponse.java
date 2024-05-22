package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndicesDetailsResponse {

	private String symbol;
	
	private String name;

	private String price;
	
	private String changesPercentage;
	
	private String change;
	
	private String dayLow;

	private String dayHigh;
	
	private String yearHigh;

	private String yearLow;
	
	private String volume;
	
	private String avgVolume;
	
    private String open;
	
	private String previousClose;
	
	private Long timestamp;
	

}
