package com.advantal.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class ExchangeResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;
	
	private List<ExchangeDetails> exchangeDetailsResponseList;
	
//	private List<ExchangeResponse> exchangeDetailsResponseList;
	
}
