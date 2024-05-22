package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsItemsNewlyListed {

	private String name;
	private String symbol;
	private String id;
	private String logo;
	private String listing_time;
	private String price;
	private String price_change_24h;

}
