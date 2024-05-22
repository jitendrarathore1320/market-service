package com.advantal.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.advantal.model.Crypto;
import com.advantal.model.Exchange;
import com.advantal.model.Indices;
import com.advantal.model.SaudiShariahCompliance;
import com.advantal.model.ShariaCompliance;
import com.advantal.model.Stock;
import com.advantal.requestpayload.CountryResquest;
import com.advantal.requestpayload.ExchangeRequest;
import com.advantal.requestpayload.FavoriteInstrumentRequest;
//import com.advantal.requestpayload.FavoriteRequest;
import com.advantal.requestpayload.FavoriteRequestPage;
import com.advantal.requestpayload.HolidayResquest;
import com.advantal.requestpayload.IndicesRequestPayload;
import com.advantal.requestpayload.NewsRequestPayload;
import com.advantal.requestpayload.PaginationPayLoad;
//import com.advantal.requestpayload.ExchangeRequestPage;
import com.advantal.requestpayload.RequestPage;
import com.advantal.requestpayload.SearchRequestPayload;
import com.advantal.requestpayload.SecFilingRequestPage;
import com.advantal.requestpayload.ShariyaComplianceRequest;
import com.advantal.requestpayload.StockMoversRequest;
import com.advantal.requestpayload.StockRequest;
import com.advantal.requestpayload.StockRequestPage;
import com.advantal.requestpayload.TimeSeriesRequest;
import com.advantal.requestpayload.WidgetsRequest;

public interface StockService {

//	Map<String, Object> saveStockList();

//	Map<String, Object> saveExchangeList();

//	Map<String, Object> getExchanges(RequestPage requestPage);

	Map<String, Object> getProfile(StockRequest request);

	Map<String, Object> getStocks(StockRequestPage stockRequest);

	Map<String, Object> getStock(StockRequest request);

//	Map<String, Object> getProfile(String symbol, String country);

	Map<String, Object> getExchange(String country);

	Map<String, Object> getMovers(StockMoversRequest stockMoversRequest);

	Map<String, Object> getTimeSeries(TimeSeriesRequest timeSeriesRequest);

	Map<String, Object> getIndices(RequestPage requestPage);

//	Map<String, Object> getIndices(IndicesSeriesRequest indicesSeriesRequest);

//	Map<String, Object> addFavorite(FavoriteRequest favoriteRequest);

	Map<String, Object> addFavorite(FavoriteInstrumentRequest favoriteRequest);

	Map<String, Object> getFavorites(FavoriteRequestPage favoriteRequestPage);

	Map<String, Object> saveCryptoList(Integer limit, Integer offset);

	Map<String, Object> getSymbolSearch(String symbol, String outputsize);

	Map<String, Object> getAnalystRating(String symbol);

	Map<String, Object> getPriceTarget(String symbol, String country);

	Map<String, Object> getInstitutionalHolders(String symbol, String exchange);

	Map<String, Object> shariyaCompliance(MultipartFile file, String country);

//	Map<String, Object> shariyaCompliance(MultipartFile file);

	Map<String, Object> getIndicesSeries(TimeSeriesRequest timeSeriesRequest);

	Map<String, Object> getKeyExecutive(String symbol, String country);

	Map<String, Object> getWidgets(WidgetsRequest widgetsRequest);

	Map<String, Object> getSectors(String country);

	Map<String, Object> getShariyaCompliance(ShariyaComplianceRequest shariyaComplianceRequest);

	Map<String, Object> saveIndices(IndicesRequestPayload indicesRequestPayload);

	Map<String, Object> saveCountry(CountryResquest countryResquest);

	Map<String, Object> getCountry();

	Map<String, Object> saveExchange(ExchangeRequest exchangeRequest);

	Map<String, Object> getStockCount(String type);

	Map<String, Object> getStockSyncCount(String type);

	Map<String, Object> getShortingKey();

	Map<String, Object> getExplore(NewsRequestPayload requestPayload);

	Map<String, Object> holiday(HolidayResquest holidayResquest);

	Map<String, Object> getHoliday(RequestPage requestPage);

	Map<String, Object> getSecFilings(SecFilingRequestPage secFilingRequestPage);

	List<Stock> getAllStocksDetails(StockRequestPage stockRequestPage);

	List<Exchange> getAllExchangeDetails(String country);

	List<Indices> getAllIndicesDetails(RequestPage requestPage);

	ShariaCompliance getAllShariyaComplianceDetails(ShariyaComplianceRequest shariyaComplianceRequest);

//	List<ShariaCompliance> getCsvFileDetails(String country);

	Map<String, Object> getCsvFileExcelDetails(String country);

	Map<String, Object> getFile();

	Map<String, Object> getIndicesSeriesData(@Valid TimeSeriesRequest timeSeriesRequest);

//	List<SaudiShariahCompliance> getAlRajhiComplianceCsvFileDetails();

}
