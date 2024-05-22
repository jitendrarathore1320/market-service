package com.advantal.requestpayload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationPayLoad {
	
	@NotNull(message = "Page index can't be null !!")
	private Integer pageIndex;

	@NotNull(message = "Page size can't be null !!")
	private Integer pageSize;
	
	@NotNull(message = "User id can't be null !!")
	private Long userId;
	
	@NotNull(message = "keyWord can't be null !!")
	private String keyWord;
	
}
