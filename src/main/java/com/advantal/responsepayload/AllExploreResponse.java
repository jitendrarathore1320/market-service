package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllExploreResponse {

	private DividendResponsePage dividendResponsePage;
	
	private EarningResponsePage earningResponsePage;
	
	private EconomicResponsePage economicResponsePage;
	
	private IposResponsePage iposResponsePage;
}
