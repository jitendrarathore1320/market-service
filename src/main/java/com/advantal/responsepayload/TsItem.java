package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsItem {

	String name;
	String symbol;
	String id;
	String logo;
	Double price;
	Double price_change_percentage_24h;
}
