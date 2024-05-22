package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDetailsResponse {

	private String symbol;

	private String name;

	private String currency;

	private String exchange;

	private String close;

	private String change;

	private String percent_change;

	private String datetime;

	private Long timestamp;

	private String mktOpnClsDtTm;

	private String logo;

	private Boolean favorite;

	private Long instrumentId;
	
	private Boolean is_market_open;
	
	private String volume;
	
	private String ftw_high;//ftwh

	private String ftw_low;//ftwl
	
	private String about;//
	
	private List<KeyValueResponse> keyValueMarketDataList; // using for mobile
	
	private List<KeyValueResponse> keyValueDetailsList; // using for mobile
	
	private Boolean isCompliance=false;//new
	
	private String weekends;
	
	private Float open;//
	
	private List<KeyValueResponse> keyValueAdvancMatrixList; // using for mobile
	
}
