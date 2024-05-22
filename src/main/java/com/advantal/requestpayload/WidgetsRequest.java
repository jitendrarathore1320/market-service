package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetsRequest {

	@NotNull(message = "PageIndex can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "PageSize can't be null !!")
	private Integer pageSize;

	@NotEmpty(message = "Symbol can't be empty !!")
	@NotNull(message = "Symbol can't be null !!")
	private String symbol;

	@NotEmpty(message = "Exchange can't be empty !!")
	@NotNull(message = "Exchange can't be null !!")
	private String exchange;
}
