package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockMaster {

//	private List<Stock> data;
	
	private String symbol;

	private String name;
	
	private String exchangeShortName;

	private String exchange;
	
	private String sector;

	private String index;
	
	private String type;
	
}
