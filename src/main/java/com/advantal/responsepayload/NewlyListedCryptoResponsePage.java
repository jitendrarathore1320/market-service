package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import com.advantal.model.Crypto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewlyListedCryptoResponsePage {
	
	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;

	private List<CryptoResponse> cryptoResponseList = new ArrayList<CryptoResponse>();
//	private List<Crypto> cryptoResponseList = new ArrayList<Crypto>();

}

