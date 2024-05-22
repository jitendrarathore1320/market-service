package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningResponse {

	private String symbol;
	
	private String companyName;
	
	private Double eps;
	
	private Double epsEstimated;
	
	private String date;
	
}
