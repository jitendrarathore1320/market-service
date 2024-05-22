package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsValuationsMatrics {

	private Long market_capitalization;
	
	private Float trailing_pe;
	
	private Float forward_pe;
	
	private Float price_to_sales_ttm;//
	
	private Float price_to_book_mrq;//
	
	
	private Long enterprise_value;
	
}
