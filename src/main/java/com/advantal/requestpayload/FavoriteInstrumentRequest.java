package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavoriteInstrumentRequest {

//	@NotNull(message = "Id can't be null !!")
////	private Long id;

	@NotNull(message = "UserId can't be null !!")
	private Long userId;//

	@NotNull(message = "InstrumentId can't be null !!")
	private Long instrumentId;

//	@NotEmpty(message = "Symbol can't be empty !!")
	private String symbol;//

//	@NotEmpty(message = "Name can't be empty !!")
//	private String name;

	private String cryptoId;//
	
//	@NotEmpty(message = "Currency can't be empty !!")
//	private String currency;

//	@NotEmpty(message = "Exchange can't be empty !!")
//	private String exchange;

	@NotEmpty(message = "Instrument type can't be empty !!")
	private String instrumentType;//

//	@NotEmpty(message = "Country name can't be empty !!")
	@NotNull(message = "country can't be null !!")
	private String country;

//	@NotEmpty(message = "Logo can't be empty !!")
//	private String logo;

	@NotNull(message = "Status can't be null !!")
	private Short status;

}
