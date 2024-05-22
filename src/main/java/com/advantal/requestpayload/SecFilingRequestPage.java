package com.advantal.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SecFilingRequestPage {
	
	@NotNull(message = "PageIndex can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "PageSize can't be null !!")
	private Integer pageSize;
	
	@NotNull(message = "Country can't be null !!")
	private String country;
		
	@NotNull(message = "keyWord can't be null !!")
	private String keyWord;
	
	@NotNull(message = "Type can't be null !!")
	private String type;
	
}
