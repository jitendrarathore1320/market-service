package com.advantal.responsepayload;

import com.advantal.requestpayload.TsNewlyListedData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewlyListedItemsResponse {

	private TsNewlyListedData data;
}
