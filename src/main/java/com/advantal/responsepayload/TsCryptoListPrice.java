package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsCryptoListPrice {

	String currency;
	
	Double price_latest;
	
	Double price_change_percentage_24h;
	
}
