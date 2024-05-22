package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsDividends {

	private String dividend_date;
	
	private Float trailing_annual_dividend_yield;
	
}
