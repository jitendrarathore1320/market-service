package com.advantal.serviceimpl;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.advantal.model.Country;
import com.advantal.model.Indices;
import com.advantal.model.Stock;
import com.advantal.repository.IndicesRepository;
import com.advantal.repository.StockRepository;
import com.advantal.responsepayload.IndicesDetailsResponse;
import com.advantal.responsepayload.MarketStateResponse;
import com.advantal.responsepayload.StockDetails;
//import com.advantal.serviceimpl.DataProcessUtil.GainerTask;
import com.advantal.utils.Constant;
import com.advantal.utils.DateUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarketDataProcessUtil {

	@Autowired
	private ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private Executor executor;

//	@Autowired
//	private CountryRepository countryRepository;

	@Autowired
	private IndicesRepository indicesRepository;
	
	@Autowired
	private NewsDataProcessUtil newsDataProcessUtil;

	public static Long oldDataCount = 0L;

	public void saveData(Page<Stock> page, Country country) {
		try {
//			Long oldDataCount = 0L;
			String apiResponse = "";
			List<Stock> oldList = new ArrayList<>();

			oldList = page.getContent();
			for (Integer i = 0; i < oldList.size(); i++) {
				Stock localStock = oldList.get(i);
				apiResponse = thirdPartyApiUtil.getStock(localStock.getSymbol(), country.getCountry());
				if (!apiResponse.isEmpty()) {
					Type collectionType = new TypeToken<List<StockDetails>>() {
					}.getType();
					List<StockDetails> stockListObj = new Gson().fromJson(apiResponse, collectionType);
					if (!stockListObj.isEmpty()) {
						localStock.setVolume(stockListObj.get(0).getVolume() != null
								? Long.parseLong(stockListObj.get(0).getVolume())
								: 0);
						localStock.setPrice(stockListObj.get(0).getPrice() != null
								? Float.parseFloat(stockListObj.get(0).getPrice())
								: 0);
						localStock.setPercent_change(stockListObj.get(0).getChangesPercentage() != null
								? Float.parseFloat(stockListObj.get(0).getChangesPercentage())
								: 0);
						localStock.setPrice_change(stockListObj.get(0).getChange() != null
								? Float.parseFloat(stockListObj.get(0).getChange())
								: 0);
						localStock.setHigh(stockListObj.get(0).getDayHigh() != null
								? Float.parseFloat(stockListObj.get(0).getDayHigh())
								: 0);
						localStock.setLow(stockListObj.get(0).getDayLow() != null
								? Float.parseFloat(stockListObj.get(0).getDayLow())
								: 0);
						localStock.setFtw_high(stockListObj.get(0).getYearHigh() != null
								? Float.parseFloat(stockListObj.get(0).getYearHigh())
								: 0);
						localStock.setFtw_low(stockListObj.get(0).getYearLow() != null
								? Float.parseFloat(stockListObj.get(0).getYearLow())
								: 0);
						localStock.setMarketCap(stockListObj.get(0).getMarketCap() != null
								? Long.parseLong(stockListObj.get(0).getMarketCap())
								: 0);
						localStock.setVolume(stockListObj.get(0).getVolume() != null
								? Long.parseLong(stockListObj.get(0).getVolume())
								: 0);
						localStock.setAvgVolume(stockListObj.get(0).getAvgVolume() != null
								? Long.parseLong(stockListObj.get(0).getAvgVolume())
								: 0);
						localStock.setOpen(
								stockListObj.get(0).getOpen() != null ? Float.parseFloat(stockListObj.get(0).getOpen())
										: 0);
						localStock.setPreviousClose(stockListObj.get(0).getPreviousClose() != null
								? Float.parseFloat(stockListObj.get(0).getPreviousClose())
								: 0);
						localStock.setEps(
								stockListObj.get(0).getEps() != null ? Float.parseFloat(stockListObj.get(0).getEps())
										: 0);
						localStock.setPe(
								stockListObj.get(0).getPe() != null ? Float.parseFloat(stockListObj.get(0).getPe())
										: 0);
						localStock.setSharesOutstanding(stockListObj.get(0).getSharesOutstanding() != null
								? Long.parseLong(stockListObj.get(0).getSharesOutstanding())
								: 0);
						localStock.setTimestamp(
								stockListObj.get(0).getTimestamp() != null ? stockListObj.get(0).getTimestamp() : 0);

						localStock.setInstrumentType("stock");
						localStock.setLastUpdatedMarketData(new Date());
						localStock = stockRepository.save(localStock);
						oldDataCount++;
						log.info(oldDataCount + ": Stock have updated! | Symbol is - " + localStock.getSymbol()
								+ " | status - {}", Constant.OK);
					} else {
						log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.NOT_EXIST);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
			log.info("Total Data Processed! " + "[ Total Stocks Updated : " + oldDataCount + " ] ! status - {}",
					Constant.OK);
			if (page.isLast()) {
				oldDataCount = 0L;
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	public Boolean getMarketState(String country) {
		Boolean isMarketOpen = false;
		String apiResponse = thirdPartyApiUtil.getMarketState(country);
		if (!apiResponse.isEmpty()) {
			Type collectionType = new TypeToken<List<MarketStateResponse>>() {
			}.getType();
			List<MarketStateResponse> marketStateObj = new Gson().fromJson(apiResponse, collectionType);
			if (!marketStateObj.isEmpty()) {
				isMarketOpen = marketStateObj.get(0).getIsTheStockMarketOpen();
			} else {
				log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}", Constant.NOT_EXIST);
			}
		} else {
			log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
		}
		return isMarketOpen;
	}

	public void saveMarketData(List<Country> countryList) {
		try {
			Long count = stockRepository.count();
			if (count > 0) {
				for (Country country : countryList) {
					if (country != null) {
						String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
						log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
								+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
								+ country.getMarketCloseTime() + " ]");
						Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
						Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
						Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());

//						country.setMarketStatus(getMarketState(country.getCountry()));
//						country.setMarketStatusUpdatedAt(new Date());
//						countryRepository.save(country);
						/* check is market open */
						if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
							getStockListByUpdationDateAndCountryWise(country);
						} else {
							log.info("Waiting... To Open The " + country.getCountry().toUpperCase()
									+ " Market.......... ! status - {}", Constant.NOT_FOUND);
						}
					} else {
						log.error("Country not found : status - {}", Constant.NOT_FOUND);
					}
				}
			}
		} catch (JsonSyntaxException e) {
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	public void retryAfterMarketCloseSaveMarketData(List<Country> countryList) {
		try {
			for (Country country : countryList) {
				log.info(country.getCountry().toUpperCase()
						+ " : Market now Closed ! 01hr, 30min Market EOD data will be updated " + " ! status - {}",
						Constant.OK);
				getStockListByUpdationDateAndCountryWise(country);
			}
		} catch (JsonSyntaxException e) {
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	private class RetryTask implements Runnable {
		private List<Country> list;

		public RetryTask(List<Country> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			newsDataProcessUtil.saveIndicesFromBrowsAi();
			retryAfterMarketCloseSaveIndicesMarketData(list);
			retryAfterMarketCloseSaveMarketData(list);
		}
	}

	public void retryThread(List<Country> countryList) {
		RetryTask retryTask = new RetryTask(countryList);
		Thread thread = new Thread(retryTask);
		thread.start();
	}

	public void retryAfterMarketCloseSaveIndicesMarketData(List<Country> countryList) {
		try {
			String apiResponse = "";
			Integer count = 0;
			for (Country country : countryList) {
				log.info(country.getCountry().toUpperCase()
						+ " : Market now Closed ! 01hr, 30min Market EOD data will be updated " + " ! status - {}",
						Constant.OK);
				apiResponse = thirdPartyApiUtil.indicesList();
				if (!apiResponse.isBlank()) {
					List<IndicesDetailsResponse> indicesDetailsResponseList = null;
					try {
						Type collectionType = new TypeToken<List<IndicesDetailsResponse>>() {
						}.getType();
						indicesDetailsResponseList = new Gson().fromJson(apiResponse, collectionType);
					} catch (Exception e) {
						log.info(e.getMessage() + " status - {}", Constant.SERVER_ERROR);
					}

					if (indicesDetailsResponseList != null && !indicesDetailsResponseList.isEmpty()) {
						List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country.getCountry(),
								Constant.ONE);
						if (!indicesList.isEmpty()) {
							for (Indices indices : indicesList) {
								for (IndicesDetailsResponse indicesDetails : indicesDetailsResponseList) {
									String symbol = indicesDetails.getSymbol();
									if (indicesDetails.getSymbol().contains(".")
											&& indicesDetails.getSymbol().contains("^")) {
										int index = symbol.indexOf(".");
										symbol = symbol.substring(1, index);
										indicesDetails.setSymbol(symbol);
									} else if (indicesDetails.getSymbol().contains("^")) {
										symbol = symbol.substring(1);
										indicesDetails.setSymbol(symbol);
									}

									if (indices.getSymbol().equalsIgnoreCase(indicesDetails.getSymbol())) {
										indices.setClose(indicesDetails.getPrice() != null
												&& indicesDetails.getPrice() != "" ? indicesDetails.getPrice()
														: "0.0");
										indices.setPercent_change(indicesDetails.getChangesPercentage() != null
												&& indicesDetails.getChangesPercentage() != ""
														? indicesDetails.getChangesPercentage()
														: "0.0");
										indices.setPrice_change(indicesDetails.getChange() != null
												&& indicesDetails.getChange() != "" ? indicesDetails.getChange()
														: "0.0");
										indices.setPreviousClose(indicesDetails.getPreviousClose() != null
												&& indicesDetails.getPreviousClose() != ""
														? indicesDetails.getPreviousClose()
														: "0.0");
										indices.setHigh(indicesDetails.getDayHigh() != null
												&& indicesDetails.getDayHigh() != ""
														? indicesDetails.getDayHigh()
														: "0.0");
										indices.setLow(indicesDetails.getDayLow() != null
												&& indicesDetails.getDayLow() != "" ? indicesDetails.getDayLow()
														: "0.0");
										indices.setFtw_high(indicesDetails.getYearHigh() != null
												&& indicesDetails.getYearHigh() != ""
														? indicesDetails.getYearHigh()
														: "0.0");
										indices.setFtw_low(indicesDetails.getYearLow() != null
												&& indicesDetails.getYearLow() != ""
														? indicesDetails.getYearLow()
														: "0.0");
										indices.setVolume(indicesDetails.getVolume() != null
												&& indicesDetails.getVolume() != "" ? indicesDetails.getVolume()
														: "0.0");
										indices.setAvgVolume(indicesDetails.getAvgVolume() != null
												&& indicesDetails.getAvgVolume() != ""
														? indicesDetails.getAvgVolume()
														: "0.0");
										indices.setOpen(indicesDetails.getOpen() != null
												&& indicesDetails.getOpen() != "" ? indicesDetails.getOpen()
														: "0.0");
										indices.setTimestamp(indicesDetails.getTimestamp() != null
												? indicesDetails.getTimestamp()
												: 0);
										indices.setLastUpdatedMarketData(new Date());
																				
										indices = indicesRepository.save(indices);
										count++;
										log.info(count + ": Indices have updated! | Symbol is - " + indices.getSymbol()
												+ " | status - {}", Constant.OK);
									}
								}
							}
							log.info("Total Data Processed! " + "[ Total Indices Updated : " + count
									+ " ] ! status - {}", Constant.OK);
						} else {
							log.info(Constant.RECORD_NOT_FOUND_MESSAGE + "! status - {}", Constant.NOT_FOUND);
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.NOT_EXIST);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
		} catch (JsonSyntaxException e) {
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	private void getStockListByUpdationDateAndCountryWise(Country con) {
		Integer pageIndex = 0;
		final Integer pageSize = 100;
		Page<Stock> page = null;
		Country country = con;
		Calendar cal = Calendar.getInstance();
		Date olderDateObj = null;
		cal.add(Calendar.SECOND, -country.getIntervalForUpdateInstrument());
		olderDateObj = cal.getTime();

		page = stockRepository.findByLastUpdatedMarketDataBeforeAndCountry(olderDateObj, country.getCountry(),
				PageRequest.of(pageIndex, pageSize));
		if (page.getContent().size() > 0) {
			log.info("records for page " + pageIndex + " is " + page.getContent().size());
			while (page.hasNext()) {
				MarketTask task = new MarketTask(page, country);
				executor.execute(task);
				pageIndex = pageIndex + 1;
				page = stockRepository.findByLastUpdatedMarketDataBeforeAndCountry(olderDateObj, country.getCountry(),
						PageRequest.of(pageIndex, pageSize));
				log.info("records for page " + pageIndex + " is " + page.getContent().size());
			}
			MarketTask task = new MarketTask(page, country);
			executor.execute(task);
		}
	}

	private class MarketTask implements Runnable {
		private Page<Stock> page;
		private Country country;

		MarketTask(Page<Stock> page, Country country) {
			super();
			this.page = page;
			this.country = country;
		}

		@Override
		public void run() {
			saveData(page, country);
		}

	}

}