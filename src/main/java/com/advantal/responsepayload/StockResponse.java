package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockResponse {

//	private Long id;instrumentId
		
	private Long instrumentId;
	
	private String logo;
	
	private String symbol;

	private String name;
	
	private String currency;
	
	private String change;

	private String percent_change;
	
	private String close;
	
	private String volume;
	
	private String country;
	
	private String instrumentType;
	
	private String exchange;
	
	private String cryptoId;
	
	private Boolean favorite;
	
	private Double turnover;//
	
	private Long free_float;//
	
	private Float open;//
}
