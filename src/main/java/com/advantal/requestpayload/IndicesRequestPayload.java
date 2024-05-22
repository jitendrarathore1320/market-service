package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicesRequestPayload {

	@NotNull(message = "Id can't be null")
	private Long id;
	
	@NotEmpty(message = "Symbol can't be empty !!")
	@NotNull(message = "Symbol can't be null !!")
	private String symbol;

	@NotEmpty(message = "name can't be empty !!")
	@NotNull(message = "name can't be null !!")
	private String name;

	@NotEmpty(message = "Country name can't be empty !!")
	@NotNull(message = "Country can't be null !!")
	private String country;

	@NotEmpty(message = "Currency name can't be empty !!")
	@NotNull(message = "Currency can't be null !!")
	private String currency;

	@NotEmpty(message = "Exchange name can't be empty !!")
	@NotNull(message = "Exchange can't be null !!")
	private String exchange;
	
	@NotEmpty(message = "FullName name can't be empty !!")
	@NotNull(message = "FullName can't be null !!")
	private String fullName;

	@NotEmpty(message = "Type name can't be empty !!")
	@NotNull(message = "Type can't be null !!")
	private String type;
 
	@NotNull(message = "Status can't be null !!")
	private Short status;
	
}
