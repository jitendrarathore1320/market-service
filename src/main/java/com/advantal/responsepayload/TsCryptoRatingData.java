package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoRatingData {
	String rating_level;
	Long review_time;
	String team_partners_investors;
	String rating_score;
	String token_economics;
	String underlying_technology_security;
	String roadmap_progress;
	String token_performance;
	String rating_page;	
	String ecosystem_development;
	Long update_time;
}
