package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExploreResponse {

	// economic
	private String event;

	private String country;

	private Double estimate;
	
	// earning
	private Double eps;

	private Double epsEstimated;

	private String updatedFromDate;

	// dividend
	private Double dividend;

	// IPOS
	private Long shares;

	private Double priceRange;
	
	private String exchange;
	
	// common parameter
	private String symbol;

	private String company;
	
	private String date;

}
