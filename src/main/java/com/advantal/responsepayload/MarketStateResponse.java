package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarketStateResponse {
	
	private Boolean isTheStockMarketOpen;
	
	@JsonProperty("stockMarketHolidays")
	private List<MarketState> stockMarketHolidays;
		
}
