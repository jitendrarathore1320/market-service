package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {
    
	@NotNull(message = "Id can't be null")
	private Long id;

	@NotEmpty(message = "Name can't be empty")
	@NotNull(message = "Name can't be null")
	private String name;

	@NotEmpty(message = "country can't be empty")
	@NotNull(message = "country can't be null")	
	private String country;

	@NotEmpty(message = "timezone can't be empty")
	@NotNull(message = "timezone can't be null")
	private String timezone;
	
	@NotNull(message = "Status can't be null")
	private Short status;
}
