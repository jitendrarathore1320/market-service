package com.advantal.responsepayload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarketState {
	
	@JsonProperty("year")
	private Integer year;	
	
	@JsonProperty("New Years Day")
	private String newYearsDay;
	
	@JsonProperty("Martin Luther King, Jr. Day")
	private String martinLutherKingJrDay;
	
	@JsonProperty("Washington's Birthday")
	private String washingtonBirthday; 
	
	@JsonProperty("Good Friday")
	private String goodFriday;
	
	@JsonProperty("Memorial Day")
	private String memorialDay;
	
	@JsonProperty("Juneteenth National Independence Day")
	private String juneteenthNationalIndependenceDay;
	
	@JsonProperty("Independence Day")
	private String independenceDay;
	
	@JsonProperty("Thanksgiving Day")
	private String thanksgivingDay;
	
	@JsonProperty("Christmas")
	private String christmas;
		
}
