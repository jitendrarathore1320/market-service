package com.advantal.responsepayload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaudiNewsResponse implements Serializable {
	/**
	 * MarketAux
	 */
//	private static final long serialVersionUID = 1L;
//	
//	MA_NewsResponseMetaData meta;
//	
//	List<NewsResponseData> data=new ArrayList<NewsResponseData>();

	/**
	 * browse AI
	 */
	private String statusCode;

	private String messageCode;

	private SARobotsResult result;
}
