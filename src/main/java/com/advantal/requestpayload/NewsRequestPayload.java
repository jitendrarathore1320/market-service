package com.advantal.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestPayload {

	@NotNull(message = "Country can't be null !!")
	private String country;

	@NotNull(message = "Page index can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "Page size can't be null !!")
	private Integer pageSize;

	@NotNull(message = "Keyword can't be null !!")
	private String keyword;

}
