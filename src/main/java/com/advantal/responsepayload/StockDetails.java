package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDetails {

	private String symbol;

	private String name;
//
//	private String currency;
//
//	private String exchange;

	private String price;

	private String changesPercentage;

	private String change;

	private String dayLow;

	private String dayHigh;

	private String yearHigh;

	private String yearLow;

	private String marketCap;

	private String volume;

	private String avgVolume;

	private String open;

	private String previousClose;

	private String eps;

	private String pe;

	private String sharesOutstanding;

	private Long timestamp;
	
	private Double turnover;//
	
	private Long free_float;//

}
