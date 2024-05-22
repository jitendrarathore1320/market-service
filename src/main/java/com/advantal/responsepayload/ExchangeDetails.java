package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeDetails {

	private Long id;

	private String name;

	private String code;

	private String country;

	private String symbol;

	private String high;

	private String low;

	private String volume;

	private String open;

	private String change;

	private String percent_change;

	private FiftyTwoWeek fifty_two_week;

	private String previous_close;

	private String datetime;

}
