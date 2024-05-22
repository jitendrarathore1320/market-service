package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DividendResponse {

	private String symbol;

	private String companyName;

	private Double dividendAmount;

	private String date;
}
