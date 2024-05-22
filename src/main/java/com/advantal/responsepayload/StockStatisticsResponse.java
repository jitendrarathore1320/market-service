package com.advantal.responsepayload;

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
public class StockStatisticsResponse {

	private String symbol;

	private Long market_capitalization;

	private Float trailing_pe;

	private Float forward_pe;

	private Float price_to_sales_ttm;

	private Float price_to_book_mrq;

	private Long enterprise_value;

	private String shares_outstanding;

	private String dividend_date;

	private Float trailing_annual_dividend_yield;

	private Float return_on_assets_ttm;

	private Float return_on_equity_ttm;

	private Double book_value_per_share_mrq;

}
