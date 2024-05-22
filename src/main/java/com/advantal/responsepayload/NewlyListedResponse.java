package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewlyListedResponse {
	
	private Long instrumentId;
	private String name;
	private String symbol;
	private String logo;
	private String cryptoId;
	private String currency;
	private String percent_change;
	private String change_value;
	private String instrumentType;
	private String price;
	private String url;
	private Boolean favorite;
}
