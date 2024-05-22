package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalystRecommendation {

	private Long analystRatingsbuy;
	
	private Long analystRatingsHold;
	
	private Long analystRatingsSell;
	
	private Short ratingScore;
	
	private String date;
}
