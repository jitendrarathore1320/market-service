package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryResquest {

	@NotNull(message = "Id can't be null")
	private Long id;

	@NotEmpty(message = "Country can't be empty")
	@NotNull(message = "Country can't be null")
	private String country;

	@NotEmpty(message = "MarketOpenTime can't be empty")
	@NotNull(message = "MarketOpenTime can't be null")
	private String marketOpenTime;

	@NotEmpty(message = "MarketCloseTime can't be empty")
	@NotNull(message = "MarketCloseTime can't be null")
	private String marketCloseTime;

	@NotEmpty(message = "TimeZone can't be empty")
	@NotNull(message = "TimeZone can't be null")
	private String timeZone;

	@NotEmpty(message = "Exchange can't be empty")
	@NotNull(message = "Exchange can't be null")
	private String exchange;
	
	@NotNull(message = "IntervalForUpdateInstrument can't be null")
	private Integer intervalForUpdateInstrument;
	
	@NotNull(message = "Status can't be null !!")
	private Short status;
}
