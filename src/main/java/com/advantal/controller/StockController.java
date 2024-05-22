package com.advantal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.requestpayload.CountryResquest;
import com.advantal.requestpayload.ExchangeRequest;
import com.advantal.requestpayload.FavoriteInstrumentRequest;
import com.advantal.requestpayload.FavoriteRequestPage;
import com.advantal.requestpayload.HolidayResquest;
import com.advantal.requestpayload.IndicesRequestPayload;
import com.advantal.requestpayload.NewsRequestPayload;
//import com.advantal.requestpayload.ExchangeRequestPage;
import com.advantal.requestpayload.RequestPage;
import com.advantal.requestpayload.SecFilingRequestPage;
import com.advantal.requestpayload.ShariyaComplianceRequest;
import com.advantal.requestpayload.StockMoversRequest;
import com.advantal.requestpayload.StockRequest;
import com.advantal.requestpayload.StockRequestPage;
import com.advantal.requestpayload.TimeSeriesRequest;
import com.advantal.requestpayload.WidgetsRequest;
import com.advantal.service.StockService;
import com.advantal.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/stock")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping(value = "/getExchange")
	public Map<String, Object> getExchange(@RequestParam(required = true) String country) {
		return stockService.getExchange(country);
	}

	@PostMapping(value = "/stocks")
	public ResponseEntity<Map<String, Object>> getStocks(@RequestBody @Valid StockRequestPage stockRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getStocks(stockRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/save_indices")
	public Map<String, Object> saveIndices(@RequestBody @Valid IndicesRequestPayload indicesRequestPayload) {
		return stockService.saveIndices(indicesRequestPayload);
	}

	@PostMapping(value = "/indices")
	public ResponseEntity<Map<String, Object>> getIndices(@RequestBody @Valid RequestPage requestPage) {
		return new ResponseEntity<Map<String, Object>>(stockService.getIndices(requestPage), HttpStatus.OK);
	}

	@PostMapping(value = "/stock")
	public ResponseEntity<Map<String, Object>> getStock(@RequestBody @Valid StockRequest request) {
		return new ResponseEntity<Map<String, Object>>(stockService.getStock(request), HttpStatus.OK);
	}

	@GetMapping(value = "/profile")
	public Map<String, Object> getProfile(@RequestBody @Valid StockRequest request) {
		return stockService.getProfile(request);
	}

	@PostMapping(value = "/movers")
	public ResponseEntity<Map<String, Object>> getMovers(@RequestBody @Valid StockMoversRequest stockMoversRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getMovers(stockMoversRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/time_series")
	public ResponseEntity<Map<String, Object>> getTimeSeries(@RequestBody @Valid TimeSeriesRequest timeSeriesRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getTimeSeries(timeSeriesRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/follow_favorite")
	public ResponseEntity<Map<String, Object>> addFavorite(
			@RequestBody @Valid FavoriteInstrumentRequest favoriteRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.addFavorite(favoriteRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/favorites")
	public ResponseEntity<Map<String, Object>> getFavorites(
			@RequestBody @Valid FavoriteRequestPage favoriteRequestPage) {
		return new ResponseEntity<Map<String, Object>>(stockService.getFavorites(favoriteRequestPage), HttpStatus.OK);
	}

	@PostMapping(value = "/save_crypto_list")
	public Map<String, Object> saveCryptoList(Integer limit, Integer offset) {
		return stockService.saveCryptoList(limit, offset);
	}

	@GetMapping(value = "/symbol_search")
	public ResponseEntity<Map<String, Object>> getSymbolSearch(@RequestParam String symbol,
			@RequestParam String outputsize) {
		return new ResponseEntity<Map<String, Object>>(stockService.getSymbolSearch(symbol, outputsize), HttpStatus.OK);
	}

	@GetMapping(value = "/analyst_rating")
	public ResponseEntity<Map<String, Object>> getAnalystRating(@RequestParam(required = true) String symbol) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!symbol.isBlank()) {
			return new ResponseEntity<Map<String, Object>>(stockService.getAnalystRating(symbol), HttpStatus.OK);
		} else {
			map.put(Constant.MESSAGE, Constant.SYMBOL_NOT_BLANK);
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put("symbol", symbol);
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@GetMapping(value = "/price_target")
	public ResponseEntity<Map<String, Object>> getPriceTarget(@RequestParam(required = true) String symbol,
			@RequestParam(required = true) String country) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!symbol.isBlank()) {
			return new ResponseEntity<Map<String, Object>>(stockService.getPriceTarget(symbol, country), HttpStatus.OK);
		} else {
			map.put(Constant.MESSAGE, Constant.SYMBOL_NOT_BLANK);
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put("symbol", symbol);
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@GetMapping(value = "/institutional_holders")
	public Map<String, Object> getInstitutionalHolders(@RequestParam String symbol, @RequestParam String exchange) {
		return stockService.getInstitutionalHolders(symbol, exchange);
	}

	@PostMapping(value = "/import_csv", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> shariyaCompliance(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam String fileType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (file != null && !file.isEmpty()) {
			return stockService.shariyaCompliance(file, fileType);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping(value = "/indices_series")
	public ResponseEntity<Map<String, Object>> getIndicesSeries(
			@RequestBody @Valid TimeSeriesRequest timeSeriesRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getIndicesSeries(timeSeriesRequest), HttpStatus.OK);
	}

	/* Indices graph data */
	@PostMapping(value = "/indices_graph")
	public ResponseEntity<Map<String, Object>> getIndicesSeriesData(
			@RequestBody @Valid TimeSeriesRequest timeSeriesRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getIndicesSeriesData(timeSeriesRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/key_executive")
	public ResponseEntity<Map<String, Object>> getKeyExecutive(@RequestParam(required = true) String symbol,
			@RequestParam(required = true) String country) {
		return new ResponseEntity<Map<String, Object>>(stockService.getKeyExecutive(symbol, country), HttpStatus.OK);
	}

	@PostMapping(value = "/stock_details_widgets")
	public ResponseEntity<Map<String, Object>> getWidgets(@RequestBody WidgetsRequest widgetsRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getWidgets(widgetsRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/sectors")
	public ResponseEntity<Map<String, Object>> getSectors(@RequestParam String country) {
		return new ResponseEntity<Map<String, Object>>(stockService.getSectors(country), HttpStatus.OK);
	}

	@PostMapping(value = "/get_shariya_compliance")
	public ResponseEntity<Map<String, Object>> getShariyaCompliance(
			@RequestBody @Valid ShariyaComplianceRequest shariyaComplianceRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.getShariyaCompliance(shariyaComplianceRequest),
				HttpStatus.OK);
	}

	// add and update country
	@PostMapping(value = "/country")
	public ResponseEntity<Map<String, Object>> saveCountry(@RequestBody CountryResquest countryResquest) {
		return new ResponseEntity<Map<String, Object>>(stockService.saveCountry(countryResquest), HttpStatus.OK);
	}

	@GetMapping(value = "/getCountry")
	public ResponseEntity<Map<String, Object>> getCountry() {
		return new ResponseEntity<Map<String, Object>>(stockService.getCountry(), HttpStatus.OK);
	}

	// add/update/disable and enable exchange
	@PostMapping(value = "/exchange")
	public ResponseEntity<Map<String, Object>> saveCountry(@RequestBody ExchangeRequest exchangeRequest) {
		return new ResponseEntity<Map<String, Object>>(stockService.saveExchange(exchangeRequest), HttpStatus.OK);
	}

	// get all stocks(all,ksa,usa) count/active/inactive
	@GetMapping(value = "/instrument_count")
	public ResponseEntity<Map<String, Object>> getStockCount(@RequestParam String type) {
		return new ResponseEntity<Map<String, Object>>(stockService.getStockCount(type), HttpStatus.OK);
	}

	// get all stocks(all,ksa,usa) count/active/inactive
	@GetMapping(value = "/instrument_sync_count")
	public ResponseEntity<Map<String, Object>> getStockSyncCount(@RequestParam String type) {
		return new ResponseEntity<Map<String, Object>>(stockService.getStockSyncCount(type), HttpStatus.OK);
	}

	/* get shorting key */
	@GetMapping(value = "/shortingKey")
	public ResponseEntity<Map<String, Object>> getShortingKey() {
		return new ResponseEntity<Map<String, Object>>(stockService.getShortingKey(), HttpStatus.OK);
	}

	/* Get explorer data */
	@PostMapping(value = "/get_explore")
	public ResponseEntity<Map<String, Object>> getExplore(@RequestBody @Valid NewsRequestPayload requestPayload) {
		return new ResponseEntity<Map<String, Object>>(stockService.getExplore(requestPayload), HttpStatus.OK);
	}

	@PostMapping(value = "/holiday")
	public ResponseEntity<Map<String, Object>> holiday(@RequestBody @Valid HolidayResquest holidayResquest) {
		return new ResponseEntity<Map<String, Object>>(stockService.holiday(holidayResquest), HttpStatus.OK);
	}

	@PostMapping(value = "/holidays")
	public ResponseEntity<Map<String, Object>> getHoliday(@RequestBody @Valid RequestPage requestPage) {
		return new ResponseEntity<Map<String, Object>>(stockService.getHoliday(requestPage), HttpStatus.OK);
	}

	@PostMapping(value = "/sec_filings")
	public ResponseEntity<Map<String, Object>> getSecFilings(
			@RequestBody @Valid SecFilingRequestPage secFilingRequestPage) {
		return new ResponseEntity<Map<String, Object>>(stockService.getSecFilings(secFilingRequestPage), HttpStatus.OK);
	}

	@GetMapping(value = "/csv_file_type")
	public ResponseEntity<Map<String, Object>> getFile() {
		return new ResponseEntity<Map<String, Object>>(stockService.getFile(), HttpStatus.OK);
	}
}