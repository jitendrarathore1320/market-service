package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IposResponse {

	private String symbol;

	private String companyName;

	private Long shares;

	private Double priceRange;

	private String exchange;

	private String date;
}
