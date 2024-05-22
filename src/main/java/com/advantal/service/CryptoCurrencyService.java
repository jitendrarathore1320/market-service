package com.advantal.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.advantal.model.Crypto;
import com.advantal.requestpayload.CryptoRequest;
import com.advantal.requestpayload.CryptoTopGainersAndLosersRequest;
import com.advantal.requestpayload.PaginationPayLoad;

@Service
public interface CryptoCurrencyService {

	Map<String, Object> getCryptoList(PaginationPayLoad paginationPayLoad);

	void saveCryptoList(Integer limit, Integer offset);

//	Map<String, Object> getCryptoDetails(String cryptoId,String currency);

	Map<String, Object> getNewsList(String id);

	Map<String, Object> getCryptoDetails(CryptoRequest cryptoRequest);

	Map<String, Object> getTopGainersAndLosers(CryptoTopGainersAndLosersRequest gainersAndLosersRequest);

	Map<String, Object> getNewlyListed(PaginationPayLoad paginationPayLoad);

//	Map<String, Object> saveCryptoExchangeList();

	Map<String, Object> getTradingPair(String cryptoId, String exchangeName);

	Map<String, Object> getCryptoExchangeList();

	List<Crypto> getAllCryptoDetails(@Valid PaginationPayLoad paginationPayLoad);

	List<Crypto> getAllNewlyListedCryptoDetails(@Valid PaginationPayLoad paginationPayLoad);

	Map<String, Object> getCryptoGraphData(String cryptoId, String type);

}
