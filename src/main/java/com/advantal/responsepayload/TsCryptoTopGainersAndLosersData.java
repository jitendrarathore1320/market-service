package com.advantal.responsepayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoTopGainersAndLosersData {

	String id;
	String name;
	String symbol;
	Double price_change_24h;
	String logo;
	Double price;
	String url;
}
