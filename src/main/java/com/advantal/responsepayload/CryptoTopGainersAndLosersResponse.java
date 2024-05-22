package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTopGainersAndLosersResponse {

//	String id;
	private Long instrumentId;
	private String cryptoId;
	private String name;
	private String symbol;
	private String price_change_24h;
	private String logo;
	private String price;
	private String url;
	
	private Boolean favorite;
	private String instrumentType;
}
