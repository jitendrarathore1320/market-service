package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapturedTexts {

	private String symbol;
	
	private String price;
	
	private String value_percentage_change;
}
