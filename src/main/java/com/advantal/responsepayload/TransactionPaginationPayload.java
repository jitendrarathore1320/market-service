package com.advantal.responsepayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPaginationPayload {

	@NotNull(message = "Page index can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "Page size can't be null !!")
	private Integer pageSize;

	@NotEmpty(message = "Crypto or stock symbol can't be empty !!")
	@NotNull
	private String crypto_or_stock_symbol;

}
