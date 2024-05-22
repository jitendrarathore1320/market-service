package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsRatio {

//	private StatisticsValuationsMatrics valuations_metrics;
//	
//	private StatisticsStockStatistics stock_statistics;
//	
//	private StatisticsDividends dividends_and_splits;
//	
//	private StatisticsFinancial financials;
	
	private Float priceToBookRatioTTM;//Price/book

	private Double dividendPerShareTTM;//Dividends
	
	private Double dividendYielPercentageTTM;//Dividend Yield(%)(Last Year)

}
