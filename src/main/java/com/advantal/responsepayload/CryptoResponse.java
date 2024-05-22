package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoResponse {
	
//	private Long id;//instrumentId
	
	private Long instrumentId;
	
	private String name;
	private String symbol;
	private String logo;
	private String cryptoId;
	private String currency;
//	private Double percent_change;
	private String percent_change;
//	private Double change_value;
	private String change_value;
	private String instrumentType;
//	private Double price;
	private String price;
	private Boolean favorite;
}
