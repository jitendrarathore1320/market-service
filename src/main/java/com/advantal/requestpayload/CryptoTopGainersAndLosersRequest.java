package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTopGainersAndLosersRequest {

	@NotEmpty(message = "Top gainers or losers can't be empty")
	private String top;
	
	@NotNull(message = "Range can't be null")
	private Integer range;
	
	@NotNull(message = "User id can't be null !!")
	private Long userId;
	
}
