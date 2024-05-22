package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchData {

	String symbol;
	String instrument_name;
	String country;
	String logo;
	String instrument_type;
}
