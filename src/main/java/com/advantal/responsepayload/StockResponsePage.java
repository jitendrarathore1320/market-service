package com.advantal.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class StockResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;

//	private StockListResponse stockListResponse;
	
	private List<StockResponse> stockDetailsResponseList;
	
}
