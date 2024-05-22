package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockNewsRequestPayload {

	@NotNull(message = "Page index can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "Page size can't be null !!")
	private Integer pageSize;

	@NotNull
	@NotEmpty(message = "Country can't be Empty or null !!")
	private String country;
	
	@NotNull
	@NotEmpty(message = "Symbol can't be Empty or null !!")
	private String symbol;
}
