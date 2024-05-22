package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeSeriesMeta {

	private String symbol;
	
	private String interval;
	
	private String currency;
	
	private String exchange;

}
