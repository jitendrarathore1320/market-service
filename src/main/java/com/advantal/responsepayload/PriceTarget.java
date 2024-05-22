package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceTarget {
	private Double high;
	private Double median;
	private Double low;
	private Double average;
	private Double current;
	private String currency;

}
