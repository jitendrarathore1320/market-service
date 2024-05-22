package com.advantal.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.requestpayload.CryptoRequest;
import com.advantal.requestpayload.CryptoTopGainersAndLosersRequest;
import com.advantal.requestpayload.PaginationPayLoad;
import com.advantal.service.CryptoCurrencyService;

@RestController
@RequestMapping("/api/crypto")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CryptoCurrencyController {

	@Autowired
	private CryptoCurrencyService cryptoCurrencyService;

	@PostMapping("/get_crypto_list")
	public ResponseEntity<Map<String, Object>> getCryptoList(@RequestBody @Valid PaginationPayLoad paginationPayLoad) {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getCryptoList(paginationPayLoad),
				HttpStatus.OK);
	}

	/* save crypto list only backed purpose data save in database */
	@GetMapping("/save_crypto_list")
	public void saveCryptoList(@RequestParam Integer limit, @RequestParam Integer offset) {
		cryptoCurrencyService.saveCryptoList(limit, offset);
	}

	/* get crypto Details */
	@PostMapping("/get_crypto_profile")
	public ResponseEntity<Map<String, Object>> getCryptoDetails(@RequestBody @Valid CryptoRequest cryptoRequest) {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getCryptoDetails(cryptoRequest),
				HttpStatus.OK);
	}

	@GetMapping("/news_list")
	public ResponseEntity<Map<String, Object>> getNewsList(
			@RequestParam(required = true, name = "cryptoId") String cryptoId) {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getNewsList(cryptoId), HttpStatus.OK);
	}

	/* Crypto Top gainers & losers */
	@PostMapping("/get_top_gainers_losers")
	public ResponseEntity<Map<String, Object>> getTopGainersAndLosers(
			@RequestBody @Valid CryptoTopGainersAndLosersRequest gainersAndLosersRequest) {
		return new ResponseEntity<Map<String, Object>>(
				cryptoCurrencyService.getTopGainersAndLosers(gainersAndLosersRequest), HttpStatus.OK);
	}

	/* newly listed */
	@PostMapping("/get_newly_listed")
	public ResponseEntity<Map<String, Object>> getNewlyListed(@RequestBody @Valid PaginationPayLoad paginationPayLoad) {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getNewlyListed(paginationPayLoad),
				HttpStatus.OK);
	}

	/* cryptoExchange List */
//	@GetMapping("/save_crypto_exchange_list")
//	public ResponseEntity<Map<String, Object>> saveCryptoExchangeList() {
//		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.saveCryptoExchangeList(), HttpStatus.OK);
//	}

	/* get cryptoExchange List */
	@GetMapping("/get_crypto_exchange_list")
	public ResponseEntity<Map<String, Object>> getCryptoExchangeList() {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getCryptoExchangeList(), HttpStatus.OK);
	}

	/* get crypto chart data */
	@GetMapping("/get_crypto_graph")
	public ResponseEntity<Map<String, Object>> getCryptoGraphData(@RequestParam(required = true) String cryptoId,
			@RequestParam(required = true) String type) {
		return new ResponseEntity<Map<String, Object>>(cryptoCurrencyService.getCryptoGraphData(cryptoId, type),
				HttpStatus.OK);
	}
}