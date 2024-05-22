package com.advantal.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestPayload {
	
	@NotNull(message = "PageIndex can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "PageSize can't be null !!")
	private Integer pageSize;
	
	private String keyWord;
			
}
