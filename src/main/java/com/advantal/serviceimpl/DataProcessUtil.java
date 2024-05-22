package com.advantal.serviceimpl;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

//import com.advantal.backgroundtask.MoverTask;
import com.advantal.model.Country;
import com.advantal.model.DayGraph;
import com.advantal.model.Dividend;
import com.advantal.model.Earning;
import com.advantal.model.Economic;
import com.advantal.model.Exchange;
import com.advantal.model.Indices;
import com.advantal.model.IndicesDayGraph;
import com.advantal.model.IndicesMinuteGraph;
import com.advantal.model.IndicesYearGraph;
import com.advantal.model.Ipos;
import com.advantal.model.KeyExecutive;
import com.advantal.model.MinuteGraph;
import com.advantal.model.SecFiling;
import com.advantal.model.ShareHolder;
import com.advantal.model.Stock;
import com.advantal.model.StockGainer;
import com.advantal.model.StockLoser;
import com.advantal.model.StockPriceTarget;
import com.advantal.model.StockProfile;
import com.advantal.model.StockRecommendation;
import com.advantal.model.StockStatistics;
import com.advantal.model.YearGraph;
import com.advantal.repository.CountryRepository;
import com.advantal.repository.DayGraphRepository;
import com.advantal.repository.DividendRepository;
import com.advantal.repository.EarningRepository;
import com.advantal.repository.EconomicRepository;
import com.advantal.repository.ExchangeRepository;
import com.advantal.repository.HolidayRepository;
import com.advantal.repository.IndicesDayGraphRepository;
import com.advantal.repository.IndicesMinuteGraphRepository;
import com.advantal.repository.IndicesRepository;
import com.advantal.repository.IndicesYearGraphRepository;
import com.advantal.repository.IposRepository;
import com.advantal.repository.KeyExecutiveRepository;
import com.advantal.repository.MinuteGraphRepository;
import com.advantal.repository.SecFilingRepository;
import com.advantal.repository.ShareHolderRepository;
import com.advantal.repository.StockGainerRepository;
import com.advantal.repository.StockLoserRepository;
import com.advantal.repository.StockPriceTargetRepository;
import com.advantal.repository.StockProfileRepository;
import com.advantal.repository.StockRecommendationRepository;
import com.advantal.repository.StockRepository;
import com.advantal.repository.StockStatisticsRepository;
import com.advantal.repository.YearGraphRepository;
import com.advantal.responsepayload.AnalystRecommendation;
import com.advantal.responsepayload.ExploreResponse;
import com.advantal.responsepayload.IndicesDetailsResponse;
import com.advantal.responsepayload.InstitutionalHolders;
import com.advantal.responsepayload.KeyExecutiveRes;
import com.advantal.responsepayload.MoversResponse;
import com.advantal.responsepayload.PriceTargets;
import com.advantal.responsepayload.SaudiProfileResponse;
import com.advantal.responsepayload.SecFilingResponse;
//import com.advantal.responsepayload.SecFilingResponse;
import com.advantal.responsepayload.StatisticsFirstResponse;
import com.advantal.responsepayload.StatisticsRatio;
import com.advantal.responsepayload.StatisticsResponse;
import com.advantal.responsepayload.StockMaster;
import com.advantal.responsepayload.StockProfileResponse;
import com.advantal.responsepayload.TimeSeriesDetails;
import com.advantal.utils.Constant;
import com.advantal.utils.DateUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
@Service
public class DataProcessUtil {

	@Autowired
	private ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StockGainerRepository gainerRepository;

	@Autowired
	private StockLoserRepository loserRepository;

	@Autowired
	private StockProfileRepository stockProfileRepository;

	@Autowired
	private StockStatisticsRepository stockStatisticsRepository;

	@Autowired
	private ExchangeRepository exchangeRepository;

	@Autowired
	private Executor executor;

	public static Long oldCount = 0L, newCount = 0L;

//	public Long count = 0L;

	@Autowired
	private StockRecommendationRepository recommendationRepository;

	@Autowired
	private ShareHolderRepository shareHolderRepository;

	@Autowired
	private StockPriceTargetRepository priceTargetRepository;

	@Autowired
	private KeyExecutiveRepository keyExecutiveRepository;

	@Autowired
	private IndicesRepository indicesRepository;

	@Autowired
	private DividendRepository dividendRepository;

	@Autowired
	private EarningRepository earningRepository;

	@Autowired
	private EconomicRepository economicRepository;

	@Autowired
	private IposRepository iposRepository;

	@Autowired
	private HolidayRepository holidayRepository;

	@Autowired
	private SecFilingRepository secFilingRepository;

	@Autowired
	private CryptoDataProcessUtil cryptoDataProcessUtil;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private YearGraphRepository yearGraphRepository;

	@Autowired
	private DayGraphRepository dayGraphRepository;

	@Autowired
	private MinuteGraphRepository minuteGraphRepository;

	@Autowired
	private IndicesYearGraphRepository indicesYearGraphRepository;

	@Autowired
	private IndicesDayGraphRepository indicesDayGraphRepository;

	@Autowired
	private IndicesMinuteGraphRepository indicesOneMinuteGraphRepository;

	/* --- Stock updating --- */
	public void stockProcess(Page<Stock> page) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Exchange> exchangeList = exchangeRepository.findAll();
			if (!exchangeList.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				List<Stock> oldList = page.getContent();
				log.info("Searching the symbol for update it...");
				for (Stock localStock : oldList) {
					if (localStock.getStockProfile() == null) {
						StockProfile localStockProfile = stockProfileRepository.findBySymbol(localStock.getSymbol());
						if (localStockProfile != null) {
							localStock.setStockProfile(localStockProfile);
							localStock.setSector(localStock.getStockProfile().getSector());
							localStock.setIsActivelyTrading(localStock.getStockProfile().getIsActivelyTrading());
						}
					} else {
						localStock.setSector(localStock.getStockProfile().getSector());
						localStock.setIsActivelyTrading(localStock.getStockProfile().getIsActivelyTrading());
					}

					localStock.setUpdationDate(new Date());
					localStock.setFavorite(Constant.FALSE);
					stockRepository.save(localStock);
					oldCount++;
					log.info(oldCount + ": Stock have updated! | Symbol is - " + localStock.getSymbol()
							+ " | status - {}", Constant.OK);
				}
				log.info("Total Data Processed! " + "[ Total Stocks Updated : " + oldCount + " ] ! status - {}",
						Constant.OK);
				if (page.isLast()) {
					oldCount = 0L;
				}
			} else {
				log.info(Constant.EXCHANGE_LIST_EMPTY_MESSAGE + "! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
	}

	public List<Stock> getAllStocksFromServer() {
		List<Stock> stockList = new ArrayList<Stock>();
		List<Exchange> exchangeList = exchangeRepository.findAll();
		if (exchangeList.size() > 0) {
			String apiResponse = "";
			for (Exchange exchange : exchangeList) {
				apiResponse = thirdPartyApiUtil.stockList(exchange.getName());
				if (!apiResponse.isEmpty()) {
					List<StockMaster> stockListObj = null;
					try {
						Type collectionType = new TypeToken<List<StockMaster>>() {
						}.getType();
						stockListObj = new Gson().fromJson(apiResponse.toString(), collectionType);
					} catch (JsonParseException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					} catch (JsonSyntaxException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					}
					if (stockListObj != null && stockListObj.size() > 0) {
						log.info("Stock list found ! status - {}", Constant.OK);
						for (StockMaster stockMaster : stockListObj) {
							Stock stock = new Stock();
							BeanUtils.copyProperties(stockMaster, stock);
							stock.setIndexName(stockMaster.getIndex());
							if (stockMaster.getExchangeShortName() != null) {
								if ((stock.getType() != null && stock.getType().equalsIgnoreCase("stock"))) {
									if (stockMaster.getExchangeShortName().equalsIgnoreCase(exchange.getName())) {
										stock.setExchange(stockMaster.getExchangeShortName());
										stock.setCountry(exchange.getCountry());
										stock.setCurrency(exchange.getCurrency());
										stock.setIndexName(
												stockMaster.getIndex() != null ? stockMaster.getIndex() : "other");
										stockList.add(stock);
									}
								}
							} else if (stockMaster.getExchange() != null) {
								String symbol = stock.getSymbol();
								if (symbol.contains(".SR")) {
									int index = symbol.indexOf(".");
									symbol = symbol.substring(0, index);
									stock.setSymbol(symbol);
								}
								stock.setExchange(exchange.getName());
								stock.setCountry(exchange.getCountry());
								stock.setCurrency(exchange.getCurrency());
								stockList.add(stock);
							}
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
		} else {
			log.info(Constant.EXCHANGE_LIST_EMPTY_MESSAGE + "! status - {}", Constant.NOT_FOUND);
		}
		return stockList;
	}

	public void saveStock() {
		getStockByUpdationDate();
	}

	private void getStockByUpdationDate() {
		Integer pageIndex = 0;
		final Integer pageSize = 100;
		Page<Stock> page = null;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -24);
//		cal.add(Calendar.HOUR, -120);
		Date olderDateObj = cal.getTime();
		page = stockRepository.findByUpdationDateBefore(olderDateObj, PageRequest.of(pageIndex, pageSize));
		if (page.getContent().size() > 0) {
			log.info("records for page " + pageIndex + " is " + page.getContent().size());//
			while (page.hasNext()) {
				MarketTask task = new MarketTask(page);
				executor.execute(task);
				pageIndex = pageIndex + 1;
				page = stockRepository.findByUpdationDateBefore(olderDateObj, PageRequest.of(pageIndex, pageSize));
				log.info("records for page " + pageIndex + " is " + page.getContent().size());
			}
			MarketTask task = new MarketTask(page);
			executor.execute(task);
		}
	}

	private class MarketTask implements Runnable {
		private Page<Stock> page;

		MarketTask(Page<Stock> page) {
			super();
			this.page = page;
		}

		@Override
		public void run() {
			stockProcess(page);
		}
	}
	/* --- End --- */

	private class StockAndCryptoMarketTask implements Runnable {
		@Override
		public void run() {
			saveMasterStockData(); /* update master stock list */
			saveStockProfileData(); /* update stock profile list */
			saveStock(); /* update stock with profile data */
			cryptoDataProcessUtil.saveBtcDominance(); /* update BTC dominance */
			cryptoDataProcessUtil.saveCryptoList(); /* Update Crypto List */
			cryptoDataProcessUtil.saveNewlyCrypto(); /* Update Newly listed crypto */
			cryptoDataProcessUtil.saveCryptoExchangeList(); /* update exchange list */

//			saveKeyExecutiveData(); /* update keyExecutive data */
//			saveShareHolderData();/* update share holder data */
//			saveStockRecommendation(); /* update analyst rating */
//			saveStockPriceTarget();//
//			saveStockStatisticsData();/* not completed */
		}
	}

	public void marketThread() {
		StockAndCryptoMarketTask stockAndCryptoMarketTask = new StockAndCryptoMarketTask();
		Thread thread = new Thread(stockAndCryptoMarketTask);
		thread.start();
	}

	/* --- Master list of stock updating --- */
	public void saveMasterStockData() {
		List<Stock> list = getAllStocksFromServer();
		masterStockDataProcess(list);
	}

	public void masterStockDataProcess(List<Stock> list) {
		try {
			Integer oldDataCount = 0, newDataCount = 0;
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {
				Stock localStock = stockRepository.findBySymbol(serverStock.getSymbol());
				if (localStock != null) {
					localStock.setLastSyncDate(new Date());
					localStock.setName(serverStock.getName());
					localStock.setIndexName(serverStock.getIndexName());
					stockRepository.save(localStock);
					oldDataCount++;
					log.info(oldDataCount + ": Master Stock updated successfully! | Symbol is - "
							+ serverStock.getSymbol() + " | status - {}", Constant.OK);
				} else {
					serverStock.setPrice(0.0f);
					serverStock.setPercent_change(0.0f);
					serverStock.setPrice_change(0.0f);
					serverStock.setLow(0.0f);
					serverStock.setHigh(0.0f);
					serverStock.setFtw_high(0.0f);
					serverStock.setFtw_low(0.0f);
					serverStock.setMarketCap(0L);
					serverStock.setVolume(0L);
					serverStock.setAvgVolume(0L);
					serverStock.setOpen(0.0f);
					serverStock.setPreviousClose(0.0f);
					serverStock.setEps(0.0f);
					serverStock.setPe(0.0f);
					serverStock.setSharesOutstanding(0L);
					serverStock.setTimestamp(0L);

					serverStock.setInstrumentType("stock");
					serverStock.setCreationDate(new Date());
					serverStock.setUpdationDate(new Date());
					serverStock.setLastSyncDate(new Date());
					serverStock.setLastUpdatedMarketData(new Date());
					serverStock.setFavorite(Constant.FALSE);
					serverStock.setStatus(Constant.ONE);
					stockRepository.save(serverStock);
					newDataCount++;
					log.info(newDataCount + ": Master Stock saved successfully! | Symbol is - "
							+ serverStock.getSymbol() + " | status - {}", Constant.OK);
				}
			}

//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.HOUR, -24);
//			Date olderDateObj = cal.getTime();
//			List<Stock> remainStocklist = stockRepository.findByLastSyncDateBefore(olderDateObj);
			List<Stock> remainStocklist = stockRepository.findByIsActivelyTrading(false);
			if (!remainStocklist.isEmpty()) {
				List<Stock> remainStocklist2 = new ArrayList<>();
				for (Stock stock : remainStocklist) {
					stock.setStatus(Constant.ZERO);
					remainStocklist2.add(stock);
				}
				stockRepository.saveAll(remainStocklist2);
			}
			log.info("Total Master Stock Processed! " + "[ Updated : " + oldDataCount + " ] | " + "[ New added : "
					+ newDataCount + " ] | " + "[ Total De-listed stocks : " + remainStocklist.size() + " ] ");
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}
	/* --- End Master Stock process --- */

	/* --- Stock Profile updating --- */
	public void saveStockProfileData() {
		List<Stock> list = getAllStocksFromServer();
		stockProfileDataProcess(list);
	}

	public void stockProfileDataProcess(List<Stock> list) {
		try {
			String profile_response = "", saudi_profile_response = "";
			Integer oldDataCount = 0, newDataCount = 0;
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {
				SaudiProfileResponse profileResponse = new SaudiProfileResponse();
				profile_response = thirdPartyApiUtil.getProfile(serverStock);
				if (!profile_response.isEmpty()) {
					Type collectionType = new TypeToken<List<StockProfileResponse>>() {
					}.getType();
					List<StockProfileResponse> serverstockProfileObj = null;
					try {
						serverstockProfileObj = new Gson().fromJson(profile_response, collectionType);
					} catch (JsonParseException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					} catch (JsonSyntaxException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					}
					if (serverstockProfileObj != null && !serverstockProfileObj.isEmpty()) {
						if (serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
							saudi_profile_response = thirdPartyApiUtil.getSaudiProfile();
							if (!saudi_profile_response.isEmpty()) {
								Type collectionType1 = new TypeToken<List<SaudiProfileResponse>>() {
								}.getType();
								List<SaudiProfileResponse> saudiProfileResponse = null;
								try {
									saudiProfileResponse = new Gson().fromJson(saudi_profile_response, collectionType1);
									if (serverstockProfileObj != null && !serverstockProfileObj.isEmpty()) {
										for (SaudiProfileResponse saudiProfileResponse1 : saudiProfileResponse) {
											String sym = serverStock.getSymbol()+".SR";
											if(sym.equals(saudiProfileResponse1.getSymbol())) {
												BeanUtils.copyProperties(saudiProfileResponse1, profileResponse);
												break;
											}
										}
									}
								} catch (JsonParseException e) {
									log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
								} catch (JsonSyntaxException e) {
									log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
								}
							}
						}
						StockProfile localStockProfile = stockProfileRepository.findBySymbol(serverStock.getSymbol());
						if (localStockProfile != null) {
							StockProfile stockProfile = new StockProfile();
							stockProfile.setId(localStockProfile.getId());
							stockProfile.setCreationDate(localStockProfile.getCreationDate());
							stockProfile.setSymbol(serverStock.getSymbol());

							stockProfile.setCompanyName(serverstockProfileObj.get(0).getCompanyName() != null
									&& serverstockProfileObj.get(0).getCompanyName() != ""
											? serverstockProfileObj.get(0).getCompanyName()
											: "");
							stockProfile.setIsin(serverstockProfileObj.get(0).getIsin() != null
									&& serverstockProfileObj.get(0).getIsin() != ""
											? serverstockProfileObj.get(0).getIsin()
											: "");
							stockProfile.setCusip(serverstockProfileObj.get(0).getCusip() != null
									&& serverstockProfileObj.get(0).getCusip() != ""
											? serverstockProfileObj.get(0).getCusip()
											: "");
							stockProfile.setExchange(serverstockProfileObj.get(0).getExchangeShortName() != null
									&& serverstockProfileObj.get(0).getExchangeShortName() != ""
											? serverstockProfileObj.get(0).getExchangeShortName()
											: "");
							stockProfile.setIndustry(serverstockProfileObj.get(0).getIndustry() != null
									&& serverstockProfileObj.get(0).getIndustry() != ""
											? serverstockProfileObj.get(0).getIndustry()
											: "");
							stockProfile.setWebsite(serverstockProfileObj.get(0).getWebsite() != null
									&& serverstockProfileObj.get(0).getWebsite() != ""
											? serverstockProfileObj.get(0).getWebsite()
											: "");
							stockProfile.setDescription(serverstockProfileObj.get(0).getDescription() != null
									&& serverstockProfileObj.get(0).getDescription() != ""
											? serverstockProfileObj.get(0).getDescription()
											: "");
							stockProfile.setCeo(serverstockProfileObj.get(0).getCeo() != null
									&& serverstockProfileObj.get(0).getCeo() != ""
											? serverstockProfileObj.get(0).getCeo()
											: "");
							if (serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA) && profileResponse != null) {
								stockProfile.setSector(
										profileResponse.getSector() != null ? profileResponse.getSector() : "");
							}else if (serverStock.getCountry().equalsIgnoreCase(Constant.UNITED_STATES)) {
								stockProfile.setSector(serverstockProfileObj.get(0).getSector() != null
										&& serverstockProfileObj.get(0).getSector() != ""
												? serverstockProfileObj.get(0).getSector()
												: "");
							}else {
								stockProfile.setSector("");
							}
							stockProfile.setCountry(serverstockProfileObj.get(0).getCountry() != null
									&& serverstockProfileObj.get(0).getCountry() != "" ? serverStock.getCountry() : "");
							stockProfile.setNoOfEmployees(serverstockProfileObj.get(0).getFullTimeEmployees() != null
									&& serverstockProfileObj.get(0).getFullTimeEmployees() != ""
											? serverstockProfileObj.get(0).getFullTimeEmployees()
											: "");
							stockProfile.setPhone(serverstockProfileObj.get(0).getPhone() != null
									&& serverstockProfileObj.get(0).getPhone() != ""
											? serverstockProfileObj.get(0).getPhone()
											: "");
							stockProfile.setAddress(serverstockProfileObj.get(0).getAddress() != null
									&& serverstockProfileObj.get(0).getAddress() != ""
											? serverstockProfileObj.get(0).getAddress()
											: "");
							stockProfile.setCity(serverstockProfileObj.get(0).getCity() != null
									&& serverstockProfileObj.get(0).getCity() != ""
											? serverstockProfileObj.get(0).getCity()
											: "");
							stockProfile.setState(serverstockProfileObj.get(0).getState() != null
									&& serverstockProfileObj.get(0).getState() != ""
											? serverstockProfileObj.get(0).getState()
											: "");
							stockProfile.setZip(serverstockProfileObj.get(0).getZip() != null
									&& serverstockProfileObj.get(0).getZip() != ""
											? serverstockProfileObj.get(0).getZip()
											: "");
							stockProfile.setLogo(serverstockProfileObj.get(0).getImage() != null
									&& serverstockProfileObj.get(0).getImage() != ""
											? serverstockProfileObj.get(0).getImage()
											: "");
							stockProfile
									.setIsActivelyTrading(serverstockProfileObj.get(0).getIsActivelyTrading() != null
											? serverstockProfileObj.get(0).getIsActivelyTrading()
											: false);
							stockProfile.setIpoDate(serverstockProfileObj.get(0).getIpoDate() != null
									&& serverstockProfileObj.get(0).getIpoDate() != ""
											? serverstockProfileObj.get(0).getIpoDate()
											: "");

							stockProfile.setUpdationDate(new Date());
							stockProfile = stockProfileRepository.save(stockProfile);

							oldCount++;
							oldDataCount++;
							log.info(oldDataCount + ": Stock Profile updated successfully! | Symbol is - "
									+ serverStock.getSymbol() + " | status - {}", Constant.OK);
						} else {
							StockProfile stockProfile = new StockProfile();
//						BeanUtils.copyProperties(serverstockProfileObj.get(0), stockProfile);
							stockProfile.setSymbol(serverStock.getSymbol());

							stockProfile.setCompanyName(serverstockProfileObj.get(0).getCompanyName() != null
									&& serverstockProfileObj.get(0).getCompanyName() != ""
											? serverstockProfileObj.get(0).getCompanyName()
											: "");
							stockProfile.setIsin(serverstockProfileObj.get(0).getIsin() != null
									&& serverstockProfileObj.get(0).getIsin() != ""
											? serverstockProfileObj.get(0).getIsin()
											: "");
							stockProfile.setCusip(serverstockProfileObj.get(0).getCusip() != null
									&& serverstockProfileObj.get(0).getCusip() != ""
											? serverstockProfileObj.get(0).getCusip()
											: "");
							stockProfile.setExchange(serverstockProfileObj.get(0).getExchangeShortName() != null
									&& serverstockProfileObj.get(0).getExchangeShortName() != ""
											? serverstockProfileObj.get(0).getExchangeShortName()
											: "");
							stockProfile.setIndustry(serverstockProfileObj.get(0).getIndustry() != null
									&& serverstockProfileObj.get(0).getIndustry() != ""
											? serverstockProfileObj.get(0).getIndustry()
											: "");
							stockProfile.setWebsite(serverstockProfileObj.get(0).getWebsite() != null
									&& serverstockProfileObj.get(0).getWebsite() != ""
											? serverstockProfileObj.get(0).getWebsite()
											: "");
							stockProfile.setDescription(serverstockProfileObj.get(0).getDescription() != null
									&& serverstockProfileObj.get(0).getDescription() != ""
											? serverstockProfileObj.get(0).getDescription()
											: "");
							stockProfile.setCeo(serverstockProfileObj.get(0).getCeo() != null
									&& serverstockProfileObj.get(0).getCeo() != ""
											? serverstockProfileObj.get(0).getCeo()
											: "");
//							stockProfile.setSector(serverstockProfileObj.get(0).getSector() != null
//									&& serverstockProfileObj.get(0).getSector() != ""
//											? serverstockProfileObj.get(0).getSector()
//											: "");
							
							if (serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA) && profileResponse != null) {
								stockProfile.setSector(
										profileResponse.getSector() != null ? profileResponse.getSector() : "");
							}else if (serverStock.getCountry().equalsIgnoreCase(Constant.UNITED_STATES)) {
								stockProfile.setSector(serverstockProfileObj.get(0).getSector() != null
										&& serverstockProfileObj.get(0).getSector() != ""
												? serverstockProfileObj.get(0).getSector()
												: "");
							}else {
								stockProfile.setSector("");
							}
							
							stockProfile.setCountry(serverstockProfileObj.get(0).getCountry() != null
									&& serverstockProfileObj.get(0).getCountry() != "" ? serverStock.getCountry() : "");
							stockProfile.setNoOfEmployees(serverstockProfileObj.get(0).getFullTimeEmployees() != null
									&& serverstockProfileObj.get(0).getFullTimeEmployees() != ""
											? serverstockProfileObj.get(0).getFullTimeEmployees()
											: "");
							stockProfile.setPhone(serverstockProfileObj.get(0).getPhone() != null
									&& serverstockProfileObj.get(0).getPhone() != ""
											? serverstockProfileObj.get(0).getPhone()
											: "");
							stockProfile.setAddress(serverstockProfileObj.get(0).getAddress() != null
									&& serverstockProfileObj.get(0).getAddress() != ""
											? serverstockProfileObj.get(0).getAddress()
											: "");
							stockProfile.setCity(serverstockProfileObj.get(0).getCity() != null
									&& serverstockProfileObj.get(0).getCity() != ""
											? serverstockProfileObj.get(0).getCity()
											: "");
							stockProfile.setState(serverstockProfileObj.get(0).getState() != null
									&& serverstockProfileObj.get(0).getState() != ""
											? serverstockProfileObj.get(0).getState()
											: "");
							stockProfile.setZip(serverstockProfileObj.get(0).getZip() != null
									&& serverstockProfileObj.get(0).getZip() != ""
											? serverstockProfileObj.get(0).getZip()
											: "");
							stockProfile.setLogo(serverstockProfileObj.get(0).getImage() != null
									&& serverstockProfileObj.get(0).getImage() != ""
											? serverstockProfileObj.get(0).getImage()
											: "");
							stockProfile
									.setIsActivelyTrading(serverstockProfileObj.get(0).getIsActivelyTrading() != null
											? serverstockProfileObj.get(0).getIsActivelyTrading()
											: false);
							stockProfile.setIpoDate(serverstockProfileObj.get(0).getIpoDate() != null
									&& serverstockProfileObj.get(0).getIpoDate() != ""
											? serverstockProfileObj.get(0).getIpoDate()
											: "");

							stockProfile.setCreationDate(new Date());
							stockProfile.setUpdationDate(new Date());
							stockProfile = stockProfileRepository.save(stockProfile);
							newCount++;
							newDataCount++;
							log.info(newDataCount + ": Stock Profile saved successfully! | Symbol is - "
									+ serverStock.getSymbol() + " | status - {}", Constant.OK);
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.NOT_EXIST);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
			log.info("Total Stock Profile Processed! " + "[ Updated : " + oldCount + " ] | " + "[ New added : "
					+ newCount + " ]");
			oldCount = 0L;
			newCount = 0L;

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* --- End Profile --- */

	/* --- Stock Statistics updating --- */
	public void saveStockStatisticsData() {
		List<Stock> list = getAllStocksFromServer();
		stockStatisticsDataProcess(list);
	}

	public void stockStatisticsDataProcess(List<Stock> list) {
		try {
			String statistics_response = "", statistics_response1 = "", statistics_response2 = "";
			Integer oldDataCount = 0, newDataCount = 0, notFoundCount = 0;
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {

				// called first End point
				/*
				 * getting response from FMP for priceToBookRatioTTM & dividendPerShareTTM &
				 * dividendYielPercentageTTM
				 */
				statistics_response = thirdPartyApiUtil.getStockStatistics(serverStock);
				if (!statistics_response.isBlank()) {
					Map<?, ?> statisticsResponse = mapper.readValue(statistics_response, Map.class);
					if (statisticsResponse.get("ratios") != null) {
						StatisticsResponse stockStatisticsResponse = mapper.convertValue(statisticsResponse,
								StatisticsResponse.class);
						if (!stockStatisticsResponse.getRatios().isEmpty()
								&& stockStatisticsResponse.getRatios() != null) {
							for (StatisticsRatio ratio : stockStatisticsResponse.getRatios()) {
								if (ratio != null && ratio.getDividendPerShareTTM() != null
										&& ratio.getDividendYielPercentageTTM() != null
										&& ratio.getPriceToBookRatioTTM() != null) {
									StockStatistics statistics = stockStatisticsRepository
											.findBySymbol(serverStock.getSymbol());
									/*----------- stockStatistics updated here ----------*/
									if (statistics != null) {
										statistics.setPriceToBookRatioTTM(
												ratio.getPriceToBookRatioTTM() != null ? ratio.getPriceToBookRatioTTM()
														: 0);
										statistics.setDividendPerShareTTM(
												ratio.getDividendPerShareTTM() != null ? ratio.getDividendPerShareTTM()
														: 0);
										statistics.setDividendYielPercentageTTM(
												ratio.getDividendYielPercentageTTM() != null
														? ratio.getDividendYielPercentageTTM()
														: 0);
										statistics.setUpdationDate(new Date());
										stockStatisticsRepository.save(statistics);
										oldDataCount++;
										log.info(oldDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Stock statistics data updated successfully! | status - {}",
												Constant.OK);
									} else {
										/*----------- new added stockStatistics here ----------*/
										StockStatistics newstatistics = new StockStatistics();
										newstatistics.setSymbol(serverStock.getSymbol());
										newstatistics.setCountry(serverStock.getCountry());
										newstatistics.setExchange(serverStock.getExchange());
										newstatistics.setStatus(Constant.ONE);
										newstatistics.setCreationDate(new Date());
										newstatistics.setPriceToBookRatioTTM(
												ratio.getPriceToBookRatioTTM() != null ? ratio.getPriceToBookRatioTTM()
														: 0);
										newstatistics.setDividendPerShareTTM(
												ratio.getDividendPerShareTTM() != null ? ratio.getDividendPerShareTTM()
														: 0);
										newstatistics.setDividendYielPercentageTTM(
												ratio.getDividendYielPercentageTTM() != null
														? ratio.getDividendYielPercentageTTM()
														: 0);
										stockStatisticsRepository.save(newstatistics);
										newDataCount++;
										log.info(newDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Stock statistics data added successfully! | status - {}",
												Constant.OK);
									}
								} else {

									notFoundCount++;
									log.info(notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
											+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
											Constant.SERVER_ERROR);
								}
							}
						} else {
							notFoundCount++;
							log.info(
									notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
											+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					} else {
						notFoundCount++;
						log.info(
								notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
										+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}

				// called second End point
				/*
				 * getting response from FMP for peRatio & bookValuePerShare & Enter price value
				 */
				statistics_response1 = thirdPartyApiUtil.getStockStatistics1(serverStock);
				if (statistics_response1.length() > 0) {
					Type collectionType = new TypeToken<List<StatisticsFirstResponse>>() {
					}.getType();
					List<StatisticsFirstResponse> statisticsFirstResponses = new Gson().fromJson(statistics_response1,
							collectionType);
					for (StatisticsFirstResponse response : statisticsFirstResponses) {
						StockStatistics stockStatistics = stockStatisticsRepository
								.findBySymbolAndStatus(serverStock.getSymbol(), Constant.ONE);
						if (stockStatistics != null) {
							if (stockStatistics.getMetrics_endpoint_date() != null) {
								if (stockStatistics.getMetrics_endpoint_date().equals(response.getDate())) {
									/*------------- stockStatistis data update here --------------*/
									stockStatistics.setBookValuePerShareTTM(
											response.getBookValuePerShare() != null ? response.getBookValuePerShare()
													: 0);
									stockStatistics.setMetrics_endpoint_date(response.getDate());
									stockStatistics
											.setPeRatioTTM(response.getPeRatio() != null ? response.getPeRatio() : 0);
									stockStatistics.setEnterpriseValueTTM(
											response.getEnterpriseValue() != null ? response.getEnterpriseValue() : 0);
									stockStatistics.setUpdationDate(new Date());
									stockStatisticsRepository.save(stockStatistics);
									oldDataCount++;
									log.info(oldDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data updated successfully! | status - {}",
											Constant.OK);
								} else {
									/*------------- stockStatistis data update here --------------*/
									stockStatistics.setBookValuePerShareTTM(
											response.getBookValuePerShare() != null ? response.getBookValuePerShare()
													: 0);
									stockStatistics.setMetrics_endpoint_date(response.getDate());
									stockStatistics
											.setPeRatioTTM(response.getPeRatio() != null ? response.getPeRatio() : 0);
									stockStatistics.setEnterpriseValueTTM(
											response.getEnterpriseValue() != null ? response.getEnterpriseValue() : 0);
									stockStatistics.setUpdationDate(new Date());
									stockStatisticsRepository.save(stockStatistics);
									oldDataCount++;
									log.info(oldDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data updated successfully! | status - {}",
											Constant.OK);
								}
							} else {
								/*------------- stockStatistis data update here --------------*/
								stockStatistics.setBookValuePerShareTTM(
										response.getBookValuePerShare() != null ? response.getBookValuePerShare() : 0);
								stockStatistics.setMetrics_endpoint_date(response.getDate());
								stockStatistics
										.setPeRatioTTM(response.getPeRatio() != null ? response.getPeRatio() : 0);
								stockStatistics.setEnterpriseValueTTM(
										response.getEnterpriseValue() != null ? response.getEnterpriseValue() : 0);
								stockStatistics.setUpdationDate(new Date());
								stockStatisticsRepository.save(stockStatistics);
								oldDataCount++;
								log.info(
										oldDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Stock statistics data updated successfully! | status - {}",
										Constant.OK);
							}
						} else {
							/*------------- new added data here --------------*/
							StockStatistics statistics = new StockStatistics();
							statistics.setSymbol(serverStock.getSymbol());
							statistics.setCountry(serverStock.getCountry());
							statistics.setCreationDate(new Date());
							statistics.setStatus(Constant.ONE);
							statistics.setExchange(serverStock.getExchange());
							statistics.setBookValuePerShareTTM(response.getBookValuePerShare());
							statistics.setMetrics_endpoint_date(response.getDate());
							statistics.setPeRatioTTM(response.getPeRatio());
							statistics.setEnterpriseValueTTM(response.getEnterpriseValue());
							stockStatisticsRepository.save(statistics);
							newDataCount++;
							log.info(
									newDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data added successfully! | status - {}",
									Constant.OK);
						}
						break;
					}
				} else {
					notFoundCount++;
					log.info(
							notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
									+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
							Constant.SERVER_ERROR);
				}

				// called third End point
				/* getting response from FMP for returnOnAssets & returnOnEquity */
				statistics_response2 = thirdPartyApiUtil.getStockStatistics2(serverStock);
				if (statistics_response2.length() > 0) {
					Type collectionType = new TypeToken<List<StatisticsFirstResponse>>() {
					}.getType();
					List<StatisticsFirstResponse> statisticsFirstResponses = new Gson().fromJson(statistics_response2,
							collectionType);
					for (StatisticsFirstResponse response : statisticsFirstResponses) {
						StockStatistics stockStatistics = stockStatisticsRepository
								.findBySymbolAndStatus(serverStock.getSymbol(), Constant.ONE);
						if (stockStatistics != null) {
							if (stockStatistics.getRatio_endpoint_date() != null) {
								if (stockStatistics.getRatio_endpoint_date().equals(response.getDate())) {
									/*------------- stockStatistis data update here --------------*/
									stockStatistics.setReturnOnAssetsTTM(
											response.getReturnOnAssets() != null ? response.getReturnOnAssets() : 0);
									stockStatistics.setRatio_endpoint_date(response.getDate());
									stockStatistics.setReturnOnEquityTTM(
											response.getReturnOnEquity() != null ? response.getReturnOnEquity() : 0);
									stockStatistics.setUpdationDate(new Date());
									stockStatisticsRepository.save(stockStatistics);
									oldDataCount++;
									log.info(oldDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data updated successfully! | status - {}",
											Constant.OK);
								} else {
									/*------------- stockStatistis data update here --------------*/
									stockStatistics.setReturnOnAssetsTTM(
											response.getReturnOnAssets() != null ? response.getReturnOnAssets() : 0);
									stockStatistics.setRatio_endpoint_date(response.getDate());
									stockStatistics.setReturnOnEquityTTM(
											response.getReturnOnEquity() != null ? response.getReturnOnEquity() : 0);
									stockStatistics.setUpdationDate(new Date());
									stockStatisticsRepository.save(stockStatistics);
									oldDataCount++;
									log.info(oldDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data updated successfully! | status - {}",
											Constant.OK);
								}
							} else {
								/*------------- stockStatistis data update here --------------*/
								stockStatistics.setReturnOnAssetsTTM(
										response.getReturnOnEquity() != null ? response.getReturnOnEquity() : 0);
								stockStatistics.setRatio_endpoint_date(response.getDate());
								stockStatistics.setReturnOnEquityTTM(
										response.getReturnOnEquity() != null ? response.getReturnOnEquity() : 0);
								stockStatistics.setUpdationDate(new Date());
								stockStatisticsRepository.save(stockStatistics);
								oldDataCount++;
								log.info(
										oldDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Stock statistics data updated successfully! | status - {}",
										Constant.OK);
							}
						} else {
							/*------------- new added data here --------------*/
							StockStatistics statistics = new StockStatistics();
							statistics.setSymbol(serverStock.getSymbol());
							statistics.setCountry(serverStock.getCountry());
							statistics.setCreationDate(new Date());
							statistics.setStatus(Constant.ONE);
							statistics.setExchange(serverStock.getExchange());
							statistics.setReturnOnAssetsTTM(response.getReturnOnAssets());
							statistics.setRatio_endpoint_date(response.getDate());
							statistics.setReturnOnEquityTTM(response.getReturnOnEquity());
							stockStatisticsRepository.save(statistics);
							newDataCount++;
							log.info(
									newDataCount + " : " + serverStock.getSymbol()
											+ " - Symbol Stock statistics data added successfully! | status - {}",
									Constant.OK);
						}
						break;
					}
				} else {
					notFoundCount++;
					log.info(
							notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
									+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
							Constant.SERVER_ERROR);
				}

			}
			log.info("Total Stock Statistics Processed! " + "[ Updated : " + oldCount + " ] | " + "[ New added : "
					+ newCount + " ]");
		} catch (Exception e) {
			e.printStackTrace();
		}

//		try {
//			String api_usage_response = "", statistics_response = "";
//			Integer oldDataCount = 0, newDataCount = 0, api_usage_code, statistics_code;
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			log.info("Searching the symbol for update it...");
//			for (Stock serverStock : list) {
//				api_usage_response = thirdPartyApiUtil.getApiUsage();
//				statistics_response = thirdPartyApiUtil.getStockStatistics(serverStock.getSymbol(),
//						serverStock.getExchange());
//				if (!api_usage_response.isBlank() && !statistics_response.isBlank()) {
//					Map<?, ?> map_api_usage_response = mapper.readValue(api_usage_response, Map.class);
//					api_usage_code = (Integer) map_api_usage_response.get("code");
//					if (api_usage_code == null) {
//						ApiUsage apiUsage = mapper.convertValue(map_api_usage_response, ApiUsage.class);
//						LocalDateTime localDateTime1;
//						int seconds, seconds2 = 60;
//						long diff, oldLongDate;
//						localDateTime1 = DateUtil.stringToLocalDateTime(apiUsage.getTimestamp());
//						oldLongDate = DateUtil.StringDateToLongDateTime(apiUsage.getTimestamp());
//						seconds = localDateTime1.getSecond();
//						diff = (long) (seconds2 - seconds);
//						diff = diff * 1000L;
//						oldLongDate = oldLongDate + diff;
//
//						/* Checking Api usage count */
//						if (apiUsage.getCurrent_usage() >= apiUsage.getPlan_limit() - 584) {
//							log.info("Thread is going to sleep for : " + (diff / 1000) + " Seconds");
//							Thread.sleep(diff);
//							log.info("Thread Resumed !!");
//						}
//
//						Map<?, ?> map_statistics_response = mapper.readValue(statistics_response, Map.class);
//						statistics_code = (Integer) map_statistics_response.get("code");
//						if (statistics_code == null) {
//							StockStatistics statistics = stockStatisticsRepository
//									.findBySymbol(serverStock.getSymbol());
//							if (statistics != null) {
//								StockStatistics stockStatistics = new StockStatistics();
//								StatisticsResponse statisticsResponse = new StatisticsResponse();
//								statisticsResponse = mapper.convertValue(map_statistics_response,
//										StatisticsResponse.class);
//								stockStatistics.setId(statistics.getId());
//								stockStatistics.setCreationDate(statistics.getCreationDate());
//								stockStatistics.setUpdationDate(new Date());
//								stockStatistics.setSymbol(serverStock.getSymbol());
//								stockStatistics.setBook_value_per_share_mrq(statisticsResponse.getStatistics()
//										.getFinancials().getBalance_sheet().getBook_value_per_share_mrq());
//								stockStatistics.setDividend_date(statisticsResponse.getStatistics()
//										.getDividends_and_splits().getDividend_date());
//								stockStatistics.setEnterprise_value(statisticsResponse.getStatistics()
//										.getValuations_metrics().getEnterprise_value());
//								stockStatistics.setForward_pe(
//										statisticsResponse.getStatistics().getValuations_metrics().getForward_pe());
////								stockStatistics.setMarket_capitalization(statisticsResponse.getStatistics()
////										.getValuations_metrics().getMarket_capitalization());
//								stockStatistics.setPrice_to_book_mrq(statisticsResponse.getStatistics()
//										.getValuations_metrics().getPrice_to_book_mrq());
//								stockStatistics.setPrice_to_sales_ttm(statisticsResponse.getStatistics()
//										.getValuations_metrics().getPrice_to_sales_ttm());
//								stockStatistics.setReturn_on_assets_ttm(
//										statisticsResponse.getStatistics().getFinancials().getReturn_on_assets_ttm());
//								stockStatistics.setReturn_on_equity_ttm(
//										statisticsResponse.getStatistics().getFinancials().getReturn_on_equity_ttm());
////								stockStatistics.setShares_outstanding(statisticsResponse.getStatistics()
////										.getStock_statistics().getShares_outstanding());
//								stockStatistics.setTrailing_annual_dividend_yield(statisticsResponse.getStatistics()
//										.getDividends_and_splits().getTrailing_annual_dividend_yield());
//								stockStatistics.setTrailing_pe(
//										statisticsResponse.getStatistics().getValuations_metrics().getTrailing_pe());
//								stockStatistics = stockStatisticsRepository.save(stockStatistics);
//								oldDataCount++;
//								oldCount++;
//								log.info(oldDataCount + ": Stock Statistics updated successfully! | Symbol is - "
//										+ serverStock.getSymbol() + " | status - {}", Constant.OK);
//							} else {
//								StockStatistics stockStatistics = new StockStatistics();
//								StatisticsResponse statisticsResponse = new StatisticsResponse();
//								statisticsResponse = mapper.convertValue(map_statistics_response,
//										StatisticsResponse.class);
//								stockStatistics.setSymbol(serverStock.getSymbol());
//								stockStatistics.setCreationDate(new Date());
//								stockStatistics.setUpdationDate(new Date());
//								stockStatistics.setBook_value_per_share_mrq(statisticsResponse.getStatistics()
//										.getFinancials().getBalance_sheet().getBook_value_per_share_mrq());
//								stockStatistics.setDividend_date(statisticsResponse.getStatistics()
//										.getDividends_and_splits().getDividend_date());
//								stockStatistics.setEnterprise_value(statisticsResponse.getStatistics()
//										.getValuations_metrics().getEnterprise_value());
//								stockStatistics.setForward_pe(
//										statisticsResponse.getStatistics().getValuations_metrics().getForward_pe());
////								stockStatistics.setMarket_capitalization(statisticsResponse.getStatistics()
////										.getValuations_metrics().getMarket_capitalization());
//								stockStatistics.setPrice_to_book_mrq(statisticsResponse.getStatistics()
//										.getValuations_metrics().getPrice_to_book_mrq());
//								stockStatistics.setPrice_to_sales_ttm(statisticsResponse.getStatistics()
//										.getValuations_metrics().getPrice_to_sales_ttm());
//								stockStatistics.setReturn_on_assets_ttm(
//										statisticsResponse.getStatistics().getFinancials().getReturn_on_assets_ttm());
//								stockStatistics.setReturn_on_equity_ttm(
//										statisticsResponse.getStatistics().getFinancials().getReturn_on_equity_ttm());
////								stockStatistics.setShares_outstanding(statisticsResponse.getStatistics()
////										.getStock_statistics().getShares_outstanding());
//								stockStatistics.setTrailing_annual_dividend_yield(statisticsResponse.getStatistics()
//										.getDividends_and_splits().getTrailing_annual_dividend_yield());
//								stockStatistics.setTrailing_pe(
//										statisticsResponse.getStatistics().getValuations_metrics().getTrailing_pe());
//								stockStatistics = stockStatisticsRepository.save(stockStatistics);
//								newDataCount++;
//								newCount++;
//								log.info(newDataCount + ": Stock Statistics saved successfully! | Symbol is - "
//										+ serverStock.getSymbol() + " | status - {}", Constant.OK);
//							}
//						} else {
//							log.error("Error in Stock Statistics : " + statistics_code);
//						}
//
//					} else {
//						log.error("Error in Api Usage : " + api_usage_code);
//					}
//				} else {
//					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				}
//			}
//			log.info("Total Stock Statistics Processed! " + "[ Updated : " + oldCount + " ] | " + "[ New added : "
//					+ newCount + " ]");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	/* --- End Statistics --- */

	/* --- Stock Recommendation --- */
	public void saveStockRecommendation() {
		List<Stock> list = getAllStocksFromServer();
		stockRecommendationDataProcess(list);
	}

	public void name() {

	}

	public void stockRecommendationDataProcess(List<Stock> list) {
		try {
			String recommendation_response = "", recommendation_rating_response = "";
			Integer oldDataCount = 0, notFoundCount = 0, newDataCount = 0;
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {
				recommendation_response = thirdPartyApiUtil.getAnalystRatings(serverStock);
				recommendation_rating_response = thirdPartyApiUtil.getRatings(serverStock);
				if (!recommendation_response.isBlank() && !recommendation_rating_response.isBlank()) {
					Type collectionType = new TypeToken<List<AnalystRecommendation>>() {
					}.getType();
					List<AnalystRecommendation> analystListObj = new Gson().fromJson(recommendation_response,
							collectionType);
					List<AnalystRecommendation> analystRating = new Gson().fromJson(recommendation_rating_response,
							collectionType);

					if (!analystListObj.isEmpty()) {
						AnalystRecommendation recommendation = new AnalystRecommendation();
						for (int i = 0; i <= 1; i++) {
							if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
								recommendation = analystListObj.get(i);
								recommendation
										.setRatingScore(!analystRating.isEmpty() ? analystRating.get(0).getRatingScore()
												: Constant.ZERO);
							} else {
								recommendation = analystListObj.get(i + 1);
								recommendation
										.setRatingScore(!analystRating.isEmpty() ? analystRating.get(0).getRatingScore()
												: Constant.ZERO);
//								recommendation.setRatingScore(analystRating.get(0).getRatingScore());
							}
							StockRecommendation stockRecommendation = recommendationRepository
									.findBySymbol(serverStock.getSymbol());
							/* updated analystRating data */
							if (stockRecommendation != null) {
								stockRecommendation.setCountry(serverStock.getCountry());
								stockRecommendation.setExchange(serverStock.getExchange());
								stockRecommendation.setSymbol(serverStock.getSymbol());
								stockRecommendation.setRating(recommendation.getRatingScore());
								stockRecommendation.setUpdationDate(new Date());
								stockRecommendation.setStatus(Constant.ONE);
								stockRecommendation.setReportedDate(recommendation.getDate());
								stockRecommendation.setBuy(recommendation.getAnalystRatingsbuy());
								stockRecommendation.setHold(recommendation.getAnalystRatingsHold());
								stockRecommendation.setSell(recommendation.getAnalystRatingsSell());
								recommendationRepository.save(stockRecommendation);
								oldDataCount++;
								log.info(oldDataCount + " : " + serverStock.getSymbol()
										+ " - Symbol Analyst recommendation data updated successfully! | status - {}",
										Constant.OK);
							} else {
								/* new analystRating data added */
								StockRecommendation recommendation2 = new StockRecommendation();
								recommendation2.setCountry(serverStock.getCountry());
								recommendation2.setExchange(serverStock.getExchange());
								recommendation2.setSymbol(serverStock.getSymbol());
								recommendation2.setRating(recommendation.getRatingScore());
								recommendation2.setCreationDate(new Date());
								recommendation2.setStatus(Constant.ONE);
								recommendation2.setReportedDate(recommendation.getDate());
								recommendation2.setBuy(recommendation.getAnalystRatingsbuy());
								recommendation2.setHold(recommendation.getAnalystRatingsHold());
								recommendation2.setSell(recommendation.getAnalystRatingsSell());
								recommendationRepository.save(recommendation2);
								newDataCount++;
								log.info(newDataCount + " : " + serverStock.getSymbol()
										+ " - Symbol Analyst recommendation data added successfully! | status - {}",
										Constant.OK);
							}
							break;
						}
					} else {
						notFoundCount++;
						log.info(
								notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
										+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}

			log.info("Total Analyst recommendation Data Processed! " + "[ Updated : " + oldCount + " ] | "
					+ "[ New added : " + newCount + " ]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* --- End Stock Recommendation --- */

	/* --- Stock Price Target --- */
	public void saveStockPriceTarget() {
		List<Stock> list = getAllStocksFromServer();
//		List<Stock> list = null;
		stockPriceTargetDataProcess(list);
	}

	public void stockPriceTargetDataProcess(List<Stock> list) {
		try {
			String price_target_response = "";
			Integer oldDataCount = 0, newDataCount = 0, notFoundCount = 0;
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {
				price_target_response = thirdPartyApiUtil.getPriceTarget(serverStock);
				if (!price_target_response.isBlank()) {
					Type collectionType = new TypeToken<List<PriceTargets>>() {
					}.getType();
					List<PriceTargets> priceTargetObj = new Gson().fromJson(price_target_response, collectionType);
					if (!priceTargetObj.isEmpty()) {
						for (PriceTargets targets : priceTargetObj) {
							StockPriceTarget priceTarget = priceTargetRepository.findBySymbol(serverStock.getSymbol());
							/* update price target data here */
							if (priceTarget != null) {
								priceTarget.setSymbol(serverStock.getSymbol());
								priceTarget.setCountry(serverStock.getCountry());
								priceTarget.setExchange(serverStock.getExchange());
								priceTarget.setUpdationDate(new Date());
								priceTarget.setHigh(targets.getTargetHigh());
								priceTarget.setLow(targets.getTargetLow());
								priceTarget.setMedian(targets.getTargetMedian());
								priceTarget.setAverage(targets.getTargetConsensus());// average price
								priceTargetRepository.save(priceTarget);
								oldDataCount++;
								log.info(
										oldDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Price target data updated successfully! | status - {}",
										Constant.OK);
							} else {
								/* new price target data here */
								StockPriceTarget stockPriceTarget = new StockPriceTarget();
								stockPriceTarget.setSymbol(serverStock.getSymbol());
								stockPriceTarget.setCountry(serverStock.getCountry());
								stockPriceTarget.setExchange(serverStock.getExchange());
								stockPriceTarget.setCreationDate(new Date());
								stockPriceTarget.setHigh(targets.getTargetHigh());
								stockPriceTarget.setLow(targets.getTargetLow());
								stockPriceTarget.setMedian(targets.getTargetMedian());
								stockPriceTarget.setAverage(targets.getTargetConsensus());// average price
								priceTargetRepository.save(stockPriceTarget);
								newDataCount++;
								log.info(
										newDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol Price target data added successfully! | status - {}",
										Constant.OK);
							}
						}
					} else {
						notFoundCount++;
						log.info(
								notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
										+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}

			}
			log.info("Total Stock Price Target Processed! " + "[ Updated : " + oldCount + " ] | " + "[ New added : "
					+ newCount + " ]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* --- End Stock Price Target --- */

	/* --- Share Holder Data --- */
	public void saveShareHolderData() {
		List<Stock> list = getAllStocksFromServer();
		shareHolderDataProcess(list);
	}

	public ShareHolder saveShareHolder(InstitutionalHolders institutionalHolders, Stock serverStock) {
		ShareHolder holder = new ShareHolder();
		holder.setPercent_held(institutionalHolders.getPercent_held());
		holder.setShares(institutionalHolders.getShares());
		holder.setShareHolderName(institutionalHolders.getHolder());
		holder.setSharesQtyChange(institutionalHolders.getChange());
		holder.setReportedDate(institutionalHolders.getDateReported());
		holder.setSymbol(serverStock.getSymbol());
		holder.setCountry(serverStock.getCountry());
		holder.setExchange(serverStock.getExchange());
		holder.setCreationDate(new Date());
		holder.setStatus(Constant.ONE);
		shareHolderRepository.save(holder);
		return holder;
	}

	public void shareHolderDataProcess(List<Stock> list) {
		try {
			String share_holder_response = "";
			Integer oldDataCount = 0, newDataCount = 0, deleteCount = 0, notFoundCount = 0;
			log.info("Searching the symbol for update it...");
			for (Stock serverStock : list) {
				share_holder_response = thirdPartyApiUtil.getShareHolders(serverStock);
				if (!share_holder_response.isBlank()) {
					Type collectionType = new TypeToken<List<InstitutionalHolders>>() {
					}.getType();
					List<InstitutionalHolders> stockListObj = new Gson().fromJson(share_holder_response,
							collectionType);
					try {
						stockListObj = new Gson().fromJson(share_holder_response, collectionType);
					} catch (JsonParseException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					} catch (JsonSyntaxException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					}
					if (!stockListObj.isEmpty()) {
						List<ShareHolder> tempShareHolderList = new ArrayList<>();
						List<ShareHolder> shareHolderList = shareHolderRepository
								.findBySymbolAndCountry(serverStock.getSymbol(), serverStock.getCountry());
						if (!shareHolderList.isEmpty()) {
							tempShareHolderList.addAll(shareHolderList);
							for (InstitutionalHolders institutionalHolders : stockListObj) {
								ShareHolder shareHolder = shareHolderList.stream().filter(
										holder -> holder.getShareHolderName().equals(institutionalHolders.getHolder()))
										.findAny().orElse(null);
								if (shareHolder != null) {
									shareHolder.setShareHolderName(institutionalHolders.getHolder());
									shareHolder.setPercent_held(institutionalHolders.getPercent_held());
									shareHolder.setShares(institutionalHolders.getShares());
									shareHolder.setSharesQtyChange(institutionalHolders.getChange());
									shareHolder.setReportedDate(institutionalHolders.getDateReported());
									shareHolder.setSymbol(serverStock.getSymbol());
									shareHolder.setCountry(serverStock.getCountry());
									shareHolder.setExchange(serverStock.getExchange());
									shareHolder.setUpdationDate(new Date());
									shareHolder.setStatus(Constant.ONE);
									shareHolderRepository.save(shareHolder);
									oldDataCount++;
//									oldCount++;
									tempShareHolderList.remove(shareHolder);
									log.info(
											oldDataCount + " : " + serverStock.getSymbol()
													+ " - Symbol Share holder updated successfully! | status - {}",
											Constant.OK);
								} else {
									saveShareHolder(institutionalHolders, serverStock);
									newDataCount++;
//									newCount++;
									log.info(
											newDataCount + " : " + serverStock.getSymbol()
													+ " - Symbol share holder saved successfully! | status - {}",
											Constant.OK);
								}
							}
							/* Remove share holder */
							List<ShareHolder> removedShareHolderList = new ArrayList<>();
							for (ShareHolder tempShareHolder : tempShareHolderList) {
								tempShareHolder.setStatus(Constant.ZERO);
								tempShareHolder.setUpdationDate(new Date());
								removedShareHolderList.add(tempShareHolder);
								deleteCount++;
							}
							shareHolderRepository.saveAll(removedShareHolderList);
							log.info(deleteCount + ": Share holder Removed successfully! | status - {}", Constant.OK);
						} else {
							for (InstitutionalHolders institutionalHolders : stockListObj) {
								saveShareHolder(institutionalHolders, serverStock);
								newDataCount++;
//								newCount++;
								log.info(
										newDataCount + " : " + serverStock.getSymbol()
												+ " - Symbol share holder saved successfully! | status - {}",
										Constant.OK);
							}
						}
					} else {
						notFoundCount++;
						log.info(
								notFoundCount + " : " + serverStock.getSymbol() + " - Symbol "
										+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
			log.info("Total Share Holder Data Processed! " + "[ Updated : " + oldDataCount + " ] | " + "[ New added : "
					+ newDataCount + " ]");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* --- End Share Holder Data --- */

	/* --- Key Executive Data --- */
	public void saveKeyExecutiveData() {
		List<Stock> list = getAllStocksFromServer();
		keyExecutiveDataProcess(list);
	}

	public KeyExecutive saveKeyExecutive(KeyExecutiveRes keyExecutiveRes, Stock serverStock) {
		KeyExecutive keyExecutive = new KeyExecutive();
		BeanUtils.copyProperties(keyExecutiveRes, keyExecutive);
		keyExecutive.setSymbol(serverStock.getSymbol());
		keyExecutive.setCountry(serverStock.getCountry());
		keyExecutive.setExchange(serverStock.getExchange());
		keyExecutive.setCreationDate(new Date());
		keyExecutiveRepository.save(keyExecutive);
		return keyExecutive;
	}

	public void keyExecutiveDataProcess(List<Stock> list) {
		try {
			String key_executive_response = "";
			Integer newDataCount = 0;
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			log.info("Searching the symbol for update it...");
			Long NotFoundCount = 0L;
			for (Stock serverStock : list) {
				key_executive_response = thirdPartyApiUtil.getKeyExecutive(serverStock);
				if (!key_executive_response.isEmpty()) {
					Type collectionType = new TypeToken<List<KeyExecutiveRes>>() {
					}.getType();
					List<KeyExecutiveRes> keyExecutiveResList = new Gson().fromJson(key_executive_response,
							collectionType);
					try {
						keyExecutiveResList = new Gson().fromJson(key_executive_response, collectionType);
					} catch (JsonParseException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					} catch (JsonSyntaxException e) {
						log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
					}
					if (!keyExecutiveResList.isEmpty()) {
						List<KeyExecutive> keyExecutiveList = keyExecutiveRepository
								.findBySymbolAndCountry(serverStock.getSymbol(), serverStock.getCountry());
						if (!keyExecutiveList.isEmpty()) {
							for (KeyExecutiveRes keyExecutiveRes : keyExecutiveResList) {
								saveKeyExecutive(keyExecutiveRes, serverStock);
								newDataCount++;
								newCount++;
								log.info(newDataCount + ": Key executive saved successfully! | Symbol is - "
										+ serverStock.getSymbol() + " | status - {}", Constant.OK);
							}
						} else {
							for (KeyExecutiveRes keyExecutiveRes : keyExecutiveResList) {
								saveKeyExecutive(keyExecutiveRes, serverStock);
								newDataCount++;
								newCount++;
								log.info(newDataCount + ": Key executive saved successfully! | Symbol is - "
										+ serverStock.getSymbol() + " | status - {}", Constant.OK);
							}
						}
					} else {
						NotFoundCount++;
						log.info(NotFoundCount + " : " + serverStock.getSymbol() + " - Symbol Not found"
								+ "! status - {}", Constant.NOT_FOUND);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
			log.info("Total Key Executive Data Processed! " + "[ Updated : " + oldCount + " ] | " + "[ New added : "
					+ newCount + " ]");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* End Key Executive */

	/* --- Top Gainer List updating --- */
	public void saveStockGainer(List<MoversResponse> moversResponseList) {
		Integer count = 0;
		for (MoversResponse movers : moversResponseList) {
			StockGainer stockGainer = new StockGainer();
			BeanUtils.copyProperties(movers, stockGainer);

			Stock stock = stockRepository.findBySymbol(movers.getSymbol());
			if (stock != null) {
				stockGainer.setClose(movers.getPrice());
				stockGainer.setPriceChange(movers.getChange());
				stockGainer.setPercentChange(movers.getChangesPercentage());
				stockGainer.setCountry(stock.getCountry());
				stockGainer.setCurrency(stock.getCurrency());

				stockGainer.setLogo(stock.getStockProfile() != null ? stock.getStockProfile().getLogo() : "");
				stockGainer.setExchange(stock.getExchange());
				stockGainer.setCreationDate(new Date());
				this.gainerRepository.save(stockGainer);
				count++;
				log.info(count + " : " + movers.getSymbol() + " - Symbol saved successfully! status - {}", Constant.OK);
			} else {
				log.info(movers.getSymbol() + " - Symbol not found! status - {}", Constant.NOT_FOUND);
			}

		}
	}

	public void saveStockLoser(List<MoversResponse> moversResponseList) {
		Integer count = 0;
		for (MoversResponse movers : moversResponseList) {
			StockLoser stockLoser = new StockLoser();
			BeanUtils.copyProperties(movers, stockLoser);
			Stock stock = stockRepository.findBySymbol(movers.getSymbol());
			if (stock != null) {
				stockLoser.setClose(movers.getPrice());
				stockLoser.setPriceChange(movers.getChange());
				stockLoser.setPercentChange(movers.getChangesPercentage());
				stockLoser.setExchange(stock.getExchange());
				stockLoser.setCountry(stock.getCountry());
				stockLoser.setCurrency(stock.getCurrency());
				stockLoser.setLogo(stock.getStockProfile() != null ? stock.getStockProfile().getLogo() : "");
				stockLoser.setCreationDate(new Date());
				loserRepository.save(stockLoser);
				count++;
				log.info(count + " : " + stockLoser.getSymbol() + " - Symbol saved successfully! status - {}",
						Constant.OK);
			} else {
				log.info(movers.getSymbol() + " - Symbol not found! status - {}", Constant.NOT_FOUND);
			}
		}
	}

	public class GainerLoserTask implements Runnable {
		private List<Country> list;

		public GainerLoserTask(List<Country> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			stockGainer(list);
			stockLoser(list);
		}
	}

	public void gainerLoserThread(List<Country> countryList) {
		GainerLoserTask gainerLoserTask = new GainerLoserTask(countryList);
		Thread thread = new Thread(gainerLoserTask);
		thread.start();
	}

	public void stockGainer(List<Country> countryList) {
		try {
			for (Country country : countryList) {
				if (country != null) {
					String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
					log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
							+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
							+ country.getMarketCloseTime() + " ]");
					Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
					Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
					Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());

					/* check is market open */
					if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
						String apiResponse = "";
						apiResponse = thirdPartyApiUtil.getTopGainer(country.getCountry());
						if (!apiResponse.isBlank()) {
							Type collectionType = new TypeToken<List<MoversResponse>>() {
							}.getType();
							List<MoversResponse> moversResponseList = new Gson().fromJson(apiResponse, collectionType);
							if (moversResponseList.size() > 0) {
								log.info("Record found! status - {}", moversResponseList);

								gainerRepository.deleteByCountry(country.getCountry());
								log.info(
										country.getCountry().toUpperCase()
												+ " - All old data removed from stock gainer list ! status - {}",
										Constant.OK);

								saveStockGainer(moversResponseList);
								log.info(
										country.getCountry().toUpperCase()
												+ " - Stock Gainer list updated successfully! status - {}",
										Constant.OK);
							} else {
								log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
										Constant.NOT_FOUND);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					} else {
						log.info("Waiting... To Open The " + country.getCountry().toUpperCase()
								+ " Market.......... ! status - {}");
					}
				} else {
					log.info("Country not found : status - {}", Constant.NOT_FOUND);
				}
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

//	public class LoserTask implements Runnable {
//		private List<Country> list;
//
//		public LoserTask(List<Country> list) {
//			super();
//			this.list = list;
//		}
//
//		@Override
//		public void run() {
//			stockLoser(list);
//		}
//	}
//
//	public void loserThread(List<Country> countryList) {
//		LoserTask moverTask = new LoserTask(countryList);
//		Thread thread = new Thread(moverTask);
//		thread.start();
//	}

	public void stockLoser(List<Country> countryList) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			for (Country country : countryList) {
				if (country != null) {
					String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
					log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
							+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
							+ country.getMarketCloseTime() + " ]");
					Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
					Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
					Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());

					/* check is market open */
					if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
						String apiResponse = "";
						apiResponse = thirdPartyApiUtil.getTopLooser(country.getCountry());
						if (!apiResponse.isBlank()) {
							Type collectionType = new TypeToken<List<MoversResponse>>() {
							}.getType();
							List<MoversResponse> moversResponseList = new Gson().fromJson(apiResponse, collectionType);
							if (moversResponseList.size() > 0) {
								log.info("Record found! status - {}", moversResponseList);

								loserRepository.deleteByCountry(country.getCountry());
								log.info(
										country.getCountry().toUpperCase()
												+ " - All old data removed from stock loser list ! status - {}",
										Constant.OK);

								saveStockLoser(moversResponseList);
								log.info(country.getCountry().toUpperCase()
										+ " - Stock Loser list updated successfully! status - {}", Constant.OK);
							} else {
								log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
										Constant.NOT_FOUND);
							}
						} else {
							log.info(country.getCountry().toUpperCase()
									+ " - Record not saved, because not getting data from third party service! status - {}",
									Constant.NOT_FOUND);
						}
					} else {
						log.info("Waiting... To Open The " + country.getCountry().toUpperCase()
								+ " Market.......... ! status - {}", Constant.NOT_FOUND);
					}
				} else {
					log.error("Country not found : status - {}", Constant.NOT_FOUND);
				}
			}
		} catch (

		Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	/* Store Indices Market data */
	public class IndicesTask implements Runnable {
		private List<Country> list;

		public IndicesTask(List<Country> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			saveIndicesMarketData(list);
		}
	}

	public void indicesThread(List<Country> countryList) {
		IndicesTask gainerTask = new IndicesTask(countryList);
		Thread thread = new Thread(gainerTask);
		thread.start();
	}

	public void saveIndicesMarketData(List<Country> countryList) {
		try {
			String apiResponse = "";
			Integer count = 0;
			for (Country country : countryList) {
				if (country != null) {
					String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
					log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
							+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
							+ country.getMarketCloseTime() + " ]");
					Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
					Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
					Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());

					/* check is market open */
					if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
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
								List<Indices> indicesList = indicesRepository
										.findByCountryAndStatus(country.getCountry(), Constant.ONE);
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
												log.info(count + ": Indices have updated! | Symbol is - "
														+ indices.getSymbol() + " | status - {}", Constant.OK);
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
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					} else {
						log.info("WAITING... To Open The " + country.getCountry().toUpperCase() + " Market...");
					}
				} else {
					log.error("Country not found : status - {}", Constant.NOT_FOUND);
				}
			}
		} catch (

		Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}
	/* --- End Indices Task--- */

//	public void saveUSAStockGainer() {
//		try {
//			String apiResponse = "";
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			apiResponse = thirdPartyApiUtil.getUSATopGainer();
//			if (!apiResponse.isBlank()) {
//				Integer count = 0, mover_code;
//				String mktOpenTime = "09:30 AM";// 7 p.m. – 1:30 a.m. in IST (GMT-4)
//				String mktcloseTime = "04:00 PM";
//				ZoneId zoneId = ZoneId.of("America/New_York");
//
//				String curentTime = DateUtil.getTimeInZoneWise(zoneId);
//				log.info("USA Market - [ Time now : " + curentTime + " | Open at : " + mktOpenTime + " | Close at : "
//						+ mktcloseTime + " ]");
//				Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
//				Long mktcloseLongTime = DateUtil.StringTimeToLongTime(mktcloseTime);
//				Long mktOpenLongTime = DateUtil.StringTimeToLongTime(mktOpenTime);
//
//				if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
////						gainerRepository.truncateTable();
//					gainerRepository.deleteByCountry(Constant.UNITED_STATES);
//					log.info("USA - All old data removed from stock gainer list ! status - {}", Constant.OK);
//
//					MoversResponse moversResponseList = new MoversResponse();
//					Map<?, ?> map_mover_response = mapper.readValue(apiResponse, Map.class);
//
//					mover_code = (Integer) map_mover_response.get("code");
//					if (mover_code == null) {
//						moversResponseList = mapper.convertValue(map_mover_response, MoversResponse.class);
//						if (moversResponseList.getValues().size() > 0) {
//							log.info("Record found! status - {}", moversResponseList);
//
//							for (MoversDetails movers : moversResponseList.getValues()) {
//								StockGainer stockGainer = new StockGainer();
//								BeanUtils.copyProperties(movers, stockGainer);
//
////								StockLogo logo = null;
////								logo_response = thirdPartyApiUtil.getLogo(movers.getSymbol(), Constant.UNITED_STATES);
////								Map<?, ?> map_logo_response = mapper.readValue(logo_response, Map.class);
////								logo_code = (Integer) map_logo_response.get("code");
////								if (logo_code == null) {
//////									log.info("Logo found! status - {}", map_logo_response);
////									logo = mapper.convertValue(map_logo_response, StockLogo.class);
////									stockGainer.setLogo(logo.getUrl());
//////									log.info("From Map to Required Object : " + logo);
////								} else {
////									log.error("Error in Logo : " + logo_code);
////								}
//
//								stockGainer.setClose(movers.getLast());
//								stockGainer.setPriceChange(movers.getChange());
//								stockGainer.setPercentChange(movers.getPercent_change());
//								stockGainer.setCountry(Constant.UNITED_STATES);
//								stockGainer.setCreationDate(new Date());
//
//								gainerRepository.save(stockGainer);
//								count++;
//								log.info(count + " : " + stockGainer.getSymbol()
//										+ " - Symbol saved successfully! status - {}", Constant.OK);
//							}
//							log.info("USA - Stock Gainer list updated successfully! status - {}", Constant.OK);
//						} else {
//							log.info("Record not saved, because not getting data from third party service! status - {}",
//									Constant.NOT_FOUND);
//						}
//					} else {
//						log.error("Error in Stock Gainer : " + mover_code);
//					}
//				} else {
//					log.info("Waiting... To Open The United Stock Market.......... ! status - {}", Constant.NOT_FOUND);
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//	}
//
//	public void saveUSAStockLoser() {
//		try {
//			String apiResponse = "";
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			apiResponse = thirdPartyApiUtil.getUSATopLoser();
//			if (!apiResponse.isBlank()) {
//				Integer count = 0, mover_code;
//				String mktOpenTime = "09:30 AM";// 7 p.m. – 1:30 a.m. in IST
//				String mktcloseTime = "04:00 PM";
//				ZoneId zoneId = ZoneId.of("America/New_York");
//
//				String curentTime = DateUtil.getTimeInZoneWise(zoneId);
//				log.info("USA Market - [ Time now : " + curentTime + " | Open at : " + mktOpenTime + " | Close at : "
//						+ mktcloseTime + " ]");
//				Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
//				Long mktcloseLongTime = DateUtil.StringTimeToLongTime(mktcloseTime);
//				Long mktOpenLongTime = DateUtil.StringTimeToLongTime(mktOpenTime);
//
//				if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
////						loserRepository.truncateTable();
//					loserRepository.deleteByCountry(Constant.UNITED_STATES);
//					log.info("USA -  All old data removed from stock loser list ! status - {}", Constant.OK);
//
//					MoversResponse moversResponseList = new MoversResponse();
//					Map<?, ?> map_mover_response = mapper.readValue(apiResponse, Map.class);
//
//					mover_code = (Integer) map_mover_response.get("code");
//					if (mover_code == null) {
//						moversResponseList = mapper.convertValue(map_mover_response, MoversResponse.class);
//						if (moversResponseList.getValues().size() > 0) {
//							log.info("Record found! status - {}", moversResponseList);
//
//							for (MoversDetails movers : moversResponseList.getValues()) {
//								StockLoser stockLoser = new StockLoser();
//								BeanUtils.copyProperties(movers, stockLoser);
//								stockLoser.setClose(movers.getLast());
//								stockLoser.setPriceChange(movers.getChange());
//								stockLoser.setPercentChange(movers.getPercent_change());
//								stockLoser.setCountry(Constant.UNITED_STATES);
//								stockLoser.setCreationDate(new Date());
//								loserRepository.save(stockLoser);
//								count++;
//								log.info(count + " : " + stockLoser.getSymbol()
//										+ " - Symbol saved successfully! status - {}", Constant.OK);
//							}
//							log.info("USA - Stock Loser list updated successfully! status - {}", Constant.OK);
//						} else {
//							log.info("Record not saved, because not getting data from third party service! status - {}",
//									Constant.NOT_FOUND);
//						}
//					} else {
//						log.error("Error in Stock Loser : " + mover_code);
//					}
//				} else {
//					log.info("Waiting... To Open The United Stock Market.......... ! status - {}", Constant.NOT_FOUND);
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//	}

	/* Store Explore Market data */
	public void saveDividendMarketData() {
		try {
			int newDataCount = 0, oldDataCount = 0;
			String apiResponse = "";
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			apiResponse = thirdPartyApiUtil.getDividend();
			if (!apiResponse.isBlank()) {
				Type collectionType = new TypeToken<List<ExploreResponse>>() {
				}.getType();
				List<ExploreResponse> exploreResponseList = new Gson().fromJson(apiResponse, collectionType);
				log.info("Dividend list size :- " + exploreResponseList.size());
				if (exploreResponseList.size() > 0) {
					for (ExploreResponse exploreResponse : exploreResponseList) {
						Dividend dividend = new Dividend();
						// get company name
						Stock stock = stockRepository.findBySymbol(exploreResponse.getSymbol());
						if (stock != null) {
							dividend.setCompanyName(stock.getName());
						}
						// get dividend data
						Dividend old_dividend = dividendRepository.findBySymbolAndDate(exploreResponse.getSymbol(),
								exploreResponse.getDate());
						/*-------------- update here ------------*/
						if (old_dividend != null) {
							old_dividend.setDividendAmount(exploreResponse.getDividend());
							old_dividend.setUpdationDate(new Date());
							dividendRepository.save(old_dividend);
							oldDataCount++;
							log.info(oldDataCount + " dividend data updated successfully !! " + "symbol is - "
									+ old_dividend.getSymbol() + " status -{} " + Constant.OK);
						} else {
							/*------------ add here ------------*/
							dividend.setSymbol(exploreResponse.getSymbol());
							dividend.setDividendAmount(exploreResponse.getDividend());
							dividend.setDate(exploreResponse.getDate());
							dividend.setStatus(Constant.ONE);
							dividend.setCreationDate(new Date());
							dividendRepository.save(dividend);
							newDataCount++;
							log.info(newDataCount + " new dividend data saved successfully !! " + "symbol is - "
									+ dividend.getSymbol() + " status - {} " + Constant.OK);
						}
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	/* Store Explore Market data */
	public void saveEarningMarketData() {
		try {
			int newDataCount = 0, oldDataCount = 0;
			String apiResponse = "";
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			apiResponse = thirdPartyApiUtil.getEarning();
			if (!apiResponse.isBlank()) {
				Type collectionType = new TypeToken<List<ExploreResponse>>() {
				}.getType();
				List<ExploreResponse> exploreResponseList = new Gson().fromJson(apiResponse, collectionType);
				log.info("Earning list size :- " + exploreResponseList.size());
				if (exploreResponseList.size() > 0) {
					for (ExploreResponse exploreResponse : exploreResponseList) {
						// get company name
						Earning earning = new Earning();
						Stock stock = stockRepository.findBySymbol(exploreResponse.getSymbol());
						if (stock != null) {
							earning.setCompanyName(stock.getName());
						}
						Earning old_earning = earningRepository.findBySymbolAndDate(exploreResponse.getSymbol(),
								exploreResponse.getDate());
						if (old_earning != null) {
							old_earning.setDate(exploreResponse.getDate());
							old_earning.setEps(exploreResponse.getEps());
							old_earning.setEpsEstimated(exploreResponse.getEpsEstimated());
							old_earning.setUpdatedFromDate(exploreResponse.getUpdatedFromDate());
							old_earning.setUpdationDate(new Date());
							earningRepository.save(old_earning);
							oldDataCount++;
							log.info(oldDataCount + " new earning data saved successfully !! " + "symbol is - "
									+ old_earning.getSymbol() + " status - {} " + Constant.OK);

						} else {
							earning.setSymbol(exploreResponse.getSymbol());
							earning.setStatus(Constant.ONE);
							earning.setCreationDate(new Date());
							earning.setDate(exploreResponse.getDate());
							earning.setEps(exploreResponse.getEps());
							earning.setEpsEstimated(exploreResponse.getEpsEstimated());
							earning.setUpdatedFromDate(exploreResponse.getUpdatedFromDate());
							earningRepository.save(earning);
							newDataCount++;
							log.info(newDataCount + " new earning data saved successfully !! " + "symbol is - "
									+ exploreResponse.getSymbol() + " status - {} " + Constant.OK);
						}
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	/* Store Explore Market data */
	public void saveEconomicMarketData() {
		try {
			int newDataCount = 0, oldDataCount = 0;
			String apiResponse = "";
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			apiResponse = thirdPartyApiUtil.getEconomic();
			if (!apiResponse.isBlank()) {
				Type collectionType = new TypeToken<List<ExploreResponse>>() {
				}.getType();
				List<ExploreResponse> exploreResponseList = new Gson().fromJson(apiResponse, collectionType);
				log.info("Economic list size :- " + exploreResponseList.size());
				if (exploreResponseList.size() > 0) {
					for (ExploreResponse exploreResponse : exploreResponseList) {
						// get company name
						Economic earning = new Economic();
						/*------------- save US economic data ---------------*/
						if (exploreResponse.getCountry().equals("US")) {
							Economic us_old_economic = economicRepository
									.findByDateAndCountry(exploreResponse.getDate(), exploreResponse.getCountry());
							/*---------- update here ----------*/
							if (us_old_economic != null) {
								us_old_economic.setCountry(exploreResponse.getCountry());
								us_old_economic.setDate(exploreResponse.getDate());
								us_old_economic.setEstimate(exploreResponse.getEstimate());
								us_old_economic.setEvent(exploreResponse.getEvent());
								us_old_economic.setUpdationDate(new Date());
								economicRepository.save(us_old_economic);
								oldDataCount++;
								log.info(oldDataCount + " new US economic data updated successfully !! "
										+ "country is - " + exploreResponse.getCountry() + " status - {} "
										+ Constant.OK);
							} else {
								/*----------- add here ------------*/
								earning.setCountry(exploreResponse.getCountry());
								earning.setDate(exploreResponse.getDate());
								earning.setEstimate(exploreResponse.getEstimate());
								earning.setEvent(exploreResponse.getEvent());
								earning.setCreationDate(new Date());
								earning.setStatus(Constant.ONE);
								economicRepository.save(earning);
								newDataCount++;
								log.info(newDataCount + " new US economic data saved successfully !! " + "country is - "
										+ exploreResponse.getCountry() + " status - {} " + Constant.OK);
							}
							/*------------- save SA economic data ---------------*/
						} else if (exploreResponse.getCountry().equals("SA")) {
							Economic sa_old_economic = economicRepository
									.findByDateAndCountry(exploreResponse.getDate(), exploreResponse.getCountry());
							/*---------- update here ----------*/
							if (sa_old_economic != null) {
								sa_old_economic.setCountry(exploreResponse.getCountry());
								sa_old_economic.setDate(exploreResponse.getDate());
								sa_old_economic.setEstimate(exploreResponse.getEstimate());
								sa_old_economic.setEvent(exploreResponse.getEvent());
								sa_old_economic.setUpdationDate(new Date());
								economicRepository.save(sa_old_economic);
								oldDataCount++;
								log.info(oldDataCount + " new SA economic data updated successfully !! "
										+ "country is - " + exploreResponse.getCountry() + " status - {} "
										+ Constant.OK);
							} else {
								/*---------- add here ----------*/
								earning.setCountry(exploreResponse.getCountry());
								earning.setDate(exploreResponse.getDate());
								earning.setEstimate(exploreResponse.getEstimate());
								earning.setEvent(exploreResponse.getEvent());
								earning.setCreationDate(new Date());
								earning.setStatus(Constant.ONE);
								economicRepository.save(earning);
								newDataCount++;
								log.info(newDataCount + " new SA economic data saved successfully !! " + "country is - "
										+ exploreResponse.getCountry() + " status - {} " + Constant.OK);
							}
						} else {
							log.info(" Getting data another country - " + exploreResponse.getCountry());
						}
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	/* Store Explore Market data */
	public void saveIposMarketData() {
		try {
			int newDataCount = 0, oldDataCount = 0;
			String apiResponse = "";
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			apiResponse = thirdPartyApiUtil.getIpos();
			if (!apiResponse.isBlank()) {
				Type collectionType = new TypeToken<List<ExploreResponse>>() {
				}.getType();
				List<ExploreResponse> exploreResponseList = new Gson().fromJson(apiResponse, collectionType);
				log.info("IPOS list size :- " + exploreResponseList.size());
				if (exploreResponseList.size() > 0) {
					for (ExploreResponse exploreResponse : exploreResponseList) {
						Ipos ipos = new Ipos();
						Ipos old_ipos = iposRepository.findBySymbolAndDate(exploreResponse.getSymbol(),
								exploreResponse.getDate());
						/*------------ update here -----------*/
						if (old_ipos != null) {
							old_ipos.setCompanyName(exploreResponse.getCompany());
							old_ipos.setDate(exploreResponse.getDate());
							old_ipos.setPriceRange(exploreResponse.getPriceRange());
							old_ipos.setShares(exploreResponse.getShares());
							old_ipos.setExchange(exploreResponse.getExchange());
							old_ipos.setUpdationDate(new Date());
							iposRepository.save(old_ipos);
							oldDataCount++;
							log.info(oldDataCount + " new IPOS data updated successfully !! " + "symbol is - "
									+ exploreResponse.getSymbol() + " status - {} " + Constant.OK);
						} else {
							/*------------ add here -------------*/
							ipos.setSymbol(exploreResponse.getSymbol());
							ipos.setCompanyName(exploreResponse.getCompany());
							ipos.setDate(exploreResponse.getDate());
							ipos.setPriceRange(exploreResponse.getPriceRange());
							ipos.setShares(exploreResponse.getShares());
							ipos.setExchange(exploreResponse.getExchange());
							ipos.setCreationDate(new Date());
							ipos.setStatus(Constant.ONE);
							iposRepository.save(ipos);
							newDataCount++;
							log.info(newDataCount + " new IPOS data saved successfully !! " + "symbol is - "
									+ exploreResponse.getSymbol() + " status - {} " + Constant.OK);
						}
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
	}

	/* --- SEC Filing updating --- */
	public void secFilingThread() {
		List<Stock> list = getAllStocksFromServer();
		SecFilingTask secFilingTask = new SecFilingTask(list);
		Thread thread = new Thread(secFilingTask);
		thread.start();
	}

	private class SecFilingTask implements Runnable {
		private List<Stock> list;

		public SecFilingTask(List<Stock> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			SaveSecFilingDataProcess(list);
		}
	}

	public void SaveSecFilingDataProcess(List<Stock> list) {
		try {
			String sec_filing_response = "";
			Integer newDataCount = 0;
			log.info("Searching the symbol for update it...");
			String[] filingType = { "10-k", "10-q", "8-k" };
			for (int i = 0; i < filingType.length; i++) {
				for (Stock serverStock : list) {
					Stock stock = stockRepository.findBySymbol(serverStock.getSymbol());
					if (stock != null) {
						sec_filing_response = thirdPartyApiUtil.getSecFilingData(stock, filingType[i]);
						if (!sec_filing_response.isEmpty()) {
							List<SecFilingResponse> serverSecFilingObj = new ArrayList<>();
							try {
								Type collectionType = new TypeToken<List<SecFilingResponse>>() {
								}.getType();
								serverSecFilingObj = new Gson().fromJson(sec_filing_response, collectionType);
							} catch (JsonParseException e) {
								log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
							} catch (JsonSyntaxException e) {
								log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
							}
							if (serverSecFilingObj != null && !serverSecFilingObj.isEmpty()) {
								DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
								LocalDate localDate = null;
								Map<String, List<SecFilingResponse>> mapList = serverSecFilingObj.stream()
										.collect(Collectors.groupingBy(SecFilingResponse::getType));
								log.info(mapList + " status - {} " + Constant.OK);
								List<SecFilingResponse> serverSecFilingList = null;
								for (Map.Entry<String, List<SecFilingResponse>> entry : mapList.entrySet()) {
									if (entry.getKey().equalsIgnoreCase(filingType[i])) {
										serverSecFilingList = entry.getValue();
									}
								}
								if (serverSecFilingList != null && !serverSecFilingList.isEmpty()) {
									List<SecFiling> secFilingList = secFilingRepository
											.findBySymbolAndFilingType(serverStock.getSymbol(), filingType[i]);
									if (secFilingList.isEmpty()) {
										for (SecFilingResponse secRes : serverSecFilingList) {
											Date date = dateFormat.parse(secRes.getFillingDate());
											String dateStr = DateUtil.convertDateToStringDate(date);
											localDate = LocalDate.parse(dateStr, dateTimeFormatter);
											SecFiling secFiling = new SecFiling();
											BeanUtils.copyProperties(secRes, secFiling);
											secFiling.setFilingType(secRes.getType());
											secFiling.setAcceptedDate(secRes.getAcceptedDate());
											secFiling.setFillingDate(secRes.getFillingDate());
											secFiling.setCountry(stock.getCountry());
											secFiling.setCreationDate(new Date());
											secFiling.setYear(localDate.getYear());
											secFilingRepository.save(secFiling);
											newCount++;
											newDataCount++;
											log.info(newDataCount
													+ ": Sec Filing data saved successfully! | Symbol is - "
													+ serverStock.getSymbol() + " | status - {}", Constant.OK);
										}
									} else {
										if (serverSecFilingList.size() > secFilingList.size()) {
											for (int j = 0; j < serverSecFilingList.size(); j++) {
												String filingDate = serverSecFilingList.get(j).getFillingDate();
												String acceptedDate = serverSecFilingList.get(j).getAcceptedDate();
												Boolean isPresent = secFilingList.stream()
														.anyMatch(data -> data.getFillingDate().equals(filingDate)
																&& data.getAcceptedDate().equals(acceptedDate));
												if (isPresent == false) {
													Date date = dateFormat
															.parse(serverSecFilingList.get(j).getFillingDate());
													String dateStr = DateUtil.convertDateToStringDate(date);
													localDate = LocalDate.parse(dateStr, dateTimeFormatter);
													SecFiling secFiling = new SecFiling();
													BeanUtils.copyProperties(serverSecFilingList.get(j), secFiling);
													secFiling.setFilingType(serverSecFilingList.get(j).getType());
													secFiling.setAcceptedDate(
															serverSecFilingList.get(j).getAcceptedDate());
													secFiling.setFillingDate(
															serverSecFilingList.get(j).getFillingDate());
													secFiling.setCountry(stock.getCountry());
													secFiling.setCreationDate(new Date());
													secFiling.setYear(localDate.getYear());
													secFilingRepository.save(secFiling);
													newCount++;
													newDataCount++;
													log.info(newDataCount
															+ ": New SEC Filing data saved successfully! | Symbol is - "
															+ serverStock.getSymbol() + " | status - {}", Constant.OK);
												}
											}
										}
									}
								}
							} else {
								log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
										Constant.NOT_EXIST);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.NOT_FOUND);
					}
				}
			}
			log.info("Total SEC Filing Processed! " + "[ New added : " + newCount + " ]");
			oldCount = 0L;
			newCount = 0L;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void SaveSecFilingDataProcess(List<Stock> list) {
//		try {
//			String sec_filing_response = "";
//			Integer newDataCount = 0;
//			log.info("Searching the symbol for update it...");
//			String[] filingType = { "10-k", "10-q", "8-k" };
//			for (int i = 0; i < filingType.length; i++) {
//				for (Stock serverStock : list) {
//					Stock stock = stockRepository.findBySymbol(serverStock.getSymbol());
//					if (stock != null) {
//						sec_filing_response = thirdPartyApiUtil.getSecFilingData(stock, filingType[i]);
//						if (!sec_filing_response.isEmpty()) {
//							List<SecFilingResponse> serverSecFilingObj = new ArrayList<>();
//							try {
//								Type collectionType = new TypeToken<List<SecFilingResponse>>() {
//								}.getType();
//								serverSecFilingObj = new Gson().fromJson(sec_filing_response, collectionType);
//							} catch (JsonParseException e) {
//								log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
//							} catch (JsonSyntaxException e) {
//								log.error(e.getMessage() + " ! status - {}", Constant.SERVER_ERROR);
//							}
//							if (serverSecFilingObj != null && !serverSecFilingObj.isEmpty()) {
//								DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//								DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//								LocalDate localDate = null;
//								Map<String, List<SecFilingResponse>> mapList = serverSecFilingObj.stream()
//										.collect(Collectors.groupingBy(SecFilingResponse::getType));
//								log.info(mapList + " status - {} " + Constant.OK);
//								List<SecFilingResponse> serverSecFilingList = null;
//								for (Map.Entry<String, List<SecFilingResponse>> entry : mapList.entrySet()) {
//									if (entry.getKey().equalsIgnoreCase(filingType[i])) {
//										serverSecFilingList = entry.getValue();
//									}
//								} else {
//									if (serverSecFilingObj.size() > secFilingList.size()) {
//										Integer count = 0;
//										for (int j = 0; j < serverSecFilingObj.size(); j++) {
//											for (int k = j; k < secFilingList.size(); k++) {
//												String filingDate = DateUtil.convertDateToStringDateTime(
//														secFilingList.get(k).getFillingDate());
//												String acceptedDate = DateUtil.convertDateToStringDateTime(
//														secFilingList.get(k).getAcceptedDate());
//												if (serverSecFilingObj.get(j).getFillingDate().contains(filingDate)
//														&& serverSecFilingObj.get(j).getAcceptedDate()
//																.contains(acceptedDate)) {
//													count++;
//													break;
//												}
//											}
//											if (secFilingList.size() == count) {
//												break;
//											}
//										}
//										for (int j = count; j < serverSecFilingObj.size(); j++) {
//											Date date = dateFormat.parse(serverSecFilingObj.get(j).getFillingDate());
//								}
//								if (serverSecFilingList != null && !serverSecFilingList.isEmpty()) {
//									List<SecFiling> secFilingList = secFilingRepository
//											.findBySymbolAndFilingType(serverStock.getSymbol(), filingType[i]);
//									if (secFilingList.isEmpty()) {
//										for (SecFilingResponse secRes : serverSecFilingList) {
//											Date date = dateFormat.parse(secRes.getFillingDate());
//											String dateStr = DateUtil.convertDateToStringDate(date);
//											localDate = LocalDate.parse(dateStr, dateTimeFormatter);
//											SecFiling secFiling = new SecFiling();
//											BeanUtils.copyProperties(secRes, secFiling);
//											secFiling.setFilingType(secRes.getType());
//											secFiling.setAcceptedDate(secRes.getAcceptedDate());
//											secFiling.setFillingDate(secRes.getFillingDate());
//											secFiling.setCountry(stock.getCountry());
//											secFiling.setCreationDate(new Date());
//											secFiling.setYear(localDate.getYear());
//											secFilingRepository.save(secFiling);
//											newCount++;
//											newDataCount++;
//											log.info(newDataCount
//													+ ": Sec Filing data saved successfully! | Symbol is - "
//													+ serverStock.getSymbol() + " | status - {}", Constant.OK);
//										}
//									} else {
//										if (serverSecFilingList.size() > secFilingList.size()) {
//											for (int j = 0; j < serverSecFilingList.size(); j++) {
//												String filingDate = serverSecFilingList.get(j).getFillingDate();
//												String acceptedDate = serverSecFilingList.get(j).getAcceptedDate();
//												Boolean isPresent = secFilingList.stream()
//														.anyMatch(data -> data.getFillingDate().equals(filingDate)
//																&& data.getAcceptedDate().equals(acceptedDate));
//												if (isPresent == false) {
//													Date date = dateFormat
//															.parse(serverSecFilingList.get(j).getFillingDate());
//													String dateStr = DateUtil.convertDateToStringDate(date);
//													localDate = LocalDate.parse(dateStr, dateTimeFormatter);
//													SecFiling secFiling = new SecFiling();
//													BeanUtils.copyProperties(serverSecFilingList.get(j), secFiling);
//													secFiling.setFilingType(serverSecFilingList.get(j).getType());
//													secFiling.setAcceptedDate(
//															serverSecFilingList.get(j).getAcceptedDate());
//													secFiling.setFillingDate(
//															serverSecFilingList.get(j).getFillingDate());
//													secFiling.setCountry(stock.getCountry());
//													secFiling.setCreationDate(new Date());
//													secFiling.setYear(localDate.getYear());
//													secFilingRepository.save(secFiling);
//													newCount++;
//													newDataCount++;
//													log.info(newDataCount
//															+ ": New SEC Filing data saved successfully! | Symbol is - "
//															+ serverStock.getSymbol() + " | status - {}", Constant.OK);
//												}
//											}
//										}
//									}
//								}
//							} else {
//								log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
//										Constant.NOT_EXIST);
//							}
//						} else {
//							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//									Constant.SERVER_ERROR);
//						}
//					} else {
//						log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.NOT_FOUND);
//					}
//				}
//			}
//			log.info("Total SEC Filing Processed! " + "[ New added : " + newCount + " ]");
//			oldCount = 0L;
//			newCount = 0L;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	/* --- End SEC Filing --- */

	/* -------- save stock chart data ---------- */
	public void saveStockYearGraphData() {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
			List<Country> countryList = countryRepository.findAllCountries();
			if (countryList != null && !countryList.isEmpty()) {
				log.info("Country list found successfully !!. list size:- " + countryList.size());
				for (Country country : countryList) {
					List<Stock> stockList = stockRepository.findByCountryAndStatus(country.getCountry(), Constant.ONE);
					if (stockList != null && !stockList.isEmpty()) {
						log.info("Stock list found successfully !!. list size:- " + stockList.size());
						for (Stock stock : stockList) {
							apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
									Constant.ONE_YEAR);
							if (!apiResponse.isBlank()) {
								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
								try {
									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
									}.getType();
									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
								} catch (Exception e) {
									log.error("Exception :- " + e.getMessage());
								}
								if (!timeSeriesDetailsList.isEmpty()) {
									List<YearGraph> oldYearGraphList = yearGraphRepository
											.findBySymbol(stock.getSymbol());
									if (oldYearGraphList != null && !oldYearGraphList.isEmpty()) {
										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
											YearGraph oldYearGraph = yearGraphRepository.findBySymbolAndDate(
													stock.getSymbol(), timeSeriesDetails.getDate());
											if (oldYearGraph != null) {
												log.info(stock.getSymbol() + " and " + timeSeriesDetails.getDate()
														+ " is already persent in our datbases !" + oldYearGraph);
												break;
											} else {
												YearGraph yearGraph = new YearGraph();
												BeanUtils.copyProperties(timeSeriesDetails, yearGraph);
												yearGraph.setSymbol(stock.getSymbol());
												yearGraph.setCountry(stock.getCountry());
												yearGraph.setExchange(stock.getExchange());
												yearGraph.setCreationDate(new Date());
												yearGraphRepository.save(yearGraph);
												newDataCount++;
												log.info(newDataCount
														+ ": Year graph data save successfully !! | Symbol is - "
														+ stock.getSymbol() + " | status - {}", Constant.OK);
											}
										}
									} else {/*-------- new data added ---------*/
										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
											YearGraph yearGraph = new YearGraph();
											BeanUtils.copyProperties(timeSeriesDetails, yearGraph);
											yearGraph.setSymbol(stock.getSymbol());
											yearGraph.setCountry(stock.getCountry());
											yearGraph.setExchange(stock.getExchange());
											yearGraph.setCreationDate(new Date());
											yearGraphRepository.save(yearGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": Year graph data save successfully !! | Symbol is - "
													+ stock.getSymbol() + " | status - {}", Constant.OK);
										}
									}
								} else {
									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
											Constant.SERVER_ERROR);
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
					}
				}
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveStockDayGraphData(String country) {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
			Country countries = countryRepository.findByCountry(country);
			if (countries != null) {
				log.info("Country found successfully !!" + country);
//				for (Country country : countryList) {
				List<Stock> stockList = stockRepository.findByCountryAndStatus(country, Constant.ONE);
				if (stockList != null && !stockList.isEmpty()) {
					log.info("Stock list found successfully !!. list size:- " + stockList.size());
					for (Stock stock : stockList) {
						apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
								Constant.ONE_DAY);
						if (!apiResponse.isBlank()) {
							List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
							try {
								Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
								}.getType();
								timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
							} catch (Exception e) {
								log.error("Exception :- " + e.getMessage());
							}
							if (!timeSeriesDetailsList.isEmpty()) {
								List<DayGraph> oldDayGraphList = dayGraphRepository.findBySymbol(stock.getSymbol());
								if (oldDayGraphList != null && !oldDayGraphList.isEmpty()) {
									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
										DayGraph oldDayGraph = dayGraphRepository.findBySymbolAndDate(stock.getSymbol(),
												timeSeriesDetails.getDate());
										if (oldDayGraph != null) {
											log.info(stock.getSymbol() + " and " + timeSeriesDetails.getDate()
													+ " is already persent in our datbases !" + oldDayGraph);
											break;
										} else {
											DayGraph dayGraph = new DayGraph();
											BeanUtils.copyProperties(timeSeriesDetails, dayGraph);
											dayGraph.setSymbol(stock.getSymbol());
											dayGraph.setCountry(stock.getCountry());
											dayGraph.setExchange(stock.getExchange());
											dayGraph.setCreationDate(new Date());
											dayGraphRepository.save(dayGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": Day graph data save successfully !! | Symbol is - "
													+ stock.getSymbol() + " | status - {}", Constant.OK);
										}
//											break;
									}
								} else {
									/* ---------- first time entries --------- */
									for (TimeSeriesDetails timeSeriesDetails1 : timeSeriesDetailsList) {
										DayGraph dayGraph = new DayGraph();
										BeanUtils.copyProperties(timeSeriesDetails1, dayGraph);
										dayGraph.setSymbol(stock.getSymbol());
										dayGraph.setCountry(stock.getCountry());
										dayGraph.setExchange(stock.getExchange());
										dayGraph.setCreationDate(new Date());
										dayGraphRepository.save(dayGraph);
										newDataCount++;
										log.info(newDataCount + ": Day graph data save successfully !! | Symbol is - "
												+ stock.getSymbol() + " | status - {}", Constant.OK);
									}
								}
								/* ----------- 5 year ago data deleted ------------- */
								for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
									DayGraph dayGraph = dayGraphRepository.findBySymbolAndDate(stock.getSymbol(),
											timeSeriesDetails.getDate());
									if (dayGraph != null) {
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("yyyy-MM-dd HH:mm:ss");
										LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
												formatter);
										// Subtract 5 years from the parsed datetime
										LocalDateTime fiveYearsAgoDateTime = dateTime.minusYears(5).minusWeeks(1);
										// Print the result
										System.out.println("String DateTime: " + dateTime.format(formatter));
										System.out.println(
												"DateTime 5 years ago: " + fiveYearsAgoDateTime.format(formatter));
										List<DayGraph> fiveYearOldData = dayGraphRepository.findAllFiveYearOldData(
												stock.getSymbol(), fiveYearsAgoDateTime.format(formatter));
										if (fiveYearOldData != null && !fiveYearOldData.isEmpty()) {
											dayGraphRepository.deleteAll(fiveYearOldData);
											log.info(fiveYearOldData
													+ " Five year old data Days deleted successfully !!" + Constant.OK);
										} else {
											log.info("Five year old data Days not found :- " + fiveYearOldData);
										}
										break;
									}
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					}
				} else {
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
				}
//				}
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void saveStockThirtyMinuteGraphData() {
//		try {
//			Integer newDataCount = 0;
//			String apiResponse = null;
//			List<Country> countryList = countryRepository.findAllCountries();
//			if (countryList != null && !countryList.isEmpty()) {
//				log.info("Country list found successfully !!. list size:- " + countryList.size());
//				for (Country country : countryList) {
//					List<Stock> stockList = stockRepository.findByCountryAndStatus(country.getCountry(), Constant.ONE);
//					if (stockList != null && !stockList.isEmpty()) {
//						log.info("Stock list found successfully !!. list size:- " + stockList.size());
//						for (Stock stock : stockList) {
//							apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
//									Constant.THIRTY_MINUTE);
//							if (!apiResponse.isBlank()) {
//								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
//								try {
//									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
//									}.getType();
//									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
//								} catch (Exception e) {
//									log.error("Exception :- " + e.getMessage());
//								}
//								if (!timeSeriesDetailsList.isEmpty()) {
//									LocalDateTime datetime = LocalDateTime.parse(timeSeriesDetailsList.get(0).getDate(),
//											DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//									LocalDateTime previousMonthDatetime = datetime.minusMonths(1).minusDays(1);
//									System.out.println("One month before " + timeSeriesDetailsList.get(0).getDate()
//											+ " is: " + previousMonthDatetime);
//									List<ThirtyMinuteGraph> oldThirtyMinuteGraphList = thirtyMinuteGraphRepository
//											.findBySymbol(stock.getSymbol());
//									if (oldThirtyMinuteGraphList != null && !oldThirtyMinuteGraphList.isEmpty()) {
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousMonthDatetime.equals(dateTime)
//													|| previousMonthDatetime.isBefore(dateTime)) {
//												ThirtyMinuteGraph oldThirtyMinuteGraph = thirtyMinuteGraphRepository
//														.findBySymbolAndDate(stock.getSymbol(),
//																timeSeriesDetails.getDate());
//												if (oldThirtyMinuteGraph != null) {
//													log.info(stock.getSymbol() + " and " + timeSeriesDetails.getDate()
//															+ " is already persent in our datbases !"
//															+ oldThirtyMinuteGraph);
//												} else {
//													ThirtyMinuteGraph thirtyMinuteGraph = new ThirtyMinuteGraph();
//													BeanUtils.copyProperties(timeSeriesDetails, thirtyMinuteGraph);
//													thirtyMinuteGraph.setSymbol(stock.getSymbol());
//													thirtyMinuteGraph.setCountry(stock.getCountry());
//													thirtyMinuteGraph.setExchange(stock.getExchange());
//													thirtyMinuteGraph.setCreationDate(new Date());
//													thirtyMinuteGraphRepository.save(thirtyMinuteGraph);
//													newDataCount++;
//													log.info(newDataCount
//															+ ": Thirty minutes graph data save successfully !! | Symbol is - "
//															+ stock.getSymbol() + " | status - {}", Constant.OK);
//												}
//											} else {
//												log.info("One Thirty previous graph data !! " + dateTime);
//											}
//										}
//									} else {/*-------- new graph data added ---------*/
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousMonthDatetime.equals(dateTime)
//													|| previousMonthDatetime.isBefore(dateTime)) {
//												ThirtyMinuteGraph thirtyMinuteGraph = new ThirtyMinuteGraph();
//												BeanUtils.copyProperties(timeSeriesDetails, thirtyMinuteGraph);
//												thirtyMinuteGraph.setSymbol(stock.getSymbol());
//												thirtyMinuteGraph.setCountry(stock.getCountry());
//												thirtyMinuteGraph.setExchange(stock.getExchange());
//												thirtyMinuteGraph.setCreationDate(new Date());
//												thirtyMinuteGraphRepository.save(thirtyMinuteGraph);
//												newDataCount++;
//												log.info(newDataCount
//														+ ": Thirty minutes graph data save successfully !! | Symbol is - "
//														+ stock.getSymbol() + " | status - {}", Constant.OK);
//											} else {
//												log.info("One Thirty previous graph data !! " + dateTime);
//											}
//										}
//									}
//									/* ----------- Before 1 month ago data deleted ------------- */
//									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//										ThirtyMinuteGraph thirtyMinuteGraph = thirtyMinuteGraphRepository
//												.findBySymbolAndDate(stock.getSymbol(), timeSeriesDetails.getDate());
//										if (thirtyMinuteGraph != null) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											// Subtract 1 month from the parsed datetime
//											LocalDateTime oneMonthAgoDateTime = dateTime.minusMonths(1).minusDays(1);
//											System.out.println("String DateTime: " + dateTime.format(formatter));
//											System.out.println(
//													"DateTime 1 month ago: " + oneMonthAgoDateTime.format(formatter));
//											List<ThirtyMinuteGraph> oneMonthOldData = thirtyMinuteGraphRepository
//													.findAllOneMonthOldData(stock.getSymbol(),
//															oneMonthAgoDateTime.format(formatter));
//											if (oneMonthOldData != null && !oneMonthOldData.isEmpty()) {
//												thirtyMinuteGraphRepository.deleteAll(oneMonthOldData);
//												log.info("One month old data deleted successfully !!" + Constant.OK);
//											} else {
//												log.info("One month old data not found :- " + oneMonthOldData);
//											}
//											break;
//										}
//									}
//								} else {
//									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//											Constant.SERVER_ERROR);
//								}
//							} else {
//								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//										Constant.SERVER_ERROR);
//							}
//						}
//					} else {
//						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//					}
//				}
//			} else {
//				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

//	public void saveStockFifteenMinuteGraphData() {
//		try {
//			Integer newDataCount = 0;
//			String apiResponse = null;
//			List<Country> countryList = countryRepository.findAllCountries();
//			if (countryList != null && !countryList.isEmpty()) {
//				log.info("Country list found successfully !!. list size:- " + countryList.size());
//				for (Country country : countryList) {
//					List<Stock> stockList = stockRepository.findByCountryAndStatus(country.getCountry(), Constant.ONE);
//					if (stockList != null && !stockList.isEmpty()) {
//						log.info("Stock list found successfully !!. list size:- " + stockList.size());
//						for (Stock stock : stockList) {
//							apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
//									Constant.FIFTEEN_MINUTE);
//							if (!apiResponse.isBlank()) {
//								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
//								try {
//									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
//									}.getType();
//									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
//								} catch (Exception e) {
//									log.error("Exception :- " + e.getMessage());
//								}
//								if (!timeSeriesDetailsList.isEmpty()) {
//									LocalDateTime datetime = LocalDateTime.parse(timeSeriesDetailsList.get(0).getDate(),
//											DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//									LocalDateTime previousWeekDatetime = datetime.minusDays(6);
//									System.out.println("One week before " + timeSeriesDetailsList.get(0).getDate()
//											+ " is: " + previousWeekDatetime);
//									List<FifteenMinuteGraph> oldFifteenMinuteGraphList = fifteenMinuteRepository
//											.findBySymbol(stock.getSymbol());
//									if (oldFifteenMinuteGraphList != null && !oldFifteenMinuteGraphList.isEmpty()) {
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousWeekDatetime.equals(dateTime)
//													|| previousWeekDatetime.isBefore(dateTime)) {
//												FifteenMinuteGraph oldFifteenMinuteGraph = fifteenMinuteRepository
//														.findBySymbolAndDate(stock.getSymbol(),
//																timeSeriesDetails.getDate());
//												if (oldFifteenMinuteGraph != null) {
//													log.info(stock.getSymbol() + " and " + timeSeriesDetails.getDate()
//															+ " is already persent in our datbases !"
//															+ oldFifteenMinuteGraph);
//													break;
//												} else {
//													FifteenMinuteGraph fifteenMinuteGraph = new FifteenMinuteGraph();
//													BeanUtils.copyProperties(timeSeriesDetails, fifteenMinuteGraph);
//													fifteenMinuteGraph.setSymbol(stock.getSymbol());
//													fifteenMinuteGraph.setCountry(stock.getCountry());
//													fifteenMinuteGraph.setExchange(stock.getExchange());
//													fifteenMinuteRepository.save(fifteenMinuteGraph);
//													newDataCount++;
//													log.info(newDataCount
//															+ ": Fifteen minutes graph data save successfully !! | Symbol is - "
//															+ stock.getSymbol() + " | status - {}", Constant.OK);
//												}
//											} else {
//												log.info("One Week previous graph data !! " + dateTime);
//											}
//										}
//									} else {/* -------- new data ---------- */
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousWeekDatetime.equals(dateTime)
//													|| previousWeekDatetime.isBefore(dateTime)) {
//												FifteenMinuteGraph fifteenMinuteGraph = new FifteenMinuteGraph();
//												BeanUtils.copyProperties(timeSeriesDetails, fifteenMinuteGraph);
//												fifteenMinuteGraph.setSymbol(stock.getSymbol());
//												fifteenMinuteGraph.setCountry(stock.getCountry());
//												fifteenMinuteGraph.setExchange(stock.getExchange());
//												fifteenMinuteGraph.setCreationDate(new Date());
//												fifteenMinuteRepository.save(fifteenMinuteGraph);
//												newDataCount++;
//												log.info(newDataCount
//														+ ": Fifteen minutes graph data save successfully !! | Symbol is - "
//														+ stock.getSymbol() + " | status - {}", Constant.OK);
//											} else {
//												log.info("One Week previous graph data !! " + dateTime);
//											}
//										}
//									}
//									/* ----------- Before 1 week ago data deleted ------------- */
//									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//										FifteenMinuteGraph fifteenMinuteGraph = fifteenMinuteRepository
//												.findBySymbolAndDate(stock.getSymbol(), timeSeriesDetails.getDate());
//										if (fifteenMinuteGraph != null) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											// Subtract 1 month from the parsed datetime
//											LocalDateTime oneWeekAgoDateTime = dateTime.minusWeeks(1).minusHours(1);
//											System.out.println("String DateTime: " + dateTime.format(formatter));
//											System.out.println(
//													"DateTime 1 week ago: " + oneWeekAgoDateTime.format(formatter));
//											List<FifteenMinuteGraph> oneWeekOldData = fifteenMinuteRepository
//													.findAllOneMonthOldData(stock.getSymbol(),
//															oneWeekAgoDateTime.format(formatter));
//											if (oneWeekOldData != null && !oneWeekOldData.isEmpty()) {
//												fifteenMinuteRepository.deleteAll(oneWeekOldData);
//												log.info("One Week old data deleted successfully !!" + Constant.OK);
//											} else {
//												log.info("One Week old data not found :- " + oneWeekOldData);
//											}
//											break;
//										}
//									}
//								} else {
//									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//											Constant.SERVER_ERROR);
//								}
//							} else {
//								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//										Constant.SERVER_ERROR);
//							}
//						}
//					} else {
//						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//					}
//				}
//				/* --------------- Before one month data deleted here ------------- */
//			} else {
//				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	public void saveStockMinuteGraphData(String country) {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
			Country countries = countryRepository.findByCountry(country);
			if (countries != null) {
				log.info("Country found successfully !! " + country);
//				for (Country country : countryList) {
				List<Stock> stockList = stockRepository.findByCountryAndStatus(country, Constant.ONE);
				if (stockList != null && !stockList.isEmpty()) {
					log.info("Stock list found successfully !!. list size:- " + stockList.size());
					for (Stock stock : stockList) {
						apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
								Constant.FIVE_MINUTE);
						if (!apiResponse.isBlank()) {
							List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
							try {
								Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
								}.getType();
								timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
							} catch (Exception e) {
								log.error("Exception :- " + e.getMessage());
							}
							if (!timeSeriesDetailsList.isEmpty()) {

								log.info("Minutes graph data size :- " + timeSeriesDetailsList.size());
								/* functionality for graph */
								TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
								String previousDate = DateUtil.getPreviousDateTimes(
										timeSeriesDetailsList.get(0).getDate(), Constant.ONE_MONTH);
								LocalDateTime latestDate = DateUtil
										.stringToLocalDateTime(timeSeriesDetailsList.get(0).getDate());

//									boolean data = false;
//									for (TimeSeriesDetails details : timeSeriesDetailsList) {
//										if (details.getDate().contains(previousDate)) {
//											BeanUtils.copyProperties(details, seriesDetails);
//											data = true;
//											break;
//										}
//									}
//									latestDate = latestDate.minusDays(7);// minusMonth
//									while (data == false) {
//										latestDate = latestDate.minusDays(1);// minusMonth
//										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//										previousDate = latestDate.format(formatter);
//										for (TimeSeriesDetails details : timeSeriesDetailsList) {
//											if (details.getDate().contains(previousDate)) {
//												BeanUtils.copyProperties(details, seriesDetails);
//												data = true;
//												break;
//											}
//										}
//									}

								List<MinuteGraph> minuteGraphList = minuteGraphRepository
										.findBySymbol(stock.getSymbol());
								if (minuteGraphList != null && !minuteGraphList.isEmpty()) {
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										MinuteGraph oldMinuteGraph = minuteGraphRepository
												.findBySymbolAndDate(stock.getSymbol(), details.getDate());
										if (oldMinuteGraph != null) {
											log.info(stock.getSymbol() + " and " + oldMinuteGraph.getDate()
													+ " is already persent in our datbases !" + oldMinuteGraph);
											break;
										} else {
											if (!details.getDate().contains(previousDate)) {
												MinuteGraph minuteGraph = new MinuteGraph();
												BeanUtils.copyProperties(details, minuteGraph);
												minuteGraph.setSymbol(stock.getSymbol());
												minuteGraph.setCountry(stock.getCountry());
												minuteGraph.setExchange(stock.getExchange());
												minuteGraph.setCreationDate(new Date());
												minuteGraphRepository.save(minuteGraph);
												newDataCount++;
												log.info(newDataCount
														+ ": minutes graph data save successfully !! | Symbol is - "
														+ stock.getSymbol() + " | status - {}", Constant.OK);
											} else {
												break;
											}
										}
									}
								} else {
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (!details.getDate().contains(previousDate)) {
											MinuteGraph minuteGraph = new MinuteGraph();
											BeanUtils.copyProperties(details, minuteGraph);
											minuteGraph.setSymbol(stock.getSymbol());
											minuteGraph.setCountry(stock.getCountry());
											minuteGraph.setExchange(stock.getExchange());
											minuteGraph.setCreationDate(new Date());
											minuteGraphRepository.save(minuteGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": minutes graph data save successfully !! | Symbol is - "
													+ stock.getSymbol() + " | status - {}", Constant.OK);
										} else {
											break;
										}
									}
								}

								/* ----------- Before 1 week ago data deleted ------------- */
								for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
									MinuteGraph minuteGraph = minuteGraphRepository
											.findBySymbolAndDate(stock.getSymbol(), timeSeriesDetails.getDate());
									if (minuteGraph != null) {
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("yyyy-MM-dd HH:mm:ss");
										LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
												formatter);
										// Subtract 1 month from the parsed datetime
										LocalDateTime oneMonthAgoDateTime = dateTime.minusDays(31);
										System.out.println("String DateTime: " + dateTime.format(formatter));
										System.out.println(
												"DateTime 1 Month ago: " + oneMonthAgoDateTime.format(formatter));
										List<MinuteGraph> monthOldData = minuteGraphRepository
												.findAllOneMonthOldData(stock.getSymbol(), oneMonthAgoDateTime);
										if (monthOldData != null && !monthOldData.isEmpty()) {
											minuteGraphRepository.deleteAll(monthOldData);
											log.info(
													"One Month old Indices data deleted successfully !!" + Constant.OK);
										} else {
											log.info("One Month old Indices data not found :- " + monthOldData);
										}
										break;
									}
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					}
				} else {
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
				}
//				}
				/* --------------- Before one month data deleted here ------------- */
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class StockYearGraphTask implements Runnable {
		@Override
		public void run() {
			saveStockYearGraphData();
		}
	}

	public void yearGraphThread() {
		StockYearGraphTask stockYearGraphTask = new StockYearGraphTask();
		Thread thread = new Thread(stockYearGraphTask);
		thread.start();
	}

	private class IndicesGraphSaudiTask implements Runnable {
		@Override
		public void run() {
			/* Indices graph */
//			saveIndicesDayGraphData(Constant.SAUDI_ARABIA);
//			saveIndicesMinuteGraphData(Constant.SAUDI_ARABIA);
			/* Stocks graph */
//			saveStockDayGraphData(Constant.SAUDI_ARABIA);
			saveStockMinuteGraphData(Constant.SAUDI_ARABIA);
		}
	}

	public void indicesGraphForSaudiThread() {
		IndicesGraphSaudiTask stockGraphTask = new IndicesGraphSaudiTask();
		Thread thread = new Thread(stockGraphTask);
		thread.start();

	}

	private class IndicesGraphUnitedStatesTask implements Runnable {
		@Override
		public void run() {
			/* Indices graph */
//			saveIndicesDayGraphData(Constant.UNITED_STATES);
//			saveIndicesMinuteGraphData(Constant.UNITED_STATES);
			/* Stocks graph */
			saveStockDayGraphData(Constant.UNITED_STATES);
//			saveStockMinuteGraphData(Constant.UNITED_STATES);
		}
	}

	public void indicesGraphForUnitedStatesThread() {
		IndicesGraphUnitedStatesTask stockGraphTask = new IndicesGraphUnitedStatesTask();
		Thread thread = new Thread(stockGraphTask);
		thread.start();

	}

	/* -------- save indices chart data ---------- */
	public void saveIndicesYearGraphData() {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
			List<Country> countryList = countryRepository.findAllCountries();
			if (countryList != null && !countryList.isEmpty()) {
				log.info("Country list found successfully !!. list size:- " + countryList.size());
				for (Country country : countryList) {
					List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country.getCountry(),
							Constant.ONE);
					if (indicesList != null && !indicesList.isEmpty()) {
						log.info("Stock list found successfully !!. list size:- " + indicesList.size());
						for (Indices indices : indicesList) {
//							apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
//									Constant.ONE_YEAR);
							apiResponse = thirdPartyApiUtil.getIndicesTimeSeriesData(indices, Constant.ONE_YEAR);
							if (!apiResponse.isBlank()) {
								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
								try {
									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
									}.getType();
									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
								} catch (Exception e) {
									log.error("Exception :- " + e.getMessage());
								}
								if (!timeSeriesDetailsList.isEmpty()) {
									List<IndicesYearGraph> oldIndicesYearGraphList = indicesYearGraphRepository
											.findBySymbol(indices.getSymbol());
									if (oldIndicesYearGraphList != null && !oldIndicesYearGraphList.isEmpty()) {
										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
											IndicesYearGraph oldIndicesYearGraph = indicesYearGraphRepository
													.findBySymbolAndDate(indices.getSymbol(),
															timeSeriesDetails.getDate());
											if (oldIndicesYearGraph != null) {
												log.info(indices.getSymbol() + " and " + timeSeriesDetails.getDate()
														+ " is already persent in our datbases !"
														+ oldIndicesYearGraph);
												break;
											} else {
												IndicesYearGraph indicesYearGraph = new IndicesYearGraph();
												BeanUtils.copyProperties(timeSeriesDetails, indicesYearGraph);
												indicesYearGraph.setSymbol(indices.getSymbol());
												indicesYearGraph.setCountry(indices.getCountry());
												indicesYearGraph.setExchange(indices.getExchange());
												indicesYearGraph.setCreationDate(new Date());
												indicesYearGraphRepository.save(indicesYearGraph);
												newDataCount++;
												log.info(newDataCount
														+ ": Indices Year graph data save successfully !! | Symbol is - "
														+ indices.getSymbol() + " | status - {}", Constant.OK);
											}
										}
									} else {/*-------- new data added ---------*/
										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
											IndicesYearGraph indicesYearGraph = new IndicesYearGraph();
											BeanUtils.copyProperties(timeSeriesDetails, indicesYearGraph);
											indicesYearGraph.setSymbol(indices.getSymbol());
											indicesYearGraph.setCountry(indices.getCountry());
											indicesYearGraph.setExchange(indices.getExchange());
											indicesYearGraph.setCreationDate(new Date());
											indicesYearGraphRepository.save(indicesYearGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": Indices Year graph data save successfully !! | Symbol is - "
													+ indices.getSymbol() + " | status - {}", Constant.OK);
										}
									}
								} else {
									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
											Constant.SERVER_ERROR);
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						}
					} else {
						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
					}
				}
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveIndicesDayGraphData(String country) {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
//			List<Country> countryList = countryRepository.findAllCountries();
			Country countries = countryRepository.findByCountry(country);
			if (countries != null) {
				log.info("Country found successfully !!" + country);
//				for (Country country : countryList) {
				List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country, Constant.ONE);
				if (indicesList != null && !indicesList.isEmpty()) {
					log.info("Stock list found successfully !!. list size:- " + indicesList.size());
					for (Indices indices : indicesList) {
//							apiResponse = thirdPartyApiUtil.getTimeSeries(stock.getSymbol(), stock.getCountry(),
//									Constant.ONE_DAY);
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeriesData(indices, Constant.ONE_DAY);
						if (!apiResponse.isBlank()) {
							List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
							try {
								Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
								}.getType();
								timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
							} catch (Exception e) {
								log.error("Exception :- " + e.getMessage());
							}
							if (!timeSeriesDetailsList.isEmpty()) {
								List<IndicesDayGraph> oldIndicesDayGraphList = indicesDayGraphRepository
										.findBySymbol(indices.getSymbol());
								if (oldIndicesDayGraphList != null && !oldIndicesDayGraphList.isEmpty()) {
									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
										IndicesDayGraph oldIndicesDayGraph = indicesDayGraphRepository
												.findBySymbolAndDate(indices.getSymbol(), timeSeriesDetails.getDate());
										if (oldIndicesDayGraph != null) {
											log.info(indices.getSymbol() + " and " + timeSeriesDetails.getDate()
													+ " is already persent in our datbases !" + oldIndicesDayGraph);
											break;
										} else {
											IndicesDayGraph indicesDayGraph = new IndicesDayGraph();
											BeanUtils.copyProperties(timeSeriesDetails, indicesDayGraph);
											indicesDayGraph.setSymbol(indices.getSymbol());
											indicesDayGraph.setCountry(indices.getCountry());
											indicesDayGraph.setExchange(indices.getExchange());
											indicesDayGraph.setCreationDate(new Date());
											indicesDayGraphRepository.save(indicesDayGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": Indices Day graph data save successfully !! | Symbol is - "
													+ indices.getSymbol() + " | status - {}", Constant.OK);
										}
//											break;
									}
								} else {
									/* ---------- first time entries --------- */
									for (TimeSeriesDetails timeSeriesDetails1 : timeSeriesDetailsList) {
										IndicesDayGraph indicesDayGraph = new IndicesDayGraph();
										BeanUtils.copyProperties(timeSeriesDetails1, indicesDayGraph);
										indicesDayGraph.setSymbol(indices.getSymbol());
										indicesDayGraph.setCountry(indices.getCountry());
										indicesDayGraph.setExchange(indices.getExchange());
										indicesDayGraph.setCreationDate(new Date());
										indicesDayGraphRepository.save(indicesDayGraph);
										newDataCount++;
										log.info(newDataCount
												+ ": Indices Day graph data save successfully !! | Symbol is - "
												+ indices.getSymbol() + " | status - {}", Constant.OK);
									}
								}
								/* ----------- 5 year ago data deleted ------------- */
								for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
									IndicesDayGraph indicesDayGraph = indicesDayGraphRepository
											.findBySymbolAndDate(indices.getSymbol(), timeSeriesDetails.getDate());
									if (indicesDayGraph != null) {
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("yyyy-MM-dd HH:mm:ss");
										LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
												formatter);
										// Subtract 5 years from the parsed datetime
										LocalDateTime fiveYearsAgoDateTime = dateTime.minusYears(5).minusWeeks(1);
										// Print the result
										System.out.println("String DateTime: " + dateTime.format(formatter));
										System.out.println(
												"DateTime 5 years ago: " + fiveYearsAgoDateTime.format(formatter));
										List<DayGraph> fiveYearOldData = dayGraphRepository.findAllFiveYearOldData(
												indices.getSymbol(), fiveYearsAgoDateTime.format(formatter));
										if (fiveYearOldData != null && !fiveYearOldData.isEmpty()) {
											dayGraphRepository.deleteAll(fiveYearOldData);
											log.info(
													"Indices Five year old data deleted successfully !!" + Constant.OK);
										} else {
											log.info("Indices Five year old data not found :- " + fiveYearOldData);
										}
										break;
									}
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					}
				} else {
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
				}
//				}
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void saveIndicesThirtyMinuteGraphData() {
//		try {
//			Integer newDataCount = 0;
//			String apiResponse = null;
//			List<Country> countryList = countryRepository.findAllCountries();
//			if (countryList != null && !countryList.isEmpty()) {
//				log.info("Country list found successfully !!. list size:- " + countryList.size());
//				for (Country country : countryList) {
//					List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country.getCountry(),
//							Constant.ONE);
//					if (indicesList != null && !indicesList.isEmpty()) {
//						log.info("Stock list found successfully !!. list size:- " + indicesList.size());
//						for (Indices indices : indicesList) {
//							apiResponse = thirdPartyApiUtil.getIndicesTimeSeriesData(indices, Constant.THIRTY_MINUTE);
//							if (!apiResponse.isBlank()) {
//								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
//								try {
//									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
//									}.getType();
//									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
//								} catch (Exception e) {
//									log.error("Exception :- " + e.getMessage());
//								}
//								if (!timeSeriesDetailsList.isEmpty()) {
//									LocalDateTime datetime = LocalDateTime.parse(timeSeriesDetailsList.get(0).getDate(),
//											DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//									LocalDateTime previousMonthDatetime = datetime.minusMonths(1).minusDays(1);
//									System.out.println("One month before " + timeSeriesDetailsList.get(0).getDate()
//											+ " is: " + previousMonthDatetime);
//									List<IndicesThirtyMinuteGraph> oldIndicesThirtyMinuteGraphList = indicesThirtyMinuteGraphRepository
//											.findBySymbol(indices.getSymbol());
//									if (oldIndicesThirtyMinuteGraphList != null
//											&& !oldIndicesThirtyMinuteGraphList.isEmpty()) {
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousMonthDatetime.equals(dateTime)
//													|| previousMonthDatetime.isBefore(dateTime)) {
//												IndicesThirtyMinuteGraph oldIndicesThirtyMinuteGraph = indicesThirtyMinuteGraphRepository
//														.findBySymbolAndDate(indices.getSymbol(),
//																timeSeriesDetails.getDate());
//												if (oldIndicesThirtyMinuteGraph != null) {
//													log.info(indices.getSymbol() + " and " + timeSeriesDetails.getDate()
//															+ " is already persent in our datbases !"
//															+ oldIndicesThirtyMinuteGraph);
//												} else {
//													IndicesThirtyMinuteGraph indicesThirtyMinuteGraph = new IndicesThirtyMinuteGraph();
//													BeanUtils.copyProperties(timeSeriesDetails,
//															indicesThirtyMinuteGraph);
//													indicesThirtyMinuteGraph.setSymbol(indices.getSymbol());
//													indicesThirtyMinuteGraph.setCountry(indices.getCountry());
//													indicesThirtyMinuteGraph.setExchange(indices.getExchange());
//													indicesThirtyMinuteGraph.setCreationDate(new Date());
//													indicesThirtyMinuteGraphRepository.save(indicesThirtyMinuteGraph);
//													newDataCount++;
//													log.info(newDataCount
//															+ ":Indices Thirty minutes graph data save successfully !! | Symbol is - "
//															+ indices.getSymbol() + " | status - {}", Constant.OK);
//												}
//											} else {
//												log.info("One month previous Indices Thirty minute graph data !! "
//														+ dateTime);
//											}
//										}
//									} else {/*-------- new graph data added ---------*/
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousMonthDatetime.equals(dateTime)
//													|| previousMonthDatetime.isBefore(dateTime)) {
//												IndicesThirtyMinuteGraph indicesThirtyMinuteGraph = new IndicesThirtyMinuteGraph();
//												BeanUtils.copyProperties(timeSeriesDetails, indicesThirtyMinuteGraph);
//												indicesThirtyMinuteGraph.setSymbol(indices.getSymbol());
//												indicesThirtyMinuteGraph.setCountry(indices.getCountry());
//												indicesThirtyMinuteGraph.setExchange(indices.getExchange());
//												indicesThirtyMinuteGraph.setCreationDate(new Date());
//												indicesThirtyMinuteGraphRepository.save(indicesThirtyMinuteGraph);
//												newDataCount++;
//												log.info(newDataCount
//														+ ": Indices Thirty minutes graph data save successfully !! | Symbol is - "
//														+ indices.getSymbol() + " | status - {}", Constant.OK);
//											} else {
//												log.info("One month previous Indices Thirty minute graph data !! "
//														+ dateTime);
//											}
//										}
//									}
//									/* ----------- Before 1 month ago data deleted ------------- */
//									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//										ThirtyMinuteGraph thirtyMinuteGraph = thirtyMinuteGraphRepository
//												.findBySymbolAndDate(indices.getSymbol(), timeSeriesDetails.getDate());
//										if (thirtyMinuteGraph != null) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											// Subtract 1 month from the parsed datetime
//											LocalDateTime oneMonthAgoDateTime = dateTime.minusMonths(1).minusDays(1);
//											System.out.println("String DateTime: " + dateTime.format(formatter));
//											System.out.println(
//													"DateTime 1 month ago: " + oneMonthAgoDateTime.format(formatter));
//											List<ThirtyMinuteGraph> oneMonthOldData = thirtyMinuteGraphRepository
//													.findAllOneMonthOldData(indices.getSymbol(),
//															oneMonthAgoDateTime.format(formatter));
//											if (oneMonthOldData != null && !oneMonthOldData.isEmpty()) {
//												thirtyMinuteGraphRepository.deleteAll(oneMonthOldData);
//												log.info("One month old data deleted successfully !!" + Constant.OK);
//											} else {
//												log.info("One month old data not found :- " + oneMonthOldData);
//											}
//											break;
//										}
//									}
//								} else {
//									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//											Constant.SERVER_ERROR);
//								}
//							} else {
//								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//										Constant.SERVER_ERROR);
//							}
//						}
//					} else {
//						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//					}
//				}
//			} else {
//				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void saveIndicesFifteenMinuteGraphData() {
//		try {
//			Integer newDataCount = 0;
//			String apiResponse = null;
//			List<Country> countryList = countryRepository.findAllCountries();
//			if (countryList != null && !countryList.isEmpty()) {
//				log.info("Country list found successfully !!. list size:- " + countryList.size());
//				for (Country country : countryList) {
//					List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country.getCountry(),
//							Constant.ONE);
//					if (indicesList != null && !indicesList.isEmpty()) {
//						log.info("Stock list found successfully !!. list size:- " + indicesList.size());
//						for (Indices indices : indicesList) {
//							apiResponse = thirdPartyApiUtil.getIndicesTimeSeriesData(indices, Constant.FIFTEEN_MINUTE);
//							if (!apiResponse.isBlank()) {
//								List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
//								try {
//									Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
//									}.getType();
//									timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
//								} catch (Exception e) {
//									log.error("Exception :- " + e.getMessage());
//								}
//								if (!timeSeriesDetailsList.isEmpty()) {
//									LocalDateTime datetime = LocalDateTime.parse(timeSeriesDetailsList.get(0).getDate(),
//											DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//									LocalDateTime previousWeekDatetime = datetime.minusDays(6);
//									System.out.println("One week before " + timeSeriesDetailsList.get(0).getDate()
//											+ " is: " + previousWeekDatetime);
//									List<IndicesFifteenMinuteGraph> oldIndicesFifteenMinuteGraphList = indicesFifteenMinuteGraphRepository
//											.findBySymbol(indices.getSymbol());
//									if (oldIndicesFifteenMinuteGraphList != null
//											&& !oldIndicesFifteenMinuteGraphList.isEmpty()) {
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousWeekDatetime.equals(dateTime)
//													|| previousWeekDatetime.isBefore(dateTime)) {
//												IndicesFifteenMinuteGraph oldIndicesFifteenMinuteGraph = indicesFifteenMinuteGraphRepository
//														.findBySymbolAndDate(indices.getSymbol(),
//																timeSeriesDetails.getDate());
//												if (oldIndicesFifteenMinuteGraph != null) {
//													log.info(indices.getSymbol() + " and " + timeSeriesDetails.getDate()
//															+ " is already persent in our datbases !"
//															+ oldIndicesFifteenMinuteGraph);
//													break;
//												} else {
//													IndicesFifteenMinuteGraph indicesFifteenMinuteGraph = new IndicesFifteenMinuteGraph();
//													BeanUtils.copyProperties(timeSeriesDetails,
//															indicesFifteenMinuteGraph);
//													indicesFifteenMinuteGraph.setSymbol(indices.getSymbol());
//													indicesFifteenMinuteGraph.setCountry(indices.getCountry());
//													indicesFifteenMinuteGraph.setExchange(indices.getExchange());
//													indicesFifteenMinuteGraphRepository.save(indicesFifteenMinuteGraph);
//													newDataCount++;
//													log.info(newDataCount
//															+ ": Indices Fifteen minutes graph data save successfully !! | Symbol is - "
//															+ indices.getSymbol() + " | status - {}", Constant.OK);
//												}
//											} else {
//												log.info("One Week Indices previous graph data !! " + dateTime);
//											}
//										}
//									} else {/* -------- new data ---------- */
//										for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											if (previousWeekDatetime.equals(dateTime)
//													|| previousWeekDatetime.isBefore(dateTime)) {
//												IndicesFifteenMinuteGraph indicesFifteenMinuteGraph = new IndicesFifteenMinuteGraph();
//												BeanUtils.copyProperties(timeSeriesDetails, indicesFifteenMinuteGraph);
//												indicesFifteenMinuteGraph.setSymbol(indices.getSymbol());
//												indicesFifteenMinuteGraph.setCountry(indices.getCountry());
//												indicesFifteenMinuteGraph.setExchange(indices.getExchange());
//												indicesFifteenMinuteGraphRepository.save(indicesFifteenMinuteGraph);
//												newDataCount++;
//												log.info(newDataCount
//														+ ": Indices Fifteen minutes graph data save successfully !! | Symbol is - "
//														+ indices.getSymbol() + " | status - {}", Constant.OK);
//											} else {
//												log.info("One Week Indices previous graph data !! " + dateTime);
//											}
//										}
//									}
//									/* ----------- Before 1 week ago data deleted ------------- */
//									for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
//										IndicesFifteenMinuteGraph fifteenMinuteGraph = indicesFifteenMinuteGraphRepository
//												.findBySymbolAndDate(indices.getSymbol(), timeSeriesDetails.getDate());
//										if (fifteenMinuteGraph != null) {
//											DateTimeFormatter formatter = DateTimeFormatter
//													.ofPattern("yyyy-MM-dd HH:mm:ss");
//											LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
//													formatter);
//											// Subtract 1 month from the parsed datetime
//											LocalDateTime oneWeekAgoDateTime = dateTime.minusWeeks(1).minusHours(1);
//											System.out.println("String DateTime: " + dateTime.format(formatter));
//											System.out.println(
//													"DateTime 1 week ago: " + oneWeekAgoDateTime.format(formatter));
//											List<IndicesFifteenMinuteGraph> oneWeekOldData = indicesFifteenMinuteGraphRepository
//													.findAllOneMonthOldData(indices.getSymbol(),
//															oneWeekAgoDateTime.format(formatter));
//											if (oneWeekOldData != null && !oneWeekOldData.isEmpty()) {
//												indicesFifteenMinuteGraphRepository.deleteAll(oneWeekOldData);
//												log.info("One Week old Indices data deleted successfully !!"
//														+ Constant.OK);
//											} else {
//												log.info("One Week old Indices data not found :- " + oneWeekOldData);
//											}
//											break;
//										}
//									}
//								} else {
//									log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//											Constant.SERVER_ERROR);
//								}
//							} else {
//								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
//										Constant.SERVER_ERROR);
//							}
//						}
//					} else {
//						log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//					}
//				}
//				/* --------------- Before one month data deleted here ------------- */
//			} else {
//				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void saveIndicesMinuteGraphData(String country) {
		try {
			Integer newDataCount = 0;
			String apiResponse = null;
			Country countries = countryRepository.findByCountry(country);
			if (countries != null) {
				log.info("Country found successfully !!" + country);
//				for (Country country : countryList) {
				List<Indices> indicesList = indicesRepository.findByCountryAndStatus(country, Constant.ONE);
				if (indicesList != null && !indicesList.isEmpty()) {
					log.info("Stock list found successfully !!. list size:- " + indicesList.size());
					for (Indices indices : indicesList) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeriesData(indices, Constant.FIVE_MINUTE);
						if (!apiResponse.isBlank()) {
							List<TimeSeriesDetails> timeSeriesDetailsList = new ArrayList<TimeSeriesDetails>();
							try {
								Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
								}.getType();
								timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
							} catch (Exception e) {
								log.error("Exception :- " + e.getMessage());
							}
							if (!timeSeriesDetailsList.isEmpty()) {
								log.info("Minutes graph data size :- " + timeSeriesDetailsList.size());
								/* functionality for graph */
								TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
								String previousDate = DateUtil.getPreviousDateTimes(
										timeSeriesDetailsList.get(0).getDate(), Constant.ONE_MONTH);
								LocalDateTime latestDate = DateUtil
										.stringToLocalDateTime(timeSeriesDetailsList.get(0).getDate());

//									boolean data = false;
//									for (TimeSeriesDetails details : timeSeriesDetailsList) {
//										if (details.getDate().contains(previousDate)) {
//											BeanUtils.copyProperties(details, seriesDetails);
//											data = true;
//											break;
//										}
//									}
//									latestDate = latestDate.minusDays(9);// minusMonth
//									while (data == false) {
//										latestDate = latestDate.minusDays(1);// minusMonth
//										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//										previousDate = latestDate.format(formatter);
//										for (TimeSeriesDetails details : timeSeriesDetailsList) {
//											if (details.getDate().contains(previousDate)) {
//												BeanUtils.copyProperties(details, seriesDetails);
//												data = true;
//												break;
//											}
//										}
//									}
								List<IndicesMinuteGraph> indicesOneMinuteGraphList = indicesOneMinuteGraphRepository
										.findBySymbol(indices.getSymbol());
								if (indicesOneMinuteGraphList != null && !indicesOneMinuteGraphList.isEmpty()) {
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										IndicesMinuteGraph oldIndicesOneMinuteGraph = indicesOneMinuteGraphRepository
												.findBySymbolAndDate(indices.getSymbol(), details.getDate());
										if (oldIndicesOneMinuteGraph != null) {
											log.info(indices.getSymbol() + " and " + oldIndicesOneMinuteGraph.getDate()
													+ " is already persent in our datbases !"
													+ oldIndicesOneMinuteGraph);
											break;
										} else {
											if (!details.getDate().contains(previousDate)) {
												IndicesMinuteGraph indicesOneMinuteGraph = new IndicesMinuteGraph();
												BeanUtils.copyProperties(details, indicesOneMinuteGraph);
												indicesOneMinuteGraph.setSymbol(indices.getSymbol());
												indicesOneMinuteGraph.setCountry(indices.getCountry());
												indicesOneMinuteGraph.setExchange(indices.getExchange());
												indicesOneMinuteGraph.setCreationDate(new Date());
												indicesOneMinuteGraphRepository.save(indicesOneMinuteGraph);
												newDataCount++;
												log.info(newDataCount
														+ ": Indices minutes graph data save successfully !! | Symbol is - "
														+ indices.getSymbol() + " | status - {}", Constant.OK);
											} else {
												break;
											}
										}
									}
								} else {
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (!details.getDate().contains(previousDate)) {
											IndicesMinuteGraph indicesOneMinuteGraph = new IndicesMinuteGraph();
											BeanUtils.copyProperties(details, indicesOneMinuteGraph);
											indicesOneMinuteGraph.setSymbol(indices.getSymbol());
											indicesOneMinuteGraph.setCountry(indices.getCountry());
											indicesOneMinuteGraph.setExchange(indices.getExchange());
											indicesOneMinuteGraph.setCreationDate(new Date());
											indicesOneMinuteGraphRepository.save(indicesOneMinuteGraph);
											newDataCount++;
											log.info(newDataCount
													+ ": Indices minutes graph data save successfully !! | Symbol is - "
													+ indices.getSymbol() + " | status - {}", Constant.OK);
										} else {
											break;
										}
									}
								}
								/* ----------- Before 1 week ago data deleted ------------- */
								for (TimeSeriesDetails timeSeriesDetails : timeSeriesDetailsList) {
									IndicesMinuteGraph indicesOneMinuteGraph = indicesOneMinuteGraphRepository
											.findBySymbolAndDate(indices.getSymbol(), timeSeriesDetails.getDate());
									if (indicesOneMinuteGraph != null) {
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("yyyy-MM-dd HH:mm:ss");
										LocalDateTime dateTime = LocalDateTime.parse(timeSeriesDetails.getDate(),
												formatter);
										// Subtract 1 month from the parsed datetime
										LocalDateTime oneMonthAgoDateTime = dateTime.minusDays(31);
										System.out.println("String DateTime: " + dateTime.format(formatter));
										System.out.println(
												"DateTime 1 Month ago: " + oneMonthAgoDateTime.format(formatter));
										List<IndicesMinuteGraph> oneMonthOldData = indicesOneMinuteGraphRepository
												.findAllOneMonthOldData(indices.getSymbol(), oneMonthAgoDateTime);
										if (oneMonthOldData != null && !oneMonthOldData.isEmpty()) {
											indicesOneMinuteGraphRepository.deleteAll(oneMonthOldData);
											log.info(
													"One Month old Indices data deleted successfully !!" + Constant.OK);
										} else {
											log.info("One Month old Indices data not found :- " + oneMonthOldData);
										}
										break;
									}
								}
							} else {
								log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
										Constant.SERVER_ERROR);
							}
						} else {
							log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}",
									Constant.SERVER_ERROR);
						}
					}
				} else {
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
				}
//				}
			} else {
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void saveIndicesAndStocksGraphData(List<Country> countryList) {
//		try {
//			Long count = stockRepository.count();
//			if (count > 0) {
//				for (Country country : countryList) {
//					if (country != null) {
//						String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
//						log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
//								+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
//								+ country.getMarketCloseTime() + " ]");
//						Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
//						Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
//						Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());
//
////						country.setMarketStatus(getMarketState(country.getCountry()));
////						country.setMarketStatusUpdatedAt(new Date());
////						countryRepository.save(country);
//						/* check is market open */
//						if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
////							getStockListByUpdationDateAndCountryWise(country);
//							indicesGraphForSaudiThread(country.getCountry());
//						} else {
//							log.info("Waiting... To Open The " + country.getCountry().toUpperCase()
//									+ " Market.......... ! status - {}", Constant.NOT_FOUND);
//						}
//					} else {
//						log.error("Country not found : status - {}", Constant.NOT_FOUND);
//					}
//				}
//			}
//		} catch (JsonSyntaxException e) {
//			log.error("Exception : " + e.getMessage());
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//	
//		
//	}

}