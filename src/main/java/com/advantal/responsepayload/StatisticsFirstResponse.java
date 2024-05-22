package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsFirstResponse {

	private String symbol;
	
	private String date;
	
	private Double peRatio;

	private Double bookValuePerShare;
	
	private Double enterpriseValue;
	
	private Double returnOnAssets;
	
	private Double returnOnEquity;
	 

}
