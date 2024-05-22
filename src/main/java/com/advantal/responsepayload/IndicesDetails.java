package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IndicesDetails {

//	private Long id;

	private String symbol;

	private String name;

	private String country;

	private String currency;

	private String exchange;
	
	private String close;
	
	private String datetime;
	
	private String change;

	private String percent_change;
		
	private String type;
	
	private List<KeyValueResponse> keyValueResponseList; // using for mobile
	
	private Long timestamp;
	
	private String fullName;
	
	
//	private String high;
//	
//	private String low;

}
