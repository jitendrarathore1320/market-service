package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaudiProfileResponse {
	
	private String symbol;
	
	private String name;
	
	private String sector;
	
	private String index;
	
	private String exchange;

}
