package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShariyaComplianceRequest {

	@NotEmpty(message = "Country can't be empty!!")
	@NotNull
	private String country;
	
	@NotEmpty(message = "Ticker symbol can't be empty!!")
	@NotNull
	private String tickerSymbol;
//	
//	@NotEmpty(message = "Rating type can't be empty!!")
//	@NotNull
//	private String ratingType;
}
