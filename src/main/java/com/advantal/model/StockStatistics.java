package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class StockStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String symbol;
	
	private String country;
	
	private String exchange;
	
	private Double peRatioTTM;//PE(TTM)

	private Double bookValuePerShareTTM;//BVPS
	
	private Double enterpriseValueTTM;//Enterprise Value(EV)(M)
			
	private Float priceToBookRatioTTM;//Price/book
	
	private Double dividendYielPercentageTTM;//Dividend Yield(%)(Last Year)
	
	private Double dividendPerShareTTM;//Dividends
	
	private Double returnOnAssetsTTM;//Return on average assets(%)(TTM)
	
	private Double returnOnEquityTTM;//Return on average Equity(%)(TTM)
	
	private String metrics_endpoint_date;//quarterly date
	
	private String ratio_endpoint_date;//quarterly date

	private Short status;
	
	private Date creationDate;

	private Date updationDate;
	
	
	
	
	
	/* valuations_metrics */
//	private Long market_capitalization;
//	private Float trailing_pe;
//	private Float forward_pe;
//	private Float price_to_sales_ttm;
//	private Float price_to_book_mrq;
//	private Long enterprise_value;

	/* stock_statistics */
//	private String shares_outstanding;

	/* dividends_and_splits */
//	private String dividend_date;
//	private Float trailing_annual_dividend_yield;

	/* financials */
//	private Float return_on_assets_ttm;
//	private Float return_on_equity_ttm;
	
	/* balance_sheet */
//	private Double book_value_per_share_mrq;
	


}
