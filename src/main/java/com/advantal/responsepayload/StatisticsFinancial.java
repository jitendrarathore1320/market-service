package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsFinancial {

	private StatisticsFinancialBalanceSheet balance_sheet;
	
	private Float return_on_assets_ttm;
	
	private Float return_on_equity_ttm;
}
