package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;
	
	/* global market status */
	private List<KeyValueGlobalResponse> cryptoGloalStatusResponsList;

	private List<CryptoResponse> cryptoResponseList=new ArrayList<CryptoResponse>();

}
