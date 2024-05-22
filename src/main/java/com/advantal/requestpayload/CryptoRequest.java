package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CryptoRequest {
			
	@NotEmpty(message = "Crypto id can't be empty !!")
	private String cryptoId;
	
	@NotEmpty(message = "Currency can't be empty !!")
	private String currency;
	
	@NotNull(message = "User id can't be null !!")
	private Long userId;
	
	@NotNull(message = "Instrument id can't be null !!")
	private Long instrumentId;
	
//
//	//new implement
//	@NotNull(message = "User id can't be null !!")
//	private String cryptoType;
	
	@NotNull(message = "Portfolio id can't be null !!")
	private Long portfolioId;//new
}
