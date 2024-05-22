package com.advantal.responsepayload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvSaudiExportFileRespnse {
	private Long id;

	private String tickerNames;

	private String tickerSymbol;

	private String complianceStatus;

	private Short complainceDegree;

	private Float revenueBreakdownHalal;

	private Float revenueBreakdownDoubtful;

	private Float revenueBreakdownNotHalal;

//	private String interestBearingSecuritiesAndAssets;//usa
	
	private Float interestBearingSecuritiesAndAssetsPercentage;//saudi
	
	private String interestBearingSecuritiesAndAssetsStatus;//saudi
	
//	private String interestBearingDebt;//usa

	private String interestBearingDebtStatus;//saudi
	
	private Float interestBearingDebtPercentage;//saudi

	private String lastUpdated;

	private String source;

	private String url;

	private String country;
	
	private Date creationDate;

}
