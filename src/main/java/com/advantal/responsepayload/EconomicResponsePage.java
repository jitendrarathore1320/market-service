package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EconomicResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;
	
	private List<EconomicResponse> economicResponseList;
}
