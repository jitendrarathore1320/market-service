package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavoriteRequestPage {
	
	@NotNull(message = "PageIndex can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "PageSize can't be null !!")
	private Integer pageSize;
	
//	@NotEmpty(message = "Country name can't be empty !!")
	@NotNull(message = "country can't be null !!")
	private String country;
	
	@NotEmpty(message = "Instrument type name can't be empty !!")
	@NotNull(message = "instrument_type can't be null !!")
	private String instrument_type;
	
	@NotNull(message = "UserId can't be null !!")
	private Long userId;
	
	@NotNull(message = "KeyWord can't be null !!")
	private String keyWord;
			
}
