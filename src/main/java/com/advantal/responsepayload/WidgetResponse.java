package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetResponse {

	private AnalystResponse analystResponse;

	private PriceTarget priceTargetResponse;

	private ShareHolderDataResponse shareHolderDataResponse;

}
	