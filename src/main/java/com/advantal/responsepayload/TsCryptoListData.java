package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsCryptoListData {

	String id;
	List<TsCryptoListPrice> price;
//	List<TsItem> items;
}
