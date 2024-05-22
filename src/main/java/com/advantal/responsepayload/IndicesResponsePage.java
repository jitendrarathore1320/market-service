package com.advantal.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class IndicesResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;
	
	private List<IndicesDetails> indicesDetailsResponseList;
	
	private List<IndicesDetails> indicesList;
	
//	private List<ExchangeResponse> exchangeDetailsResponseList;
	
}
