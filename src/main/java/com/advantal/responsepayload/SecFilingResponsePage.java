package com.advantal.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class SecFilingResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;
	
	private List<SecFilingRes> secFilingResList;
	
}
