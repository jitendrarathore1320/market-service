package com.advantal.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoversResponse {

	private Long instrumentId;

	private String logo;

	private String symbol;

	private String name;

	private String currency;

	private String country;

	private String instrumentType;

	private Boolean favorite;

	private String price;

	private String change;

	private String changesPercentage;

}
