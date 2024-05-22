package com.advantal.service;

import java.util.Map;

import javax.validation.Valid;

import com.advantal.requestpayload.NewsRequestPayload;
import com.advantal.requestpayload.StockNewsRequestPayload;

public interface NewsService {

	public Map<String, Object> getAllNews(NewsRequestPayload newsRequestPayloadS);

	public Map<String, Object> getStockNews(@Valid StockNewsRequestPayload stockNewsRequestPayload);

//	public Map<String, Object> getAllCryptoNews(Integer limit, Integer page);

//	public Map<String, Object> getAllCryptoNews(NewsRequestPayload newsRequestPayloadS);

//	public Map<String, Object> getAnnouncementNews(@Valid NewsRequestPayload newsRequestPayloadS);

	
}
