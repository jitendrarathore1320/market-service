package com.advantal.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestPage {
	
	@NotNull(message = "PageIndex can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "PageSize can't be null !!")
	private Integer pageSize;
	
//	@NotEmpty(message = "Country name can't be empty !!")
	@NotNull(message = "Country can't be null !!")
	private String country;
	
//	@NotEmpty(message = "Exchange name can't be empty !!")
	@NotNull(message = "Exchange can't be null !!")
	private String exchange;
	
	@NotNull(message = "keyWord can't be null !!")
	private String keyWord;
	
	@NotNull(message = "UserId can't be null !!")
	private Long userId;
	
	@NotEmpty(message = "SortBy can't be empty !!")
	@NotNull(message = "SortBy can't be null !!")
	private String sortBy;//filterBy;
	
	@JsonProperty("OrderBy")
	@NotNull(message = "Direction can't be null !!")
	private String direction;//sortBy;
	
	@JsonProperty("fileterBySector")
	@NotNull(message = "FiletrBySector can't be null !!")
	private String filterBySector;//fileterBySector;
	
//	@NotNull(message = "fileter By Shariah Compliance can't be null !!")
	private String filterByShariahCompliance;//
			
}
