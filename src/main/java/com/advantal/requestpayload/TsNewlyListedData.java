package com.advantal.requestpayload;

import java.util.List;

import com.advantal.responsepayload.PageInfo;
import com.advantal.responsepayload.TsItemsNewlyListed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsNewlyListedData {

	private List<TsItemsNewlyListed> items;
	
	private PageInfo page_info;
}
