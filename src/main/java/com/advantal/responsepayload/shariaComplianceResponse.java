package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class shariaComplianceResponse {

	private Short complainceDegree;

	private Float revenueBreakdownHalal;

	private Float revenueBreakdownDoubtful;

	private Float revenueBreakdownNotHalal;
	
	private String revenueBreakdownResult;
	
	private String interestBearingDebtResult;
	
	private String interestBearingDebtPercentage;
	
	private String interestBearingSecuritiesAndAssetsResult;
	
	private String interestBearingSecuritiesAndAssetsPercentage;
	
	private String lastUpdated;

	private String source;
	
	private String AaoifiComplianceFlag;
	
	private String AlRajhiComplianceFlag;
	
	private String saudiLastUpdated;
	
	private String saudiSource;

	
}
