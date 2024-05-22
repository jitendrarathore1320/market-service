package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaudiMarketNews {

	private String _STATUS;
	
	private String title;
	
	private String description;
	
	private String link;
	
	private String date;
	
	private String provider;
	
	private String image;
	
	private String symbol;
	
	private String percentage;
}
