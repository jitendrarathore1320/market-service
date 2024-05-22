package com.advantal.serviceimpl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.exception.BadRequestException;
import com.advantal.exception.ResourceNotFoundException;
import com.advantal.model.Assets;
import com.advantal.model.Country;
import com.advantal.model.Crypto;
import com.advantal.model.CsvFileType;
import com.advantal.model.Dividend;
import com.advantal.model.Earning;
import com.advantal.model.Economic;
import com.advantal.model.Exchange;
import com.advantal.model.FavoriteCrypto;
import com.advantal.model.FavoriteStock;
import com.advantal.model.Holiday;
import com.advantal.model.Indices;
import com.advantal.model.IndicesDayGraph;
import com.advantal.model.IndicesMinuteGraph;
import com.advantal.model.IndicesYearGraph;
import com.advantal.model.Ipos;
import com.advantal.model.KeyExecutive;
import com.advantal.model.Portfolio;
import com.advantal.model.SaudiShariahCompliance;
import com.advantal.model.SecFiling;
import com.advantal.model.ShareHolder;
import com.advantal.model.ShariaCompliance;
import com.advantal.model.ShortingKey;
import com.advantal.model.Stock;
import com.advantal.model.StockGainer;
import com.advantal.model.StockLoser;
import com.advantal.model.StockPriceTarget;
import com.advantal.model.StockRecommendation;
import com.advantal.model.User;
import com.advantal.model.Wallet;
import com.advantal.repository.CountryRepository;
import com.advantal.repository.CryptoRepository;
import com.advantal.repository.CsvFileRepository;
import com.advantal.repository.DividendRepository;
import com.advantal.repository.EarningRepository;
import com.advantal.repository.EconomicRepository;
import com.advantal.repository.ExchangeRepository;
import com.advantal.repository.FavoriteCryptoRepository;
import com.advantal.repository.FavoriteStockRepository;
import com.advantal.repository.HolidayRepository;
import com.advantal.repository.IndicesDayGraphRepository;
import com.advantal.repository.IndicesMinuteGraphRepository;
import com.advantal.repository.IndicesRepository;
import com.advantal.repository.IndicesYearGraphRepository;
import com.advantal.repository.IposRepository;
import com.advantal.repository.KeyExecutiveRepository;
import com.advantal.repository.SariyaComplianceRepository;
import com.advantal.repository.SaudiShariahComplianceRepository;
import com.advantal.repository.SecFilingRepository;
import com.advantal.repository.ShareHolderRepository;
import com.advantal.repository.ShortingKeyRepository;
import com.advantal.repository.StockGainerRepository;
import com.advantal.repository.StockLoserRepository;
import com.advantal.repository.StockPriceTargetRepository;
import com.advantal.repository.StockProfileRepository;
import com.advantal.repository.StockRecommendationRepository;
import com.advantal.repository.StockRepository;
import com.advantal.repository.UserRepository;
import com.advantal.requestpayload.CountResponse;
import com.advantal.requestpayload.CountryResquest;
import com.advantal.requestpayload.ExchangeRequest;
import com.advantal.requestpayload.FavoriteInstrumentRequest;
import com.advantal.requestpayload.FavoriteRequestPage;
import com.advantal.requestpayload.HolidayResquest;
import com.advantal.requestpayload.IndicesRequestPayload;
import com.advantal.requestpayload.NewsRequestPayload;
import com.advantal.requestpayload.RequestPage;
import com.advantal.requestpayload.SecFilingRequestPage;
import com.advantal.requestpayload.ShariyaComplianceRequest;
import com.advantal.requestpayload.StockMoversRequest;
import com.advantal.requestpayload.StockRequest;
import com.advantal.requestpayload.StockRequestPage;
import com.advantal.requestpayload.SyncCountResponse;
import com.advantal.requestpayload.TimeSeriesRequest;
import com.advantal.requestpayload.WidgetsRequest;
import com.advantal.responsepayload.AllExploreResponse;
import com.advantal.responsepayload.AnalystDataResponse;
import com.advantal.responsepayload.AnalystResponse;
import com.advantal.responsepayload.ComplianceResponse;
import com.advantal.responsepayload.CsvSaudiExportFileRespnse;
import com.advantal.responsepayload.CsvUsaExportFileRespnse;
import com.advantal.responsepayload.DividendResponse;
import com.advantal.responsepayload.DividendResponsePage;
import com.advantal.responsepayload.EarningResponse;
import com.advantal.responsepayload.EarningResponsePage;
import com.advantal.responsepayload.EconomicResponse;
import com.advantal.responsepayload.EconomicResponsePage;
import com.advantal.responsepayload.HolidayResponse;
import com.advantal.responsepayload.HolidayResponsePage;
import com.advantal.responsepayload.IndicesDetails;
import com.advantal.responsepayload.IndicesResponsePage;
import com.advantal.responsepayload.IndicesTimeSeriesResponse;
import com.advantal.responsepayload.InstitutionalHolders;
import com.advantal.responsepayload.IposResponse;
import com.advantal.responsepayload.IposResponsePage;
import com.advantal.responsepayload.KeyExecutiveRes;
import com.advantal.responsepayload.KeyExecutiveResponse;
import com.advantal.responsepayload.KeyValueResponse;
import com.advantal.responsepayload.MoversDetails;
import com.advantal.responsepayload.MoversResponse2;
import com.advantal.responsepayload.PriceTarget;
import com.advantal.responsepayload.SearchData;
import com.advantal.responsepayload.SearchResponse;
import com.advantal.responsepayload.SecFilingRes;
import com.advantal.responsepayload.SecFilingResponse;
import com.advantal.responsepayload.SecFilingResponsePage;
import com.advantal.responsepayload.ShareHolderDataResponse;
import com.advantal.responsepayload.StockDetailsResponse;
import com.advantal.responsepayload.StockLogo;
import com.advantal.responsepayload.StockProfileResponse;
import com.advantal.responsepayload.StockResponse;
import com.advantal.responsepayload.StockResponsePage;
import com.advantal.responsepayload.TimeSeriesDetails;
import com.advantal.responsepayload.TimeSeriesResponse;
import com.advantal.responsepayload.TsCryptoListData;
import com.advantal.responsepayload.TsCryptoListPrice;
import com.advantal.responsepayload.TsCryptoListResponse;
import com.advantal.responsepayload.WidgetResponse;
import com.advantal.responsepayload.csvAlRajhiComplianceExportFileResponse;
import com.advantal.responsepayload.shariaComplianceResponse;
import com.advantal.service.StockService;
import com.advantal.utils.Constant;
import com.advantal.utils.CsvExportFileResponse;
import com.advantal.utils.DateUtil;
import com.advantal.utils.MethodUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.advantal.utils.UtilityMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ExchangeRepository exchangeRepository;

	@Autowired
	private ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	private MethodUtil methodUtil;

	@Autowired
	private IndicesRepository indicesRepository;

	String apiResponse = "";

	@Autowired
	private CryptoRepository cryptoRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FavoriteStockRepository favoriteStockRepository;

	@Autowired
	private FavoriteCryptoRepository favoriteCryptoRepository;

	@Autowired
	private StockGainerRepository gainerRepository;

	@Autowired
	private StockLoserRepository loserRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Value("${spring.csvfileformatepath}")
	private String csvFileFormatePath;

	@Autowired
	private SariyaComplianceRepository sariyaComplianceRepository;

	@Autowired
	private StockProfileRepository stockProfileRepository;

	@Autowired
	private SaudiShariahComplianceRepository saudiShariahComplianceRepository;

	@Autowired
	private ShortingKeyRepository shortingKeyRepository;

	@Autowired
	private KeyExecutiveRepository executiveRepository;

	@Autowired
	private ShareHolderRepository shareHolderRepository;

	@Autowired
	private StockRecommendationRepository recommendationRepository;

	@Autowired
	private StockPriceTargetRepository priceTargetRepository;

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
	private CsvFileRepository csvFileRepository;

	@Autowired
	private IndicesMinuteGraphRepository indicesMinuteGraphRepository;

	@Autowired
	private IndicesYearGraphRepository indicesYearGraphRepository;

	@Autowired
	private IndicesDayGraphRepository indicesDayGraphRepository;

	@Override
	public Map<String, Object> getExchange(String country) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (!country.isBlank()) {
				List<Exchange> exchangeList = exchangeRepository.findAllExchangesList(country);
				if (!exchangeList.isEmpty() && exchangeList != null) {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, exchangeList);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, exchangeList);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				List<Exchange> exchangeList = exchangeRepository.findAll();
				if (!exchangeList.isEmpty() && exchangeList != null) {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, exchangeList);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, exchangeList);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	public Pageable getPageable(StockRequestPage stockRequest) {
		Pageable pageable = null;
		if (!stockRequest.getDirection().isBlank() && stockRequest.getDirection().equalsIgnoreCase("desc")) {
			pageable = PageRequest.of(stockRequest.getPageIndex(), stockRequest.getPageSize(),
					Sort.by(stockRequest.getSortBy()).descending());
		} else if (stockRequest.getDirection().isBlank()) {
			pageable = PageRequest.of(stockRequest.getPageIndex(), stockRequest.getPageSize(),
					Sort.by(stockRequest.getSortBy()).ascending());
		}
		return pageable;
	}

	@Override
	public Map<String, Object> getStocks(StockRequestPage stockRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			Page<Stock> page = null;
			List<Stock> stockList = new ArrayList<>();
			List<StockResponse> stockDetailsResponseList = new ArrayList<>();
			List<StockResponse> stockDetailsResponseList2 = new ArrayList<>();
			StockResponsePage stockResponsePage = new StockResponsePage();
			List<FavoriteStock> favoriteStockList = null;
			ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			if (stockRequest.getPageSize() > 0) {
				Pageable pageable = getPageable(stockRequest);
				if (pageable != null) {
					if (!stockRequest.getKeyWord().isBlank() && stockRequest.getCountry().isBlank()
							&& stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& stockRequest.getFilterBySector().isBlank()
							&& stockRequest.getFilterByShariahCompliance().isBlank()) {
						page = stockRepository.findAllStocks(stockRequest.getKeyWord(), pageable);
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					}
					if (!stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
							&& stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& stockRequest.getFilterBySector().isBlank()
							&& stockRequest.getFilterByShariahCompliance().isBlank()) {
						page = stockRepository.findAllStockByCountry(stockRequest.getKeyWord(),
								stockRequest.getCountry(), pageable);
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
							&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& stockRequest.getFilterBySector().isBlank()
							&& stockRequest.getFilterByShariahCompliance().isBlank()) {
						// will use
						if (stockRequest.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
							page = stockRepository.getSaudiStockListByIndex(stockRequest.getCountry(),
									stockRequest.getExchange(), pageable);
						} else if (stockRequest.getCountry().equalsIgnoreCase(Constant.UNITED_STATES)){
							page = stockRepository.getStockListBySorting(stockRequest.getCountry(),
									stockRequest.getExchange(), pageable);
						}
						//
//						page = stockRepository.getStockListBySorting(stockRequest.getCountry(),
//								stockRequest.getExchange(), pageable);
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
							&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& !stockRequest.getFilterBySector().isBlank()
							&& stockRequest.getFilterByShariahCompliance().isBlank()) {
						page = stockRepository.filterBySector(stockRequest.getCountry(),
								stockRequest.getFilterBySector(), pageable);
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
							&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& !stockRequest.getFilterBySector().isBlank()
							&& !stockRequest.getFilterByShariahCompliance().isBlank()) {
						if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("1")) {
							page = stockRepository.getStockListByCompliance(stockRequest.getCountry(),
									stockRequest.getFilterBySector(), pageable);
						} else if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("0")) {
							page = stockRepository.getStockListByNonCompliance(stockRequest.getCountry(),
									stockRequest.getFilterBySector(), pageable);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, Constant.INCORRECT_COMPLIANCE_VALUE_MESSAGE);
							log.info(Constant.INCORRECT_COMPLIANCE_VALUE_MESSAGE + "! status - {}",
									Constant.BAD_REQUEST);
							return map;
						}
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
							&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
							&& stockRequest.getFilterBySector().isBlank()
							&& !stockRequest.getFilterByShariahCompliance().isBlank()) {
						if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("1")) {
							page = stockRepository.getStockListByCompliance(stockRequest.getCountry(), pageable);
						} else if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("0")) {
							page = stockRepository.getStockListByNonCompliance(stockRequest.getCountry(), pageable);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, Constant.INCORRECT_COMPLIANCE_VALUE_MESSAGE);
							log.info(Constant.INCORRECT_COMPLIANCE_VALUE_MESSAGE + "! status - {}",
									Constant.BAD_REQUEST);
							return map;
						}
						favoriteStockList = favoriteStockRepository.findByUserId(stockRequest.getUserId());
					}

					if (page != null && page.getContent().size() > Constant.ZERO) {
						stockList = page.getContent();
						for (Stock stockObj : stockList) {
							StockResponse stockDetailsResponse = new StockResponse();
							BeanUtils.copyProperties(stockObj, stockDetailsResponse);

							stockDetailsResponse.setLogo(
									stockObj.getStockProfile() != null ? stockObj.getStockProfile().getLogo() : "");
							stockDetailsResponse.setInstrumentId(stockObj.getId());
							stockDetailsResponse.setChange(stockObj.getPrice_change().toString());
							stockDetailsResponse.setPercent_change(stockObj.getPercent_change().toString());
							stockDetailsResponse.setClose(stockObj.getPrice().toString());
							stockDetailsResponseList.add(stockDetailsResponse);
						}

						if (favoriteStockList.size() > 0) {
							for (StockResponse stockResponseObj : stockDetailsResponseList) {
								/* Setting favorite value true here */
								for (FavoriteStock favoriteStock : favoriteStockList) {
									if (stockResponseObj.getInstrumentId().equals(favoriteStock.getStock().getId())) {
										stockResponseObj.setFavorite(Constant.TRUE);
									}
								}
								stockDetailsResponseList2.add(stockResponseObj);
							}
							stockResponsePage.setStockDetailsResponseList(stockDetailsResponseList2);
						} else {
							stockResponsePage.setStockDetailsResponseList(stockDetailsResponseList);
						}

						stockResponsePage.setPageIndex(page.getNumber());
						stockResponsePage.setPageSize(page.getSize());
						stockResponsePage.setTotalElement(page.getTotalElements());
						stockResponsePage.setTotalPages(page.getTotalPages());
						stockResponsePage.setIsLastPage(page.isLast());
						stockResponsePage.setIsFirstPage(page.isFirst());

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_FOUND);
						map.put(Constant.DATA, stockResponsePage);
						log.info("Data found! status - {}", Constant.OK);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, stockResponsePage);
						log.info("Data not found! status - {}", Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.INVALID_USER_REQUEST_MESSAGE);
					log.info(Constant.INVALID_USER_REQUEST_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
					return map;
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
				log.info("Page size can't be less then one! status - {}", stockRequest.getPageSize());
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	/* new changes on 01 December */
	@Override
	public Map<String, Object> saveIndices(IndicesRequestPayload indicesRequestPayload) {
		Map<String, Object> response = new HashMap<>();
		try {
			/*--------------------------- Add Indices here ------------------------------*/
			if (indicesRequestPayload.getStatus().equals(Constant.ZERO)) {
				Indices indices = indicesRepository.findByCountryAndSymbol(indicesRequestPayload.getCountry(),
						indicesRequestPayload.getSymbol());
				if (indices != null) {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.ALREADY_EXIT_MESSAGE);
					log.info("Already exit ! status - {}", Constant.OK);
				} else {
					Indices newIndices = new Indices();
					newIndices.setSymbol(indicesRequestPayload.getSymbol());
					newIndices.setName(indicesRequestPayload.getName());
					newIndices.setCountry(indicesRequestPayload.getCountry());
					newIndices.setCurrency(indicesRequestPayload.getCurrency());
					newIndices.setExchange(indicesRequestPayload.getExchange());
					newIndices.setFullName(indicesRequestPayload.getFullName());
					newIndices.setType(indicesRequestPayload.getType());
					newIndices.setStatus(Constant.ONE);
					newIndices.setCreationDate(new Date());
					indicesRepository.save(newIndices);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.INDICES_ADDED);
					response.put(Constant.DATA, newIndices);
					log.info("new indices added successfully !! " + newIndices);
				}
				/*--------------------------- Update Indices here ------------------------------*/
			} else if (indicesRequestPayload.getStatus().equals(Constant.ONE)) {
				Indices indices = indicesRepository.findByIdAndSymbol(indicesRequestPayload.getId(),
						indicesRequestPayload.getSymbol());
				if (indices != null) {
					indices.setName(indicesRequestPayload.getName());
					indices.setCountry(indicesRequestPayload.getCountry());
					indices.setCurrency(indicesRequestPayload.getCurrency());
					indices.setExchange(indicesRequestPayload.getExchange());
					indices.setFullName(indicesRequestPayload.getFullName());
					indices.setType(indicesRequestPayload.getType());
					indices.setStatus(Constant.ONE);
					indices.setUpdationDate(new Date());
					indicesRepository.save(indices);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.INDICES_UPDATED);
					response.put(Constant.DATA, indices);
					log.info(" indices Updated successfully !! " + indices);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					log.info("Id & symbol not found in our database ! status - {}", Constant.OK);
				}
				/*--------------------------- Disable Indices here ------------------------------*/
			} else if (indicesRequestPayload.getStatus().equals(Constant.TWO)) {
				Indices indices = indicesRepository.findByIdAndSymbol(indicesRequestPayload.getId(),
						indicesRequestPayload.getSymbol());
				if (indices != null) {
					indices.setStatus(Constant.ZERO);
					indices.setUpdationDate(new Date());
					indicesRepository.save(indices);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.INDICES_DISABLE);
//					response.put(Constant.DATA, indices);
					log.info(" indices Disable successfully !! " + indices);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					log.info("Id & symbol not found in our database ! status - {}", Constant.OK);
				}
				/*--------------------------- enable Indices here ------------------------------*/
			} else if (indicesRequestPayload.getStatus().equals(Constant.THREE)) {
				Indices indices = indicesRepository.findByIdAndSymbol(indicesRequestPayload.getId(),
						indicesRequestPayload.getSymbol());
				if (indices != null) {
					indices.setStatus(Constant.ONE);
					indices.setUpdationDate(new Date());
					indicesRepository.save(indices);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.INDICES_ENABLE);
//					response.put(Constant.DATA, indices);
					log.info(" indices enable successfully !! " + indices);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					log.info("Id & symbol not found in our database ! status - {}", Constant.OK);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				response.put(Constant.MESSAGE, Constant.STATUS_INVALID_MESSAGE);
				log.info("invalid status ! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return response;
	}

	/* new implement 01 December */
	@Override
	public Map<String, Object> getIndices(RequestPage indicesSeriesRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Country> countriesList = null;
			Page<Indices> page = null;
			Country country = new Country();
			List<Indices> indicesList = new ArrayList<>();
			List<IndicesDetails> indicesDetailsResponseList = new ArrayList<>();
			List<IndicesDetails> indicesDetailsList = new ArrayList<>();
			IndicesResponsePage indicesResponsePage = new IndicesResponsePage();
			if (indicesSeriesRequest.getPageSize() > 0) {
				Pageable pageable = PageRequest.of(indicesSeriesRequest.getPageIndex(),
						indicesSeriesRequest.getPageSize());
				if (indicesSeriesRequest.getKeyWord() != null && !indicesSeriesRequest.getKeyWord().isBlank()
						&& indicesSeriesRequest.getCountry().isBlank()) {
					page = indicesRepository.findAllIndicesUsingSearch(indicesSeriesRequest.getKeyWord(), pageable);
					countriesList = countryRepository.findAll();
					if (countriesList.isEmpty()) {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (!indicesSeriesRequest.getCountry().isBlank()
						&& indicesSeriesRequest.getKeyWord().isBlank()) {
					country = countryRepository.findByCountry(indicesSeriesRequest.getCountry());
					if (country != null) {
						page = indicesRepository.findAllIndices(indicesSeriesRequest.getCountry(), pageable);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (indicesSeriesRequest.getCountry().isBlank() && indicesSeriesRequest.getKeyWord().isBlank()) {
					page = indicesRepository.findAllIndices(pageable);
					countriesList = countryRepository.findAll();
					if (countriesList.isEmpty()) {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (!indicesSeriesRequest.getCountry().isBlank()
						&& !indicesSeriesRequest.getKeyWord().isBlank()) {
					country = countryRepository.findByCountry(indicesSeriesRequest.getCountry());
					if (country != null) {
						page = indicesRepository.findAllIndicesWithCountryAndSearchingPagintion(
								indicesSeriesRequest.getCountry(), indicesSeriesRequest.getKeyWord(), pageable);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}

				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.INVALID_USER_REQUEST_MESSAGE);
					log.info(Constant.INVALID_USER_REQUEST_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
					return map;
				}
			} else {
				if (indicesSeriesRequest.getKeyWord() != null && !indicesSeriesRequest.getKeyWord().isBlank()
						&& indicesSeriesRequest.getCountry().isBlank()) {
					indicesList = indicesRepository.findAllIndicesUsingSearch(indicesSeriesRequest.getKeyWord());
					countriesList = countryRepository.findAll();
					if (countriesList.isEmpty()) {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (!indicesSeriesRequest.getCountry().isBlank()
						&& indicesSeriesRequest.getKeyWord().isBlank()) {
					country = countryRepository.findByCountry(indicesSeriesRequest.getCountry());
					if (country != null) {
						indicesList = indicesRepository.findAllIndicesWithCountry(indicesSeriesRequest.getCountry());
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (indicesSeriesRequest.getCountry().isBlank() && indicesSeriesRequest.getKeyWord().isBlank()) {
					indicesList = indicesRepository.findAllIndices();
					countriesList = countryRepository.findAll();
					if (countriesList.isEmpty()) {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}
				} else if (!indicesSeriesRequest.getCountry().isBlank()
						&& !indicesSeriesRequest.getKeyWord().isBlank()) {
					country = countryRepository.findByCountry(indicesSeriesRequest.getCountry());
					if (country != null) {
						indicesList = indicesRepository.findAllIndicesWithCountryAndSearching(
								indicesSeriesRequest.getCountry(), indicesSeriesRequest.getKeyWord());
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesResponsePage);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
						return map;
					}

				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.INVALID_USER_REQUEST_MESSAGE);
					log.info(Constant.INVALID_USER_REQUEST_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
					return map;
				}
			}

			if (page != null && page.getContent().size() > Constant.ZERO) {
				indicesList = page.getContent();
			}

			for (Indices indicesObj : indicesList) {
				IndicesDetails indicesDetails = new IndicesDetails();
				IndicesDetails indicesDetails2 = new IndicesDetails();
				BeanUtils.copyProperties(indicesObj, indicesDetails);
				indicesDetails.setChange(indicesObj.getPrice_change());
				BeanUtils.copyProperties(indicesObj, indicesDetails2);
				indicesDetails2.setChange(indicesObj.getPrice_change());

				/* new chnges */
				if (indicesSeriesRequest.getCountry().isBlank()) {
					for (Country country1 : countriesList) {
						if (indicesObj.getCountry().equalsIgnoreCase(country1.getCountry())) {
							BeanUtils.copyProperties(country1, country);
						}
					}
				}

				if (indicesDetails.getType().equals("market")) {
					/* It is done only for mobile side requirement, Creating array for */
					/* IndicesMarketDetails */
					List<KeyValueResponse> keyValueResponsesList = MethodUtil.indicesMarketData(indicesObj);
					/* End */
					indicesDetails.setKeyValueResponseList(keyValueResponsesList);

					indicesDetails.setDatetime(DateUtil.convertUnixTimeStampToStringDate(indicesObj.getTimestamp(),
							country.getTimeZone()));
				}

				indicesDetails2.setDatetime(
						DateUtil.convertUnixTimeStampToStringDate(indicesObj.getTimestamp(), country.getTimeZone()));

				log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				if (indicesDetails.getType().equals("market")) {
					/* Adding object into Market list */
					indicesDetailsResponseList.add(indicesDetails);
					log.info("Market indices details added into the Market List ! status - {}", Constant.OK);
				}
				/* Adding object into Indices List */
				indicesDetailsList.add(indicesDetails2);
				log.info("Indices details added into the Indices List ! status - {}", Constant.OK);
			}

			indicesDetailsResponseList = indicesDetailsResponseList.stream()
					.sorted(Comparator.comparing(IndicesDetails::getExchange)).toList();

			indicesResponsePage.setIndicesDetailsResponseList(indicesDetailsResponseList);
			indicesResponsePage.setIndicesList(indicesDetailsList);
			indicesResponsePage.setPageIndex(page != null ? page.getNumber() : null);
			indicesResponsePage.setPageSize(page != null ? page.getSize() : null);
			indicesResponsePage
					.setTotalElement(page != null ? page.getTotalElements() : indicesDetailsResponseList.size());
			indicesResponsePage.setTotalPages(page != null ? page.getTotalPages() : null);
			indicesResponsePage.setIsLastPage(page != null ? page.isLast() : null);
			indicesResponsePage.setIsFirstPage(page != null ? page.isFirst() : null);

			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
			map.put(Constant.DATA, indicesResponsePage);
			log.info("Record found! status - {}", Constant.OK);
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	public Short weekEndCount(int holidayCount, int dayOfWeek, String weekendStr) {
		String[] weekendArray = weekendStr.split(",");
		Short dayCount = 0;
		for (int i = 0; i < weekendArray.length; i++) {
			Short weekend = Short.valueOf(weekendArray[i]);
			if (dayOfWeek == weekend) {
				dayCount = (short) (dayCount + holidayCount + 1);
				break;
			} else if (dayOfWeek == weekend) {
				dayCount = (short) (dayCount + holidayCount + 2);
				break;
			}
		}
		return dayCount;
	}

	@Override
	public Map<String, Object> getStock(StockRequest request) {
		Map<String, Object> map = new HashMap<>();
		StockDetailsResponse stockDetailsResponse = new StockDetailsResponse();
		try {
			User user = userRepository.findByIdAndStatus(request.getUserId(), Constant.ONE);
			if (user != null) {
				Country country = countryRepository.findByCountry(request.getCountry());
				if (country != null) {
					Stock stock = stockRepository.findByIdAndSymbol(request.getInstrumentId(), request.getSymbol());
					if (stock != null) {
						BeanUtils.copyProperties(stock, stockDetailsResponse);
						stockDetailsResponse.setFavorite(stock.getFavorite());
						if (request.getCountry().equalsIgnoreCase(country.getCountry())) {
							if (country.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
								SaudiShariahCompliance saudiShariahCompliance = saudiShariahComplianceRepository
										.findBySymbol(request.getSymbol());
								if (saudiShariahCompliance != null) {
									if (saudiShariahCompliance.getCompliance_status().equalsIgnoreCase("compliance")) {
										stockDetailsResponse.setIsCompliance(true);
									}
								}
							} else if (country.getCountry().equalsIgnoreCase(Constant.UNITED_STATES)) {
								ShariaCompliance shariaCompliance = sariyaComplianceRepository
										.getStockByShariyaCompliance(request.getCountry(), request.getSymbol());
								if (shariaCompliance != null) {
									stockDetailsResponse.setIsCompliance(true);
								}
							}

							String curentTime = DateUtil.getTimeInZoneWise(ZoneId.of(country.getTimeZone()));
							log.info(country.getCountry().toUpperCase() + " Market - [ Time now : " + curentTime
									+ " | Open at : " + country.getMarketOpenTime() + " | Close at : "
									+ country.getMarketCloseTime() + " ]");
							Long currentLongTime = DateUtil.StringTimeToLongTime(curentTime);
							Long mktcloseLongTime = DateUtil.StringTimeToLongTime(country.getMarketCloseTime());
							Long mktOpenLongTime = DateUtil.StringTimeToLongTime(country.getMarketOpenTime());
							LocalDateTime currentDate = LocalDateTime.now(ZoneId.of(country.getTimeZone()));

							/* check is market open */
							Integer holidayCount = 0;
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							String date = currentDate.format(formatter);
//							List<Holiday> holidayList = holidayRepository.findByCountryAndYear(country.getCountry(),
//									currentDate.getYear());
							List<Holiday> holidayList = country.getHolidayList();
							if (!holidayList.isEmpty()) {
								List<Holiday> holidayList2 = new ArrayList<>();
								for (Holiday holiday : holidayList) {
									if (holiday.getYear() == currentDate.getYear()) {
										holidayList2.add(holiday);
									}
								}
								for (Holiday holiday : holidayList2) {
									if (holiday.getDate().toString().equals(date)) {
										holidayCount++;
									}
								}
							}

							if (weekEndCount(holidayCount, currentDate.getDayOfWeek().getValue(),
									country.getWeekends()) == 0) {
								if (currentLongTime >= mktOpenLongTime && mktcloseLongTime >= currentLongTime) {
									stockDetailsResponse.setIs_market_open(Constant.TRUE);
									stockDetailsResponse.setMktOpnClsDtTm(country.getMarketCloseTime());
								} else {
									stockDetailsResponse.setIs_market_open(Constant.FALSE);
									stockDetailsResponse.setMktOpnClsDtTm(country.getMarketOpenTime());
								}
							} else {
								stockDetailsResponse.setIs_market_open(Constant.FALSE);
								stockDetailsResponse.setMktOpnClsDtTm(country.getMarketOpenTime());
							}

							stockDetailsResponse.setWeekends(country.getWeekends());
							stockDetailsResponse.setClose(stock.getPrice().toString());
							stockDetailsResponse.setChange(stock.getPrice_change().toString());
							stockDetailsResponse.setPercent_change(stock.getPercent_change().toString());
							stockDetailsResponse.setAbout(stock.getStockProfile().getDescription());
							stockDetailsResponse.setDatetime(DateUtil.convertUnixTimeStampToStringDate(
									stockDetailsResponse.getTimestamp(), country.getTimeZone()));
							stockDetailsResponse.setLogo(stock.getStockProfile().getLogo());
							stockDetailsResponse.setInstrumentId(stock.getId());
							List<FavoriteStock> favoriteStockList = favoriteStockRepository
									.findByUserId(request.getUserId());
							/* check favorite */
							if (favoriteStockList.size() > 0) {
								for (FavoriteStock favoriteStock : favoriteStockList) {
									if (favoriteStock.getStock().getId().equals(stock.getId())) {
										stockDetailsResponse.setFavorite(Constant.TRUE);
									}
								}
							}
							String ftw_high, ftw_low;
							ftw_high = stock.getFtw_high().toString();
							ftw_low = stock.getFtw_low().toString();
							stockDetailsResponse.setFtw_high(ftw_high);
							stockDetailsResponse.setFtw_low(ftw_low); //
							/* It is done only for mobile side requirement, Creating array for */
							/* IndicesMarketDetails */
							List<KeyValueResponse> keyValueMarketDataList = new ArrayList<>();
							keyValueMarketDataList = MethodUtil.marketData(stock);
							List<KeyValueResponse> keyValueDetailsList = new ArrayList<>();
							keyValueDetailsList = MethodUtil.companyProfileData(stock.getStockProfile());

							Assets oldAssets = new Assets();
							boolean isFound = false;
							Portfolio oldPortfolio = new Portfolio();
							for (int i = 1; i < user.getPortfolioList().size(); i++) {
								if (user.getPortfolioList().get(i).getId().equals(request.getPortfolioId())) {
									isFound = true;
									BeanUtils.copyProperties(user.getPortfolioList().get(i), oldPortfolio);
									for (Wallet wallet : oldPortfolio.getWalletList()) {
										if (wallet.getStatus().equals(Constant.ONE)) {
											for (Assets assets : wallet.getAssetsList()) {
												if (request.getSymbol().equalsIgnoreCase(assets.getSymbol())) {
													BeanUtils.copyProperties(assets, oldAssets);
													break;
												}
											}
										}
									}
									break;
								}
							}
//							if (isFound == false) {
//								map.put(Constant.RESPONSE_CODE, Constant.OK);
//								map.put(Constant.MESSAGE, Constant.PORTFOLIO_ID_NOT_FOUND_MESSAGE);
//								log.info(Constant.PORTFOLIO_ID_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
//								return map;
//							}
							List<KeyValueResponse> keyValueAdvancMatrixList = null;
							if (oldAssets.getId() != null) {
								keyValueAdvancMatrixList = MethodUtil.getAdvanceMatrix(stock, oldAssets);
							}
							/* End */
							stockDetailsResponse.setKeyValueMarketDataList(keyValueMarketDataList);
							stockDetailsResponse.setKeyValueDetailsList(keyValueDetailsList); //
							stockDetailsResponse.setKeyValueAdvancMatrixList(keyValueAdvancMatrixList);
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
							map.put(Constant.DATA, stockDetailsResponse);
							log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.INSTRUMENT_NOT_FOUND_COUNTRY_NOT_MATCHED_MESSAGE);
							log.info(Constant.INSTRUMENT_NOT_FOUND_COUNTRY_NOT_MATCHED_MESSAGE + "! status - {}",
									Constant.OK);
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
						log.info(Constant.INSTRUMENT_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
					log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.USER_ID_NOT_FOUND_MESSAGE);
				log.info(Constant.USER_ID_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
			}
		} catch (IllegalArgumentException e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getProfile(StockRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (!apiResponse.isBlank()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> mapResponse = mapper.readValue(apiResponse, Map.class);
				Integer code = (Integer) mapResponse.get("code");
				if (code == null) {
					StockProfileResponse stockProfileResponse = new StockProfileResponse();
					stockProfileResponse = mapper.convertValue(mapResponse, StockProfileResponse.class);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, stockProfileResponse);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
					throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (JsonMappingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (JsonProcessingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getMovers(StockMoversRequest stockMoversRequest) {
		Map<String, Object> map = new HashMap<>();
		MoversResponse2 moversResponse = new MoversResponse2();
		List<MoversDetails> moversDetailList = new ArrayList<>();
		List<StockGainer> gainerList = null;
		List<StockLoser> loserList = null;
		try {
			if (stockMoversRequest.getCountry().equalsIgnoreCase(Constant.UNITED_STATES)) {
				if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.GAINERS)) {
					gainerList = gainerRepository.findByCountry(stockMoversRequest.getCountry());
				} else if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.LOSERS)) {
					loserList = loserRepository.findByCountry(stockMoversRequest.getCountry());
				}
			} else if (stockMoversRequest.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.GAINERS)) {
					gainerList = gainerRepository.findByCountry(stockMoversRequest.getCountry());
				} else if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.LOSERS)) {
					loserList = loserRepository.findByCountry(stockMoversRequest.getCountry());
				}
			}
			if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.GAINERS)) {
				if (gainerList.size() > 0) {
					List<FavoriteStock> favoriteStockList = favoriteStockRepository
							.findByUserId(stockMoversRequest.getUserId());
					if (favoriteStockList.size() > 0) {
						for (StockGainer gainer : gainerList) {
							Stock stock = stockRepository.findBySymbol(gainer.getSymbol());
							if (stock != null) {
								MoversDetails movers = new MoversDetails();
								BeanUtils.copyProperties(stock, movers);
								movers.setInstrumentId(stock.getId());
								movers.setLast(gainer.getClose());
								movers.setChange(gainer.getPriceChange());
								movers.setPercent_change(gainer.getPercentChange());
								movers.setLogo(gainer.getLogo());
								/* Setting favorite value true here */
								for (FavoriteStock favoriteStock : favoriteStockList) {
									if (favoriteStock.getStock().getId().equals(stock.getId())) {
										movers.setFavorite(Constant.TRUE);
									}
								}
								moversDetailList.add(movers);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE,
										gainer.getSymbol() + " : " + Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
								map.put(Constant.DATA, moversResponse);
								log.info("Symbol - " + gainer.getSymbol() + " - Not found! status - {}", Constant.OK);
								return map;
							}
						}
					} else {
						for (StockGainer gainer : gainerList) {
							Stock stock = stockRepository.findBySymbol(gainer.getSymbol());
							if (stock != null) {
								MoversDetails movers = new MoversDetails();
								BeanUtils.copyProperties(stock, movers);
								movers.setInstrumentId(stock.getId());
								movers.setLast(gainer.getClose());
								movers.setChange(gainer.getPriceChange());
								movers.setPercent_change(gainer.getPercentChange());
								movers.setLogo(gainer.getLogo());
								moversDetailList.add(movers);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE,
										gainer.getSymbol() + " : " + Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
								map.put(Constant.DATA, moversResponse);
								log.info("Symbol - " + gainer.getSymbol() + " - Not found! status - {}", Constant.OK);
							}
						}
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, moversResponse);
					log.info("Data not found! status - {}", Constant.OK);
					return map;
				}
			} else if (stockMoversRequest.getDirection().equalsIgnoreCase(Constant.LOSERS)) {
				if (loserList.size() > 0) {
					List<FavoriteStock> favoriteStockList = favoriteStockRepository
							.findByUserId(stockMoversRequest.getUserId());
					if (favoriteStockList.size() > 0) {
						for (StockLoser loser : loserList) {
							Stock stock = stockRepository.findBySymbol(loser.getSymbol());
							if (stock != null) {
								MoversDetails movers = new MoversDetails();
								BeanUtils.copyProperties(stock, movers);
								movers.setInstrumentId(stock.getId());
								movers.setLast(loser.getClose());
								movers.setChange(loser.getPriceChange());
								movers.setPercent_change(loser.getPercentChange());
								movers.setLogo(loser.getLogo());

								/* Setting favorite value true here */
								for (FavoriteStock favoriteStock : favoriteStockList) {
									if (favoriteStock.getStock().getId().equals(stock.getId())) {
										movers.setFavorite(Constant.TRUE);
									}
								}
								moversDetailList.add(movers);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE,
										loser.getSymbol() + " : " + Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
								map.put(Constant.DATA, moversResponse);
								log.info("Symbol - " + loser.getSymbol() + " - Not found! status - {}", Constant.OK);
								return map;
							}
						}
					} else {
						for (StockLoser loser : loserList) {
							Stock stock = stockRepository.findBySymbol(loser.getSymbol());
							if (stock != null) {
								MoversDetails movers = new MoversDetails();
								BeanUtils.copyProperties(stock, movers);
								movers.setInstrumentId(stock.getId());
								movers.setLast(loser.getClose());
								movers.setChange(loser.getPriceChange());
								movers.setPercent_change(loser.getPercentChange());
								movers.setLogo(loser.getLogo());
								moversDetailList.add(movers);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE,
										loser.getSymbol() + " : " + Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
								map.put(Constant.DATA, moversResponse);
								log.info("Symbol - " + loser.getSymbol() + " - Not found! status - {}", Constant.OK);
								return map;
							}
						}
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, moversResponse);
					log.info("Data not found! status - {}", Constant.OK);
					return map;
				}
			} else {
				log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
				throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
			}

			moversResponse.setValues(moversDetailList);
			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
			map.put(Constant.DATA, moversResponse);
			log.info("Data found! status - {}", Constant.OK);
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getTimeSeries(TimeSeriesRequest timeSeriesRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			Country country = countryRepository.findByCountry(timeSeriesRequest.getCountry());
			if (country != null) {
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_DAY)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.FIVE_MINUTE);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_WEEK)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.FIFTEEN_MINUTE);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_MONTH)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.THIRTY_MINUTE);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.ONE_DAY);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.ONE_DAY);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.FIVE_YEAR)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.ONE_WEEK);
				}
				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
					apiResponse = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, Constant.ONE_YEAR);
				}

				if (!apiResponse.isBlank()) {
					TimeSeriesResponse timeSeriesResponse = new TimeSeriesResponse();

					Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
					}.getType();
					List<TimeSeriesDetails> timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);

					if (!timeSeriesDetailsList.isEmpty()) {
						TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
						List<TimeSeriesDetails> timeSeriesDetailsList2 = new ArrayList<>();
						String previousDate = DateUtil.getPreviousDateTime(timeSeriesDetailsList.get(0).getDate(),
								timeSeriesRequest);
						LocalDateTime latestDate = DateUtil
								.stringToLocalDateTime(timeSeriesDetailsList.get(0).getDate());

						Boolean data = false;
						if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
							seriesDetails = timeSeriesDetailsList.get(timeSeriesDetailsList.size() - 1);
							timeSeriesDetailsList.remove(timeSeriesDetailsList.get(timeSeriesDetailsList.size() - 1));
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								timeSeriesDetailsList2.add(details);
							}
						} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.FIVE_YEAR)) {
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (details.getDate().contains(previousDate)) {
									BeanUtils.copyProperties(details, seriesDetails);
									data = true;
									break;
								}
							}
							latestDate = latestDate.minusDays(1824);
							while (data == false) {
								latestDate = latestDate.minusHours(24);
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								previousDate = latestDate.format(formatter);
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
							}
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (!details.getDate().contains(previousDate))
									timeSeriesDetailsList2.add(details);
								else
									break;
							}
						} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (details.getDate().contains(previousDate)) {
									BeanUtils.copyProperties(details, seriesDetails);
									data = true;
									break;
								}
							}
							latestDate = latestDate.minusDays(364);// minusDays
							while (data == false) {
								latestDate = latestDate.minusHours(24);// minusDays
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								previousDate = latestDate.format(formatter);
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
							}
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (!details.getDate().contains(previousDate))
									timeSeriesDetailsList2.add(details);
								else
									break;
							}
						} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (details.getDate().contains(previousDate)) {
									BeanUtils.copyProperties(details, seriesDetails);
									data = true;
									break;
								}
							}
							latestDate = latestDate.minusDays(179);// minusDays
							while (data == false) {
								latestDate = latestDate.minusHours(24);
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								previousDate = latestDate.format(formatter);
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
							}
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (!details.getDate().contains(previousDate))
									timeSeriesDetailsList2.add(details);
								else
									break;
							}
						} else {
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (details.getDate().contains(previousDate)) {
									BeanUtils.copyProperties(details, seriesDetails);
									data = true;
									break;
								}
							}
							while (data == false) {
								latestDate = latestDate.minusHours(24);// minusDays
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								previousDate = latestDate.format(formatter);
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
							}
							for (TimeSeriesDetails details : timeSeriesDetailsList) {
								if (!details.getDate().contains(previousDate))
									timeSeriesDetailsList2.add(details);
								else
									break;
							}
						}

						TimeSeriesDetails highestTimeSeries = timeSeriesDetailsList2.stream()
								.max(Comparator.comparing(TimeSeriesDetails::getHigh)).get();
						TimeSeriesDetails lowestTimeSeries = timeSeriesDetailsList2.stream()
								.min(Comparator.comparing(TimeSeriesDetails::getLow)).get();

						Collections.reverse(timeSeriesDetailsList2);

						timeSeriesResponse.setHigh(highestTimeSeries.getHigh());
						timeSeriesResponse.setLow(lowestTimeSeries.getLow());
						timeSeriesResponse.setPrevious_close(seriesDetails.getClose());
						timeSeriesResponse.setValues(timeSeriesDetailsList2);

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
						map.put(Constant.DATA, timeSeriesResponse);
						log.info("Data found! status - {}", Constant.OK);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
						map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE);
						log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
								Constant.SERVER_ERROR);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					map.put(Constant.MESSAGE, Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE);
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " ! status - {}", Constant.OK);
			}
		} catch (IllegalArgumentException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> addFavorite(FavoriteInstrumentRequest favoriteRequest) {
		Map<String, Object> map = new HashMap<>();
		List<FavoriteStock> favoriteStockList = null;
		List<FavoriteCrypto> favoriteCryptoList = null;
		try {
			User user = userRepository.findByIdAndStatus(favoriteRequest.getUserId(), Constant.ONE);
			if (user != null) {
				if (favoriteRequest.getStatus() == 1) {
					/* here favorite instrument is removing */
					if (favoriteRequest.getInstrumentType().equals("stock") && !favoriteRequest.getSymbol().isBlank()
							&& favoriteRequest.getCryptoId().isBlank()) {
						favoriteStockList = favoriteStockRepository.findByUserId(favoriteRequest.getUserId());
						if (!favoriteStockList.isEmpty()) {
							for (FavoriteStock instrument : favoriteStockList) {
								if (instrument.getStock().getId().equals(favoriteRequest.getInstrumentId())
										&& instrument.getUserId().equals(favoriteRequest.getUserId())) {
									favoriteStockRepository.deleteById(instrument.getId());

									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.REMOVED_FAVORITE_MESSAGE);
									log.info("This Instrument - " + favoriteRequest.getSymbol() + " - "
											+ Constant.REMOVED_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
									return map;
								}
							}
							Integer count = 0;
							for (FavoriteStock instrument : favoriteStockList) {
								if (!instrument.getStock().getId().equals(favoriteRequest.getInstrumentId())
										&& instrument.getUserId().equals(favoriteRequest.getUserId())) {
									count++;
								}
							}
							if (count > 0) {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE);
								log.info(Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE + "! status - {}", Constant.OK);
							}
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE);
							log.info(Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE + "! status - {}", Constant.OK);
						}
					} else if (favoriteRequest.getInstrumentType().equals("crypto")
							&& favoriteRequest.getSymbol().isBlank() && !favoriteRequest.getCryptoId().isBlank()) {
						favoriteCryptoList = favoriteCryptoRepository.findByUserId(favoriteRequest.getUserId());
						if (!favoriteCryptoList.isEmpty()) {
							for (FavoriteCrypto instrument : favoriteCryptoList) {
								if (instrument.getCrypto().getId().equals(favoriteRequest.getInstrumentId())
										&& instrument.getUserId().equals(favoriteRequest.getUserId())) {
									favoriteCryptoRepository.deleteById(instrument.getId());

									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.REMOVED_FAVORITE_MESSAGE);
									log.info("This Instrument - " + favoriteRequest.getCryptoId() + " - "
											+ Constant.REMOVED_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
									return map;
								}
							}
							Integer count = 0;
							for (FavoriteCrypto instrument : favoriteCryptoList) {
								if (!instrument.getCrypto().getId().equals(favoriteRequest.getInstrumentId())
										&& instrument.getUserId().equals(favoriteRequest.getUserId())) {
									count++;
								}
							}
							if (count > 0) {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE);
								log.info(Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE + "! status - {}", Constant.OK);
							}
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE);
							log.info(Constant.UNABLE_TO_REMOVE_INSTRUMENT_MESSAGE + "! status - {}", Constant.OK);
						}
					}
				} else if (favoriteRequest.getStatus() == 0) {
					/* Adding favorite instrument into favorite list */
					if (favoriteRequest.getInstrumentType().equals("stock") && !favoriteRequest.getSymbol().isBlank()
							&& favoriteRequest.getCryptoId().isBlank()) {
						favoriteStockList = favoriteStockRepository.findByUserId(favoriteRequest.getUserId());
						if (favoriteStockList.isEmpty()) {
							createFavoriteStock(favoriteRequest, user);
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.SAVED_FAVORITE_MESSAGE);
						} else {
							Integer count = 0;
							for (FavoriteStock instrument : favoriteStockList) {
								if (instrument.getStock().getId().equals(favoriteRequest.getInstrumentId())) {
									count++;
								}
							}
							if (count > 0) {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.ALREADY_FAVORITE_MESSAGE);
								log.info(Constant.ALREADY_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
							} else {
								createFavoriteStock(favoriteRequest, user);
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.SAVED_FAVORITE_MESSAGE);
							}
						}
					} else if (favoriteRequest.getInstrumentType().equals("crypto")
							&& favoriteRequest.getSymbol().isBlank() && !favoriteRequest.getCryptoId().isBlank()) {
						favoriteCryptoList = favoriteCryptoRepository.findByUserId(favoriteRequest.getUserId());
						if (favoriteCryptoList.isEmpty()) {
							createFavoriteCrypto(favoriteRequest, user);
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.SAVED_FAVORITE_MESSAGE);
						} else {
							Integer count = 0;
							for (FavoriteCrypto instrument : favoriteCryptoList) {
								if (instrument.getCrypto().getId().equals(favoriteRequest.getInstrumentId())
										&& instrument.getUserId().equals(favoriteRequest.getUserId())) {
									count++;
								}
							}
							if (count > 0) {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.ALREADY_FAVORITE_MESSAGE);
								log.info(Constant.ALREADY_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
							} else {
								createFavoriteCrypto(favoriteRequest, user);
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.SAVED_FAVORITE_MESSAGE);
							}
						}
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.STATUS_VALUE_INVALID_MESSAGE);
					log.info(Constant.STATUS_VALUE_INVALID_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.USER_ID_NOT_FOUND_MESSAGE);
				log.info(Constant.USER_ID_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	/* For create favorite stock */
	FavoriteStock createFavoriteStock(FavoriteInstrumentRequest favoriteRequest, User user) {
		FavoriteStock favoriteStock = new FavoriteStock();
		BeanUtils.copyProperties(favoriteRequest, favoriteStock);
		Stock stock = stockRepository.findByIdAndSymbol(favoriteRequest.getInstrumentId(), favoriteRequest.getSymbol());
		if (stock != null) {
			favoriteStock.setCreationDate(new Date());
			favoriteStock.setStatus(Constant.ONE);
			favoriteStock.setName(stock.getName());
			favoriteStock.setSymbol(stock.getSymbol());
			favoriteStock.setExchange(stock.getExchange());
			favoriteStock.setStock(stock);
			favoriteStock.setUserId(user.getId());
			favoriteStock = favoriteStockRepository.save(favoriteStock);
			log.info(Constant.SAVED_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
		} else {
			log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			throw new ResourceNotFoundException(Constant.DATA_NOT_FOUND_MESSAGE);
		}
		return favoriteStock;
	}

	/* For create favorite crypto */
	FavoriteCrypto createFavoriteCrypto(FavoriteInstrumentRequest favoriteRequest, User user) {
		FavoriteCrypto favoriteCrypto = new FavoriteCrypto();
		BeanUtils.copyProperties(favoriteRequest, favoriteCrypto);
		Crypto crypto = cryptoRepository.findByIdAndCryptoId(favoriteRequest.getInstrumentId(),
				favoriteRequest.getCryptoId());
		if (crypto != null) {
			favoriteCrypto.setCreationDate(new Date());
			favoriteCrypto.setStatus(Constant.ONE);
			favoriteCrypto.setSymbol(crypto.getSymbol());
			favoriteCrypto.setName(crypto.getName());
			favoriteCrypto.setCrypto(crypto);
			favoriteCrypto.setUserId(user.getId());
			favoriteCrypto = favoriteCryptoRepository.save(favoriteCrypto);
			log.info(Constant.SAVED_FAVORITE_MESSAGE + "! status - {}", Constant.OK);
		} else {
			log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.NOT_FOUND);
			throw new ResourceNotFoundException(Constant.DATA_NOT_FOUND_MESSAGE);
		}
		return favoriteCrypto;
	}

	@Override
	public Map<String, Object> getFavorites(FavoriteRequestPage favoriteRequestPage) {
		Map<String, Object> map = new HashMap<>();
		try {
			Page<FavoriteStock> page = null;
			Page<FavoriteCrypto> page2 = null;
			List<FavoriteStock> favoriteStockList = new ArrayList<>();
			List<FavoriteCrypto> favoriteCryptoList = new ArrayList<>();
			List<StockResponse> stockDetailsResponseList = new ArrayList<>();
			List<StockResponse> stockDetailsResponseList2 = new ArrayList<>();
			StockResponsePage stockResponsePage = new StockResponsePage();

			if (favoriteRequestPage.getPageSize() > 0) {
				Pageable pageable = PageRequest.of(favoriteRequestPage.getPageIndex(),
						favoriteRequestPage.getPageSize());
				if (favoriteRequestPage.getCountry().isBlank()
						&& favoriteRequestPage.getInstrument_type().equalsIgnoreCase("stock")) {
					page = favoriteStockRepository.findByUserIdAndStatus(favoriteRequestPage.getUserId(), Constant.ONE,
							pageable);
				} else if (!favoriteRequestPage.getCountry().isBlank() && favoriteRequestPage.getKeyWord().isBlank()
						&& favoriteRequestPage.getInstrument_type().equalsIgnoreCase("stock")) {
					page = favoriteStockRepository.findByUserIdAndCountryAndStatus(favoriteRequestPage.getUserId(),
							favoriteRequestPage.getCountry(), Constant.ONE, pageable);
				} else if (!favoriteRequestPage.getCountry().isBlank() && !favoriteRequestPage.getKeyWord().isBlank()
						&& favoriteRequestPage.getInstrument_type().equalsIgnoreCase("stock")) {
					page = favoriteStockRepository.findAllFavoriteStocks(favoriteRequestPage.getUserId(),
							favoriteRequestPage.getCountry(), favoriteRequestPage.getInstrument_type(),
							favoriteRequestPage.getKeyWord(), pageable);
				} else if (favoriteRequestPage.getCountry().isBlank() && favoriteRequestPage.getKeyWord().isBlank()
						&& favoriteRequestPage.getInstrument_type().equalsIgnoreCase("crypto")) {
					page2 = favoriteCryptoRepository.findByUserIdAndStatus(favoriteRequestPage.getUserId(),
							Constant.ONE, pageable);
					if (page2 != null && page2.getContent().size() > Constant.ZERO) {
						favoriteCryptoList = page2.getContent();
						String symbolString = "";
						ObjectMapper objMapper = new ObjectMapper();
						objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						for (FavoriteCrypto favoriteCryptoObj : favoriteCryptoList) {
							StockResponse stockDetailsResponse = new StockResponse();
							BeanUtils.copyProperties(favoriteCryptoObj.getCrypto(), stockDetailsResponse);
							stockDetailsResponse.setInstrumentId(favoriteCryptoObj.getCrypto().getId());
							stockDetailsResponseList.add(stockDetailsResponse);

							/* here creating Array of symbol */
							symbolString = symbolString + favoriteCryptoObj.getCrypto().getCryptoId() + ",";
						}
						/* Removing comma from string */
						if (symbolString != "") {
							int index = symbolString.lastIndexOf(",");
							symbolString = symbolString.substring(0, index);
						}

						apiResponse = thirdPartyApiUtil.getCryptoList(symbolString);
						if (!apiResponse.isBlank()) {
							Map<?, ?> map_crypto_response = objMapper.readValue(apiResponse, Map.class);
							for (StockResponse stockResponseObj : stockDetailsResponseList) {

								TsCryptoListResponse data = objMapper.convertValue(map_crypto_response,
										TsCryptoListResponse.class);
								for (TsCryptoListData cryptoListData : data.getData()) {
									if (cryptoListData.getId().equals(stockResponseObj.getCryptoId())) {
										for (TsCryptoListPrice tsCryptoListPrice : cryptoListData.getPrice()) {
											stockResponseObj.setPercent_change(String
													.valueOf(tsCryptoListPrice.getPrice_change_percentage_24h() * 100));
											stockResponseObj.setClose(tsCryptoListPrice.getPrice_latest().toString());
											stockResponseObj.setChange(methodUtil.calculatePercentageValue(
													tsCryptoListPrice.getPrice_change_percentage_24h() * 100,
													tsCryptoListPrice.getPrice_latest()));
											stockResponseObj.setCryptoId(cryptoListData.getId());
											stockResponseObj.setFavorite(Constant.TRUE);
											stockDetailsResponseList2.add(stockResponseObj);
										}
									}
								}
							}

							stockDetailsResponseList2 = stockDetailsResponseList2.stream()
									.sorted(Comparator.comparing(StockResponse::getSymbol)).toList();

							stockResponsePage.setStockDetailsResponseList(stockDetailsResponseList2);
							stockResponsePage.setPageIndex(page2.getNumber());
							stockResponsePage.setPageSize(page2.getSize());
							stockResponsePage.setTotalElement(page2.getTotalElements());
							stockResponsePage.setTotalPages(page2.getTotalPages());
							stockResponsePage.setIsLastPage(page2.isLast());
							stockResponsePage.setIsFirstPage(page2.isFirst());

							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
							map.put(Constant.DATA, stockResponsePage);
							log.info("Record found! status - {}", Constant.OK);
							return map;
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
							map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
							log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
							return map;
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, stockResponsePage);
						log.info("Record not found! status - {}", Constant.OK);
						return map;
					}
				} else if (favoriteRequestPage.getCountry().isBlank() && !favoriteRequestPage.getKeyWord().isBlank()
						&& favoriteRequestPage.getInstrument_type().equalsIgnoreCase("crypto")) {
					page2 = favoriteCryptoRepository.findAllFavoriteCrypto(favoriteRequestPage.getUserId(),
							favoriteRequestPage.getInstrument_type(), favoriteRequestPage.getKeyWord(), pageable);
					if (page2 != null && page2.getContent().size() > Constant.ZERO) {
						favoriteCryptoList = page2.getContent();
						String symbolString = "";
						ObjectMapper objMapper = new ObjectMapper();
						objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						for (FavoriteCrypto favoriteCryptoObj : favoriteCryptoList) {
							StockResponse stockDetailsResponse = new StockResponse();
							BeanUtils.copyProperties(favoriteCryptoObj.getCrypto(), stockDetailsResponse);
							stockDetailsResponse.setInstrumentId(favoriteCryptoObj.getCrypto().getId());
							stockDetailsResponseList.add(stockDetailsResponse);

							/* here creating Array of symbol */
							symbolString = symbolString + favoriteCryptoObj.getCrypto().getCryptoId() + ",";
						}
						/* Removing comma from string */
						if (symbolString != "") {
							int index = symbolString.lastIndexOf(",");
							symbolString = symbolString.substring(0, index);
						}

						apiResponse = thirdPartyApiUtil.getCryptoList(symbolString);
						if (!apiResponse.isBlank()) {
							Map<?, ?> map_crypto_response = objMapper.readValue(apiResponse, Map.class);
							for (StockResponse stockResponseObj : stockDetailsResponseList) {

								TsCryptoListResponse data = objMapper.convertValue(map_crypto_response,
										TsCryptoListResponse.class);
								for (TsCryptoListData cryptoListData : data.getData()) {
									if (cryptoListData.getId().equals(stockResponseObj.getCryptoId())) {
										for (TsCryptoListPrice tsCryptoListPrice : cryptoListData.getPrice()) {
											stockResponseObj.setPercent_change(String
													.valueOf(tsCryptoListPrice.getPrice_change_percentage_24h() * 100));
											stockResponseObj.setClose(tsCryptoListPrice.getPrice_latest().toString());
											stockResponseObj.setChange(methodUtil.calculatePercentageValue(
													tsCryptoListPrice.getPrice_change_percentage_24h() * 100,
													tsCryptoListPrice.getPrice_latest()));
											stockResponseObj.setCryptoId(cryptoListData.getId());
											stockResponseObj.setFavorite(Constant.TRUE);
											stockDetailsResponseList2.add(stockResponseObj);
										}
									}
								}
							}

							stockDetailsResponseList2 = stockDetailsResponseList2.stream()
									.sorted(Comparator.comparing(StockResponse::getSymbol)).toList();

							stockResponsePage.setStockDetailsResponseList(stockDetailsResponseList2);
							stockResponsePage.setPageIndex(page2.getNumber());
							stockResponsePage.setPageSize(page2.getSize());
							stockResponsePage.setTotalElement(page2.getTotalElements());
							stockResponsePage.setTotalPages(page2.getTotalPages());
							stockResponsePage.setIsLastPage(page2.isLast());
							stockResponsePage.setIsFirstPage(page2.isFirst());

							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
							map.put(Constant.DATA, stockResponsePage);
							log.info("Record found! status - {}", Constant.OK);
							return map;
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
							map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
							log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
							return map;
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, stockResponsePage);
						log.info("Record not found! status - {}", Constant.OK);
						return map;
					}
				}

				if ((page != null && page.getContent().size() > Constant.ZERO)) {
					favoriteStockList = page.getContent();
					for (FavoriteStock favoriteStockObj : favoriteStockList) {
						StockResponse stockDetailsResponse = new StockResponse();
						BeanUtils.copyProperties(favoriteStockObj.getStock(), stockDetailsResponse);

						stockDetailsResponse.setLogo(favoriteStockObj.getStock().getStockProfile() != null
								? favoriteStockObj.getStock().getStockProfile().getLogo()
								: "");
						stockDetailsResponse.setClose(String.valueOf(favoriteStockObj.getStock().getPrice()));
						stockDetailsResponse.setChange(String.valueOf(favoriteStockObj.getStock().getPrice_change()));
						stockDetailsResponse
								.setPercent_change(String.valueOf(favoriteStockObj.getStock().getPercent_change()));
						stockDetailsResponse.setInstrumentId(favoriteStockObj.getStock().getId());
						stockDetailsResponse.setFavorite(Constant.TRUE);
						stockDetailsResponseList.add(stockDetailsResponse);
					}

					stockDetailsResponseList = stockDetailsResponseList.stream()
							.sorted(Comparator.comparing(StockResponse::getSymbol)).toList();

					stockResponsePage.setStockDetailsResponseList(stockDetailsResponseList);
					stockResponsePage.setPageIndex(page.getNumber());
					stockResponsePage.setPageSize(page.getSize());
					stockResponsePage.setTotalElement(page.getTotalElements());
					stockResponsePage.setTotalPages(page.getTotalPages());
					stockResponsePage.setIsLastPage(page.isLast());
					stockResponsePage.setIsFirstPage(page.isFirst());

					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					map.put(Constant.DATA, stockResponsePage);
					log.info("Record found! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);//
					map.put(Constant.DATA, stockResponsePage);
					log.info("Record not found! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
				log.info("Page size can't be less then one! status - {}", favoriteRequestPage.getPageSize());
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> saveCryptoList(Integer limit, Integer offset) {
		Map<String, Object> map = new HashMap<>();
		try {

		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getSymbolSearch(String symbol, String outputsize) {
		Map<String, Object> map = new HashMap<>();
		String logo_response;
		try {
			apiResponse = thirdPartyApiUtil.getSymbolSearch(symbol, outputsize);
			if (!apiResponse.isBlank()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> mapResponse = mapper.readValue(apiResponse, Map.class);
				Integer code = (Integer) mapResponse.get("code");
				if (code == null) {
					SearchResponse searchResponse = new SearchResponse();
					searchResponse = mapper.convertValue(mapResponse, SearchResponse.class);
					List<SearchData> list = new ArrayList<SearchData>();
					for (SearchData data : searchResponse.getData()) {
						logo_response = thirdPartyApiUtil.getLogo(data.getSymbol(), data.getCountry());
						StockLogo logo = new StockLogo();
						if (!logo_response.isBlank()) {
							Map<?, ?> map_logo_response = mapper.readValue(logo_response, Map.class);
							Integer logo_code;
							logo_code = (Integer) map_logo_response.get("code");
							if (logo_code == null) {
								logo = mapper.convertValue(map_logo_response, StockLogo.class);
								data.setLogo(logo.getUrl());
								list.add(data);
							}
						}
					}
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, list);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
					throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (JsonMappingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (JsonProcessingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getAnalystRating(String symbol) {
		Map<String, Object> map = new HashMap<String, Object>();
		String response = "";
		AnalystResponse analystResponse = new AnalystResponse();
		try {
			if (!response.isBlank()) {
				log.info("third party data found susscessfully! status:- " + Constant.OK);
				Gson gson = new Gson();
				AnalystDataResponse analystDataResponse = gson.fromJson(response, AnalystDataResponse.class);
				if (analystDataResponse.getStatus().equals("ok")) {
					analystResponse.setBuy(analystDataResponse.getTrends().getCurrent_month().getBuy() + "%");
					analystResponse.setHold(analystDataResponse.getTrends().getCurrent_month().getHold() + "%");
					analystResponse.setSell(analystDataResponse.getTrends().getCurrent_month().getSell() + "%");
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					map.put(Constant.DATA, analystResponse);
					log.info("Record found ! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, analystResponse);
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, analystResponse);
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getPriceTarget(String symbol, String country) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			PriceTargetResponse priceTargetResponse = new PriceTargetResponse();
			PriceTarget priceTarget = new PriceTarget();
//			String response = thirdPartyApiUtil.getPriceTarget(symbol, country);
//			log.info("third party data found susscessfully! status:-" + Constant.OK);
//			if (!response.isBlank()) {
//				Gson gson = new Gson();
//				PriceTargetDataResponse dataResponse = gson.fromJson(response, PriceTargetDataResponse.class);
//				if (dataResponse != null && dataResponse.getStatus().equals("ok")) {
//					BeanUtils.copyProperties(dataResponse.getPrice_target(), priceTarget);
//					map.put(Constant.RESPONSE_CODE, Constant.OK);
//					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
//					map.put(Constant.DATA, priceTarget);
//					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
//				} else {
//					map.put(Constant.RESPONSE_CODE, Constant.OK);
//					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
//					map.put(Constant.DATA, priceTarget);
//					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				}
//			} else {
//				map.put(Constant.RESPONSE_CODE, Constant.OK);
//				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
//				map.put(Constant.DATA, priceTarget);
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getInstitutionalHolders(String symbol, String exchange) {
		Map<String, Object> map = new HashMap<>();
		List<InstitutionalHolders> list = new ArrayList<>();
		Double total_institutions_parcentage = 0.0, othershared = 0.0;
		try {
			if (!apiResponse.isBlank()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> mapResponse = mapper.readValue(apiResponse, Map.class);
				Integer code = (Integer) mapResponse.get("code");
				if (code == null) {
					ShareHolderDataResponse shareHolderDataResponse = new ShareHolderDataResponse();
					shareHolderDataResponse = mapper.convertValue(mapResponse, ShareHolderDataResponse.class);

					for (InstitutionalHolders holders : shareHolderDataResponse.getInstitutional_holders()) {
						if (holders.getPercent_held() != null) {
							total_institutions_parcentage = total_institutions_parcentage + holders.getPercent_held();
						}
						list.add(holders);
					}

					shareHolderDataResponse.setInstitutions(total_institutions_parcentage);
					othershared = 100 - total_institutions_parcentage;
					shareHolderDataResponse.setOthers(othershared);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, shareHolderDataResponse);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
					throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (JsonMappingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (JsonProcessingException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> shariyaCompliance(MultipartFile docfile, String fileType) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (docfile != null && !docfile.isEmpty()) {
				String extension = FilenameUtils.getExtension(docfile.getOriginalFilename());
				if (extension.equals("csv")) {
					ComplianceResponse complianceResponse = UtilityMethods.getDataFromCSVFile(docfile,
							csvFileFormatePath, fileType);
					if (complianceResponse != null) {
						if (!complianceResponse.getSariaComplianceList().isEmpty()) {
							if (fileType.equalsIgnoreCase(Constant.SAUDI_AAOIFI_COMPLIANCE)) {
								sariyaComplianceRepository.deleteAllByCountry(Constant.SAUDI_ARABIA);
							} else if (fileType.equalsIgnoreCase(Constant.USA_AAOIFI_COMPLIANCE)) {
								sariyaComplianceRepository.deleteAllByCountry(Constant.UNITED_STATES);
							}
							log.info("Old data deleted from server! status - {}", Constant.OK);
							log.info("Now Started to uploading data from file to Database ! status - {}", Constant.OK);

							sariyaComplianceRepository.saveAll(complianceResponse.getSariaComplianceList());
						} else if (!complianceResponse.getSaudiShariahCompliancesList().isEmpty()) {
							saudiShariahComplianceRepository.deleteAll();

							log.info("Old data deleted from server! status - {}", Constant.OK);
							log.info("Now Started to uploading data from file to Database ! status - {}", Constant.OK);

							saudiShariahComplianceRepository
									.saveAll(complianceResponse.getSaudiShariahCompliancesList());
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
							map.put(Constant.MESSAGE, Constant.FILE_UPLOAD_FAILED_MESSAGE);
							log.info(Constant.FILE_UPLOAD_FAILED_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
							return map;
						}

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_IMPORT_SUCCESS_MESSAGE);
						log.info(Constant.DATA_IMPORT_SUCCESS_MESSAGE + "! status - {}", Constant.OK);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
						map.put(Constant.MESSAGE, Constant.FILE_UPLOAD_FAILED_MESSAGE);
						log.info(Constant.FILE_UPLOAD_FAILED_MESSAGE + "! status - {}", Constant.BAD_REQUEST);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.NOT_A_CSV_FILE_MESSAGE);
					log.info("The file Formate should be CSV type(Ex: .csv) ! status - {}", Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.FILE_UPLOAD_FAILED_MESSAGE);
				log.info("File uploading failed! please try again! status - {}", Constant.BAD_REQUEST);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> getIndicesSeries(TimeSeriesRequest timeSeriesRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			IndicesTimeSeriesResponse indicesTimeSeriesResponse = new IndicesTimeSeriesResponse();
			Country country = countryRepository.findByCountry(timeSeriesRequest.getCountry());
			if (country != null) {
				Indices indices = indicesRepository.findBySymbol(timeSeriesRequest.getSymbol());
				if (indices != null) {
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_DAY)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.FIVE_MINUTE);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_WEEK)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest,
								Constant.FIFTEEN_MINUTE);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_MONTH)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.THIRTY_MINUTE);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.ONE_DAY);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.ONE_DAY);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.FIVE_YEAR)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.ONE_WEEK);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
						apiResponse = thirdPartyApiUtil.getIndicesTimeSeries(timeSeriesRequest, Constant.ONE_YEAR);
					}
					if (!apiResponse.isBlank()) {
						Double change, percentChange;
						TimeSeriesResponse timeSeriesResponse = new TimeSeriesResponse();
						Type collectionType = new TypeToken<List<TimeSeriesDetails>>() {
						}.getType();
						List<TimeSeriesDetails> timeSeriesDetailsList = null;
						try {
							timeSeriesDetailsList = new Gson().fromJson(apiResponse, collectionType);
						} catch (JsonSyntaxException e) {
							log.error("Exception : " + e.getMessage());
						}

						if (!timeSeriesDetailsList.isEmpty()) {
							/* functionality for graph */
							TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
							List<TimeSeriesDetails> timeSeriesDetailsList2 = new ArrayList<>();
							String previousDate = DateUtil.getPreviousDateTime(timeSeriesDetailsList.get(0).getDate(),
									timeSeriesRequest);
							LocalDateTime latestDate = DateUtil
									.stringToLocalDateTime(timeSeriesDetailsList.get(0).getDate());

							Boolean data = false;
							if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
								seriesDetails = timeSeriesDetailsList.get(timeSeriesDetailsList.size() - 1);
								timeSeriesDetailsList
										.remove(timeSeriesDetailsList.get(timeSeriesDetailsList.size() - 1));
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									timeSeriesDetailsList2.add(details);
								}
							} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.FIVE_YEAR)) {
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
								latestDate = latestDate.minusDays(1824);
								while (data == false) {
									latestDate = latestDate.minusHours(24);
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									previousDate = latestDate.format(formatter);
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (details.getDate().contains(previousDate)) {
											BeanUtils.copyProperties(details, seriesDetails);
											data = true;
											break;
										}
									}
								}
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (!details.getDate().contains(previousDate))
										timeSeriesDetailsList2.add(details);
									else
										break;
								}
							} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
								latestDate = latestDate.minusDays(364);
								while (data == false) {
									latestDate = latestDate.minusHours(24);// minusDays
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									previousDate = latestDate.format(formatter);
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (details.getDate().contains(previousDate)) {
											BeanUtils.copyProperties(details, seriesDetails);
											data = true;
											break;
										}
									}
								}
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (!details.getDate().contains(previousDate))
										timeSeriesDetailsList2.add(details);
									else
										break;
								}
							} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
								latestDate = latestDate.minusDays(179);// minusDays
								while (data == false) {
									latestDate = latestDate.minusHours(24);
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									previousDate = latestDate.format(formatter);
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (details.getDate().contains(previousDate)) {
											BeanUtils.copyProperties(details, seriesDetails);
											data = true;
											break;
										}
									}
								}
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (!details.getDate().contains(previousDate))
										timeSeriesDetailsList2.add(details);
									else
										break;
								}
							} else {
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (details.getDate().contains(previousDate)) {
										BeanUtils.copyProperties(details, seriesDetails);
										data = true;
										break;
									}
								}
								while (data == false) {
									latestDate = latestDate.minusHours(24);// minusDays
									DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
									previousDate = latestDate.format(formatter);
									for (TimeSeriesDetails details : timeSeriesDetailsList) {
										if (details.getDate().contains(previousDate)) {
											BeanUtils.copyProperties(details, seriesDetails);
											data = true;
											break;
										}
									}
								}
								for (TimeSeriesDetails details : timeSeriesDetailsList) {
									if (!details.getDate().contains(previousDate))
										timeSeriesDetailsList2.add(details);
									else
										break;
								}
							}
							Collections.reverse(timeSeriesDetailsList2);
							/* End graph */

							/* functionality for indices market data */
							IndicesDetails indicesDetails = new IndicesDetails();
							BeanUtils.copyProperties(indices, indicesDetails);
							TimeSeriesDetails highestTimeSeries = timeSeriesDetailsList2.stream()
									.max(Comparator.comparing(TimeSeriesDetails::getHigh)).get();
							TimeSeriesDetails lowestTimeSeries = timeSeriesDetailsList2.stream()
									.min(Comparator.comparing(TimeSeriesDetails::getLow)).get();
							change = Double.parseDouble(
									timeSeriesDetailsList2.get(timeSeriesDetailsList2.size() - 1).getClose())
									- Double.parseDouble(
											seriesDetails.getClose());/* change = prev.close - current price */
							percentChange = (change / Double.parseDouble(seriesDetails.getClose()) * 100);

							indicesDetails.setChange(change.toString());
							indicesDetails.setPercent_change(percentChange.toString());
							indicesDetails.setDatetime(DateUtil.convertUnixTimeStampToStringDate(indices.getTimestamp(),
									country.getTimeZone()));
							indicesDetails.setKeyValueResponseList(
									MethodUtil.indicesMarketData(indices));/* IndicesMarketDetails */
							/* End indices market data */

							timeSeriesResponse.setHigh(highestTimeSeries.getHigh());
							timeSeriesResponse.setLow(lowestTimeSeries.getLow());
							timeSeriesResponse.setPrevious_close(seriesDetails.getClose());
							timeSeriesResponse.setValues(timeSeriesDetailsList2);
							indicesTimeSeriesResponse.setIndicesDetails(indicesDetails);
							indicesTimeSeriesResponse.setTimeSeriesResponse(timeSeriesResponse);

							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
							map.put(Constant.DATA, indicesTimeSeriesResponse);
							log.info("Data found! status - {}", Constant.OK);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE);
							log.info(Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}", Constant.OK);
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
						map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.SYMBOL_NOT_FOUND);
					log.info(Constant.SYMBOL_NOT_FOUND + " status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				log.error("Country not found : status - {}", Constant.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getKeyExecutive(String symbol, String country) {
		Map<String, Object> map = new HashMap<String, Object>();
		KeyExecutiveResponse keyExecutiveResponse = new KeyExecutiveResponse();
		List<KeyExecutiveRes> list = new ArrayList<KeyExecutiveRes>();
		try {
			if (!symbol.isBlank() && !country.isBlank()) {
				List<KeyExecutive> executiveList = executiveRepository.findBySymbolAndCountry(symbol, country);
				if (!executiveList.isEmpty()) {
					log.info(" KeyExecutive list found :- " + executiveList);
					Integer maleCount = 0, femaleCount = 0;
					for (KeyExecutive executive : executiveList) {
						KeyExecutiveRes res = new KeyExecutiveRes();
						BeanUtils.copyProperties(executive, res);
						if (executive.getYearBorn() != null) {
							Year year = Year.now();
							Integer age = year.getValue();
							age = age - res.getYearBorn();
							res.setAge(age);
							list.add(res);
						}
						if (executive.getGender() != null && !executive.getGender().isBlank()
								&& executive.getGender().equalsIgnoreCase("male")) {
							maleCount++;
						} else if (executive.getGender() != null && !executive.getGender().isBlank()
								&& executive.getGender().equalsIgnoreCase("female")) {
							femaleCount++;
						}
					}
					keyExecutiveResponse.setKey_executives(list);
					keyExecutiveResponse.setFemaleCount(femaleCount);
					keyExecutiveResponse.setMaleCount(maleCount);
					keyExecutiveResponse.setFemaleRatio((femaleCount * 100.0f) / (maleCount + femaleCount));
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, keyExecutiveResponse);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, keyExecutiveResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.SYMBOL_AND_COUNTRY_NOT_BLANK);
				log.error("Symbol and Country can't be blank or empty!! :" + Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getWidgets(WidgetsRequest widgetsRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		WidgetResponse widgetResponse = new WidgetResponse();
		ShareHolderDataResponse shareHolderDataResponse = new ShareHolderDataResponse();
		AnalystResponse analystResponse = new AnalystResponse();
		List<InstitutionalHolders> holdersList = new ArrayList<InstitutionalHolders>();
		List<ShareHolder> shareHolders = new ArrayList<ShareHolder>();
		Pageable pageable = null;
		Page<ShareHolder> page = null;
		try {
			/* getting share holder */
			if (widgetsRequest.getPageSize() > 0) {
				pageable = PageRequest.of(widgetsRequest.getPageIndex(), widgetsRequest.getPageSize());
				page = shareHolderRepository.findAllInstitutionalHolders(widgetsRequest.getSymbol(),
						widgetsRequest.getExchange(), pageable);
				if (page != null) {
					shareHolders = page.getContent();
					for (ShareHolder holder : shareHolders) {
						InstitutionalHolders institutionalHolders = new InstitutionalHolders();
						BeanUtils.copyProperties(holder, institutionalHolders);
						institutionalHolders.setHolder(holder.getShareHolderName());
						holdersList.add(institutionalHolders);
					}
					shareHolderDataResponse.setPageIndex(page.getNumber());
					shareHolderDataResponse.setPageSize(page.getSize());
					shareHolderDataResponse.setIsFirstPage(page.isFirst());
					shareHolderDataResponse.setIsLastPage(page.isLast());
					shareHolderDataResponse.setTotalElement(page.getTotalElements());
					shareHolderDataResponse.setTotalPages(page.getTotalPages());
					shareHolderDataResponse.setInstitutional_holders(holdersList);
					shareHolderDataResponse.setInstitutions(null);
					shareHolderDataResponse.setOthers(null);
					widgetResponse.setShareHolderDataResponse(shareHolderDataResponse);
				} else {
					widgetResponse.setShareHolderDataResponse(shareHolderDataResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				log.info("Page size can't be less then one! status - {}", widgetsRequest.getPageSize());
			}
			/* getting Analyst rating data in our local database */
			StockRecommendation recommendation = recommendationRepository
					.findBySymbolAndStatus(widgetsRequest.getSymbol(), Constant.ONE);
			if (recommendation != null) {
				analystResponse.setBuy(recommendation.getBuy().toString());
				analystResponse.setHold(recommendation.getHold().toString());
				analystResponse.setRating(recommendation.getRating());
				analystResponse.setSell(recommendation.getSell().toString());
				widgetResponse.setAnalystResponse(analystResponse);
				log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", analystResponse);
			} else {
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}
			/* getting Price target data in our local database */
			StockPriceTarget priceTarget = priceTargetRepository.findBySymbol(widgetsRequest.getSymbol());
			if (priceTarget != null) {
				PriceTarget target = new PriceTarget();
				BeanUtils.copyProperties(priceTarget, target);
				widgetResponse.setPriceTargetResponse(target);
			} else {
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}

			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.DATA, widgetResponse);
			log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", shareHolderDataResponse);

		} catch (IllegalArgumentException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getSectors(String country) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!country.isBlank()) {
				List<Object> sectorList = new ArrayList<>();
				List<Object> stockProfileList = stockProfileRepository.getSectors(country);
				if (!stockProfileList.isEmpty()) {
					for (Object stockProfile : stockProfileList) {
						sectorList.add(stockProfile);
					}
					sectorList.removeAll(Arrays.asList("", null));
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, sectorList);
					log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, sectorList);
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_BLANK);
				log.info(Constant.COUNTRY_NOT_BLANK + "! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getShariyaCompliance(ShariyaComplianceRequest shariyaComplianceRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		ShariaCompliance shariaCompliance = new ShariaCompliance();
		SaudiShariahCompliance saudiShariahCompliance = new SaudiShariahCompliance();
		shariaComplianceResponse shariaComplianceResponse = new shariaComplianceResponse();
		try {
			/* Checking AAOIFI Compliance and Non Compliance Data */
			shariaCompliance = sariyaComplianceRepository.getStockByShariyaCompliance(
					shariyaComplianceRequest.getCountry(), shariyaComplianceRequest.getTickerSymbol());
			if (shariaCompliance != null) {
				if (shariyaComplianceRequest.getCountry().equals("united states")) {
					BeanUtils.copyProperties(shariaCompliance, shariaComplianceResponse);
					/* calculate the revenue */
					Float revenue = shariaCompliance.getRevenueBreakdownDoubtful()
							+ shariaCompliance.getRevenueBreakdownNotHalal();
					if (revenue > 5) {
						shariaComplianceResponse.setRevenueBreakdownResult("FAIL");
					} else {
						shariaComplianceResponse.setRevenueBreakdownResult("PASS");
					}

//					String interestBearingDebt = shariaCompliance.getInterestBearingDebt();
//					String[] ss = interestBearingDebt.split(" ");
//					System.out.println("result " + ss[ss.length - 3] + " percentage " + ss[ss.length - 1]);
//					shariaComplianceResponse.setInterestBearingDebtResult(ss[ss.length - 3].toUpperCase());
//					shariaComplianceResponse.setInterestBearingDebtPercentage(ss[ss.length - 1]);
//					String interestBearingSecuritiesAndAssets = shariaCompliance
//							.getInterestBearingSecuritiesAndAssets();
//					String[] sc = interestBearingSecuritiesAndAssets.split(" ");
//					shariaComplianceResponse
//							.setInterestBearingSecuritiesAndAssetsResult(sc[sc.length - 3].toUpperCase());
//					shariaComplianceResponse.setInterestBearingSecuritiesAndAssetsPercentage(sc[sc.length - 1]);
//					shariaComplianceResponse.setLastUpdated(shariaCompliance.getLastUpdated());
//					shariaComplianceResponse.setSource(shariaCompliance.getSource());
//					shariaComplianceResponse.setAaoifiComplianceFlag("YES");
//					shariaComplianceResponse.setAlRajhiComplianceFlag("NO");

//					String interestBearingDebt = shariaCompliance.getInterestBearingDebt();
//					String[] ss = interestBearingDebt.split(" ");
//					System.out.println("result " + ss[ss.length - 3] + " percentage " + ss[ss.length - 1]);
					shariaComplianceResponse
							.setInterestBearingDebtResult(shariaCompliance.getInterestBearingDebtStatus());
					shariaComplianceResponse.setInterestBearingDebtPercentage(
							shariaCompliance.getInterestBearingDebtPercentage().toString());
//					String interestBearingSecuritiesAndAssets = shariaCompliance
//							.getInterestBearingSecuritiesAndAssets();
//					String[] sc = interestBearingSecuritiesAndAssets.split(" ");
					shariaComplianceResponse.setInterestBearingSecuritiesAndAssetsResult(
							shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus());
					shariaComplianceResponse.setInterestBearingSecuritiesAndAssetsPercentage(
							shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage().toString());
					shariaComplianceResponse.setLastUpdated(shariaCompliance.getLastUpdated());

					shariaComplianceResponse.setSource(shariaCompliance.getSource());
					shariaComplianceResponse.setAaoifiComplianceFlag("YES");
					shariaComplianceResponse.setAlRajhiComplianceFlag("NO");
				} else if (shariyaComplianceRequest.getCountry().equals("saudi arabia")) {
					BeanUtils.copyProperties(shariaCompliance, shariaComplianceResponse);
					/* calculate the revenue */
					Float revenue = shariaCompliance.getRevenueBreakdownDoubtful()
							+ shariaCompliance.getRevenueBreakdownNotHalal();
					if (revenue > 5) {
						shariaComplianceResponse.setRevenueBreakdownResult("FAIL");
					} else {
						shariaComplianceResponse.setRevenueBreakdownResult("PASS");
					}

					shariaComplianceResponse.setInterestBearingDebtResult(
							shariaCompliance.getInterestBearingDebtStatus().toUpperCase());
					shariaComplianceResponse.setInterestBearingDebtPercentage(
							shariaCompliance.getInterestBearingDebtPercentage().toString());
					/* interestBearingSecuritiesAndAssets */
					shariaComplianceResponse.setInterestBearingSecuritiesAndAssetsResult(
							shariaCompliance.getInterestBearingSecuritiesAndAssetsStatus().toUpperCase());
					shariaComplianceResponse.setInterestBearingSecuritiesAndAssetsPercentage(
							shariaCompliance.getInterestBearingSecuritiesAndAssetsPercentage().toString());
					shariaComplianceResponse.setLastUpdated(shariaCompliance.getLastUpdated());
					shariaComplianceResponse.setSource(shariaCompliance.getSource());

					if (shariaCompliance.getComplainceDegree() != 0) {
						shariaComplianceResponse.setAaoifiComplianceFlag("YES");
					} else {
						shariaComplianceResponse.setAaoifiComplianceFlag("NO");
					}

					/* Checking Alrajhi Compliance and Non Compliance Data */
					saudiShariahCompliance = saudiShariahComplianceRepository
							.findBySymbol(shariyaComplianceRequest.getTickerSymbol());
					if (saudiShariahCompliance != null) {
						if (saudiShariahCompliance.getCompliance_status().equals("compliance")) {
							shariaComplianceResponse.setAlRajhiComplianceFlag("YES");
						} else {
							shariaComplianceResponse.setAlRajhiComplianceFlag("NO");
						}
						shariaComplianceResponse.setSaudiLastUpdated(saudiShariahCompliance.getLastUpdated());
						shariaComplianceResponse.setSaudiSource(saudiShariahCompliance.getSource());
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						map.put(Constant.DATA, shariaCompliance);
						log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
						return map;
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
					log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.BAD_REQUEST);
					return map;
				}

				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
				map.put(Constant.DATA, shariaComplianceResponse);
				log.info(Constant.DATA_FOUND_MESSAGE + "! status - {}", Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, shariaCompliance);
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> saveCountry(CountryResquest countryResquest) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			/*------------------- Add Country here ------------------*/
			if (countryResquest.getStatus().equals(Constant.ZERO)) {
				Country old_country_exchange = countryRepository.findByCountryAndExchange(countryResquest.getCountry(),
						countryResquest.getExchange());
				if (old_country_exchange != null) {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.COUNTRY_AND_EXCHANGE_ALREADY);
					log.info(Constant.OK + " ! status - {}", Constant.COUNTRY_AND_EXCHANGE_ALREADY);
				} else {
					Country country = new Country();
					country.setCountry(countryResquest.getCountry());
					country.setExchange(countryResquest.getExchange());
					country.setIntervalForUpdateInstrument(countryResquest.getIntervalForUpdateInstrument());
					country.setMarketCloseTime(countryResquest.getMarketCloseTime());
					country.setMarketOpenTime(countryResquest.getMarketOpenTime());
					country.setTimeZone(countryResquest.getTimeZone());
					countryRepository.save(country);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.COUNTRY_ADDED);
					map.put(Constant.DATA, country);
					log.info(" country added successfully ! status - {} " + Constant.OK + country);
				}
				/*------------------- Update Country here ------------------*/
			} else if (countryResquest.getStatus().equals(Constant.ONE)) {
				Country country = countryRepository.findById(countryResquest.getId()).orElse(null);
				if (country != null) {
					country.setIntervalForUpdateInstrument(countryResquest.getIntervalForUpdateInstrument());
					country.setMarketCloseTime(countryResquest.getMarketCloseTime());
					country.setMarketOpenTime(countryResquest.getMarketOpenTime());
					countryRepository.save(country);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.COUNTRY_UPDATE);
					map.put(Constant.DATA, country);
					log.info(" country updated successfully ! status - {} " + Constant.OK + country);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, country);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.STATUS_INVALID_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.STATUS_INVALID_MESSAGE);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getCountry() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Country> countryList = countryRepository.findAll();
			if (!countryList.isEmpty() && countryList != null) {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
				map.put(Constant.DATA, countryList);
				log.info(" country updated successfully ! status - {} " + Constant.OK + countryList);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, countryList);
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> saveExchange(ExchangeRequest exchangeRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			/*------------------- Add Exchange here ------------------*/
			if (exchangeRequest.getStatus().equals(Constant.ZERO)) {
				Exchange oldExchange = exchangeRepository.findByCountryAndName(exchangeRequest.getCountry(),
						exchangeRequest.getName());
				if (oldExchange != null) {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.COUNTRY_AND_EXCHANGE_ALREADY);
					log.info(Constant.OK + " ! status - {}", Constant.COUNTRY_AND_EXCHANGE_ALREADY);
				} else {
					Exchange exchange = new Exchange();
					exchange.setCountry(exchangeRequest.getCountry());
					exchange.setName(exchangeRequest.getName());
					exchange.setTimezone(exchangeRequest.getTimezone());
					exchange.setStatus(Constant.ONE);
					exchangeRepository.save(exchange);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.EXCHNAGE_ADDED);
					map.put(Constant.DATA, exchange);
					log.info(" Exchange added successfully ! status - {} " + Constant.OK + exchange);
				}
				/*------------------- Update Exchange here ------------------*/
			} else if (exchangeRequest.getStatus().equals(Constant.ONE)) {
				Exchange exchange = exchangeRepository.findById(exchangeRequest.getId()).orElse(null);
				if (exchange != null) {
					exchange.setCountry(exchangeRequest.getCountry());
					exchange.setName(exchangeRequest.getName());
					exchange.setTimezone(exchangeRequest.getTimezone());
					exchangeRepository.save(exchange);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.EXCHANGE_UPDATE);
					map.put(Constant.DATA, exchange);
					log.info(" Exchange updated successfully ! status - {} " + Constant.OK + exchange);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, exchange);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
				/*------------------- disable Exchange here ------------------*/
			} else if (exchangeRequest.getStatus().equals(Constant.TWO)) {
				Exchange exchange = exchangeRepository.findById(exchangeRequest.getId()).orElse(null);
				if (exchange != null) {
					exchange.setStatus(Constant.ZERO);
					exchangeRepository.save(exchange);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.EXCHANGE_DISABLE);
					log.info(" Exchange disable successfully ! status - {} " + Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, exchange);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
				/*------------------- enable Exchange here ------------------*/
			} else if (exchangeRequest.getStatus().equals(Constant.THREE)) {
				Exchange exchange = exchangeRepository.findById(exchangeRequest.getId()).orElse(null);
				if (exchange != null) {
					exchange.setStatus(Constant.ONE);
					exchangeRepository.save(exchange);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.EXCHANGE_ENABLE);
					log.info(" Exchange enable successfully ! status - {} " + Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, exchange);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.STATUS_INVALID_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getStockCount(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		CountResponse countResponse = new CountResponse();
		Long stockCount = 0L, activeCount = 0L, inactiveCount = 0L, KsaStockCount = 0L, UsaStockCount = 0L,
				NewlyListedStockCount = 0L, cryptoCount = 0L;
		try {
			/* get only for stock */
			if (type.equalsIgnoreCase("stock")) {
				/*---------------- get all stock(include all stock, KSA stock, USA stock) count ---------------*/
				stockCount = stockRepository.findAllStockCount();
				log.info(" Stock count :- " + stockCount);
				if (stockCount != 0) {
					countResponse.setTotal(stockCount);
					/*---------------- get all active stocks count --------------*/
					activeCount = stockRepository.findAllActiveStocksCount();
					if (activeCount != 0) {
						countResponse.setActive(activeCount);
					} else {
						countResponse.setActive(0L);
						log.info(" Active stock not found :- " + activeCount);
					}
					/*---------------- get all inactive stock count --------------*/
					inactiveCount = stockRepository.findAllInactiveStocksCount();
					log.info(" Inactive Stock count :- " + stockCount);
					if (inactiveCount != 0) {
						countResponse.setInactive(inactiveCount);
					} else {
						countResponse.setInactive(0L);
						log.info(" Inactive stock not found :- " + inactiveCount);
					}
					/*---------------- get all KSA stock count ----------------*/
					KsaStockCount = stockRepository.findAllKsaStocksCount("Saudi Arabia");
					log.info(" KSA stock count :- " + KsaStockCount);
					if (KsaStockCount != 0) {
						countResponse.setTotalKsaStocks(KsaStockCount);
					} else {
						countResponse.setTotalKsaStocks(0L);
						log.info(" KSA stock not found :- " + KsaStockCount);
					}
					/*---------------- get all USA stock count ----------------*/
					UsaStockCount = stockRepository.findAllUsaStockCount("United States");
					log.info(" USA stock count :- " + UsaStockCount);
					if (UsaStockCount != 0) {
						countResponse.setTotalUsaStocks(UsaStockCount);
					} else {
						countResponse.setTotalUsaStocks(0L);
						log.info(" USA stock not found :- " + UsaStockCount);
					}
					/* set newlylistedStockCount */
					countResponse.setNewlyListed(NewlyListedStockCount);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND);
					map.put(Constant.DATA, countResponse);
					log.info(Constant.DATA_FOUND + "! status - {}", Constant.OK + " " + countResponse);
				} else {
					countResponse.setTotal(stockCount);
					countResponse.setActive(activeCount);
					countResponse.setInactive(inactiveCount);
					countResponse.setTotalKsaStocks(KsaStockCount);
					countResponse.setTotalUsaStocks(UsaStockCount);
					countResponse.setNewlyListed(NewlyListedStockCount);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, countResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK + " " + countResponse);
				}
				/* get only for crypto */
			} else if (type.equalsIgnoreCase("crypto")) {
				/*-------------- get all crypto count -----------------*/
				cryptoCount = cryptoRepository.findAllCryptosCount();
				log.info(" Crypto count :- " + cryptoCount);
				if (cryptoCount != null) {
					countResponse.setTotal(cryptoCount);
					/*-------------- get all active crypto count ----------------*/
					activeCount = cryptoRepository.findAllActiveCryptoCount();
					log.info(" Crypto active count :- " + activeCount);
					if (activeCount != null) {
						countResponse.setActive(activeCount);
					} else {
						countResponse.setActive(0L);
						log.info(" Crypto active not found :- " + activeCount);
					}
					/*-------------- get all inactive crypto count ----------------*/
					inactiveCount = cryptoRepository.findAllInactiveCryptoCount();
					log.info(" Crypto inactive count :- " + inactiveCount);
					if (inactiveCount != null) {
						countResponse.setInactive(inactiveCount);
					} else {
						countResponse.setInactive(0L);
						log.info(" Crypto inactive not found :- " + inactiveCount);
					}
					/*-------------- get all newlyListed crypto count ----------------*/
					LocalDate currentDate = LocalDate.now();
					// Calculate one year ago from the current date
					LocalDate oneYearAgoDate = currentDate.minus(Period.ofYears(1));
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					// Format the one year ago date as a string
					String formattedOneYearAgoDate = oneYearAgoDate.format(formatter);
					NewlyListedStockCount = cryptoRepository.findAllNewlyListedCryptoCount(formattedOneYearAgoDate);
					log.info(" NewlyListed crypto count :- " + NewlyListedStockCount);
					if (NewlyListedStockCount != null) {
						countResponse.setNewlyListed(NewlyListedStockCount);
					} else {
						countResponse.setNewlyListed(0L);
						log.info(" NewlyListed crypto not found :- " + NewlyListedStockCount);
					}
					/*----------------- set KSA & USA count value --------------*/
					countResponse.setTotalKsaStocks(KsaStockCount);
					countResponse.setTotalUsaStocks(UsaStockCount);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND);
					map.put(Constant.DATA, countResponse);
					log.info(Constant.DATA_FOUND + "! status - {}", Constant.OK + " " + countResponse);
				} else {
					countResponse.setTotal(stockCount);
					countResponse.setActive(activeCount);
					countResponse.setInactive(inactiveCount);
					countResponse.setTotalKsaStocks(KsaStockCount);
					countResponse.setTotalUsaStocks(UsaStockCount);
					countResponse.setNewlyListed(NewlyListedStockCount);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, countResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK + " " + countResponse);
				}

			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.INVALID_TYPE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getStockSyncCount(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		SyncCountResponse syncCountResponse = new SyncCountResponse();
		Long stockCount = 0L, cryptoCrypto = 0L, syncStocksCount = 0L, syncCryptoCount = 0L,
				remainingSyncStockCount = 0L, remainingSyncCryptoCount = 0L;
		try {
			if (type.equalsIgnoreCase("stock")) {
				/*---------------- get all stock(include all stock, KSA stock, USA stock) count ---------------*/
				stockCount = stockRepository.findAllStockCount();
				log.info(" Stock count :- " + stockCount);
				if (stockCount != 0) {
					syncCountResponse.setTotalCount(stockCount);
					/*-------------------- get all synced stock count ------------------------*/
					Date currentDate = new Date();
					SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd 02:01:10");
					String strDateTime = formatter1.format(currentDate);
					System.out.println(" SyncedDateTime :- " + strDateTime);
					syncStocksCount = stockRepository.findAllSyncedStockCount(strDateTime);
					log.info(" Synced stocks count :- " + syncStocksCount);
					if (syncStocksCount != 0) {
						syncCountResponse.setTotalSynced(syncStocksCount);
					} else {
						syncCountResponse.setTotalSynced(0L);
						log.info(" Synced stocks not found :- " + syncStocksCount);
					}
					/*------------------- get all remaining sync stocks count -------------------------*/
					remainingSyncStockCount = stockCount - syncStocksCount;
					log.info(" Remainig sync stocks count :- " + remainingSyncStockCount);
					if (remainingSyncStockCount != 0) {
						syncCountResponse.setRemainsForSync(remainingSyncStockCount);
					} else {
						syncCountResponse.setRemainsForSync(0L);
						log.info(" Remaining sync stocks not found " + remainingSyncStockCount);
					}
					/*-------------------- set lastUpdate date -----------------------*/
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND);
					map.put(Constant.DATA, syncCountResponse);
					log.info(Constant.DATA_FOUND + "! status - {}", Constant.OK + " " + syncCountResponse);

				} else {
					syncCountResponse.setTotalCount(0L);
					syncCountResponse.setTotalSynced(0L);
					syncCountResponse.setRemainsForSync(0L);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, syncCountResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK + " " + syncCountResponse);
				}
			} else if (type.equalsIgnoreCase("crypto")) {
				/*----------------- get all crypto count ------------------*/
				cryptoCrypto = cryptoRepository.findAllCryptosCount();
				log.info(" crypto count :- " + cryptoCrypto);
				if (cryptoCrypto != 0) {
					syncCountResponse.setTotalCount(cryptoCrypto);
					/*----------------- get all synced crypto count ------------------*/
					Date currentDate = new Date();
					SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd 02:30:00");
					String strDateTime = formatter1.format(currentDate);
					System.out.println(" SyncedDateTime :- " + strDateTime);
					syncCryptoCount = cryptoRepository.findAllSyncedCryptoCount(strDateTime);
					log.info(" Synced crypto count :- " + syncCryptoCount);
					if (syncCryptoCount != 0) {
						syncCountResponse.setTotalSynced(syncCryptoCount);
					} else {
						syncCountResponse.setTotalSynced(0L);
						log.info(" Synced crypto count not found :- " + syncCryptoCount);
					}
					/*----------------- get all remaining sync crypto count ------------------*/
					remainingSyncCryptoCount = cryptoCrypto - syncCryptoCount;
					log.info(" Remaining crypto :- " + remainingSyncCryptoCount);
					if (remainingSyncCryptoCount != 0) {
						syncCountResponse.setRemainsForSync(remainingSyncCryptoCount);
					} else {
						syncCountResponse.setRemainsForSync(remainingSyncCryptoCount);
						log.info(" Remaining sync crypto not found :- " + syncCryptoCount);
					}
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND);
					map.put(Constant.DATA, syncCountResponse);
					log.info(Constant.DATA_FOUND + "! status - {}", Constant.OK + " " + syncCountResponse);
				} else {
					syncCountResponse.setTotalCount(0L);
					syncCountResponse.setTotalSynced(0L);
					syncCountResponse.setRemainsForSync(0L);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, syncCountResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK + " " + syncCountResponse);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.INVALID_TYPE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getShortingKey() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		try {
			List<ShortingKey> shortingKeyList = shortingKeyRepository.findAllShortingKey();
			if (!shortingKeyList.isEmpty()) {
				for (ShortingKey shortingKey : shortingKeyList) {
					list.add(shortingKey.getShortBy());
				}
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND);
				map.put(Constant.DATA, list);
				log.info(Constant.DATA_FOUND + "! status - {}", Constant.OK + " " + list);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, shortingKeyList);
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK + " " + shortingKeyList);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getExplore(NewsRequestPayload requestPayload) {
		Map<String, Object> map = new HashMap<String, Object>();
		Pageable pageable = null;
		AllExploreResponse allExploreResponsePage = new AllExploreResponse();
		DividendResponsePage dividendResponsePage = new DividendResponsePage();
		EarningResponsePage earningResponsePage = new EarningResponsePage();
		EconomicResponsePage economicResponsePage = new EconomicResponsePage();
		IposResponsePage iposResponsePage = new IposResponsePage();
		List<Dividend> dividendList = new ArrayList<Dividend>();
		List<Earning> earningList = new ArrayList<Earning>();
		List<Economic> economicList = new ArrayList<Economic>();
		List<Ipos> iposList = new ArrayList<Ipos>();
		List<DividendResponse> list = new ArrayList<DividendResponse>();
		List<EarningResponse> list1 = new ArrayList<EarningResponse>();
		List<EconomicResponse> list2 = new ArrayList<EconomicResponse>();
		List<IposResponse> list3 = new ArrayList<IposResponse>();
		try {
			if (requestPayload.getPageSize() > 0) {
				/* ----------------- Get dividend data ------------------ */
				if (requestPayload.getCountry().equals("united states")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Dividend> dividendPage = dividendRepository.findAllDividend("US", Constant.ONE, pageable);
					if (dividendPage != null) {
						dividendList = dividendPage.getContent();
						for (Dividend dividend : dividendList) {
							DividendResponse dividendResponse = new DividendResponse();
							BeanUtils.copyProperties(dividend, dividendResponse);
							list.add(dividendResponse);
						}
						dividendResponsePage.setIsFirstPage(dividendPage.isFirst());
						dividendResponsePage.setIsLastPage(dividendPage.isLast());
						dividendResponsePage.setPageIndex(dividendPage.getNumber());
						dividendResponsePage.setPageSize(dividendPage.getSize());
						dividendResponsePage.setTotalElement(dividendPage.getTotalElements());
						dividendResponsePage.setTotalPages(dividendPage.getTotalPages());
						dividendResponsePage.setDividendResponseList(list);
					} else {
						dividendResponsePage = null;
					}
				} else if (requestPayload.getCountry().equals("saudi arabia")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Dividend> dividendPage = dividendRepository.findAllDividend("SA", Constant.ONE, pageable);
					if (dividendPage != null) {
						dividendList = dividendPage.getContent();
						for (Dividend dividend : dividendList) {
							DividendResponse dividendResponse = new DividendResponse();
							BeanUtils.copyProperties(dividend, dividendResponse);
							list.add(dividendResponse);
						}
						dividendResponsePage.setIsFirstPage(dividendPage.isFirst());
						dividendResponsePage.setIsLastPage(dividendPage.isLast());
						dividendResponsePage.setPageIndex(dividendPage.getNumber());
						dividendResponsePage.setPageSize(dividendPage.getSize());
						dividendResponsePage.setTotalElement(dividendPage.getTotalElements());
						dividendResponsePage.setTotalPages(dividendPage.getTotalPages());
						dividendResponsePage.setDividendResponseList(list);
					} else {
						dividendResponsePage = null;
					}
				}

				/* ----------------- Get earning data ------------------ */
				if (requestPayload.getCountry().equals("united states")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Earning> earningPage = earningRepository.findAllEarning("US", Constant.ONE, pageable);
					if (earningPage != null) {
						earningList = earningPage.getContent();
						for (Earning earning : earningList) {
							EarningResponse earningResponse = new EarningResponse();
							BeanUtils.copyProperties(earning, earningResponse);
							list1.add(earningResponse);
						}
						earningResponsePage.setIsFirstPage(earningPage.isFirst());
						earningResponsePage.setIsLastPage(earningPage.isLast());
						earningResponsePage.setPageIndex(earningPage.getNumber());
						earningResponsePage.setPageSize(earningPage.getSize());
						earningResponsePage.setTotalElement(earningPage.getTotalElements());
						earningResponsePage.setTotalPages(earningPage.getTotalPages());
						earningResponsePage.setEarningResponseList(list1);
					} else {
						earningResponsePage = null;
					}
				} else if (requestPayload.getCountry().equals("saudi arabia")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Earning> earningPage = earningRepository.findAllEarning("SA", Constant.ONE, pageable);
					if (earningPage != null) {
						earningList = earningPage.getContent();
						for (Earning earning : earningList) {
							EarningResponse earningResponse = new EarningResponse();
							BeanUtils.copyProperties(earning, earningResponse);
							list1.add(earningResponse);
						}
						earningResponsePage.setIsFirstPage(earningPage.isFirst());
						earningResponsePage.setIsLastPage(earningPage.isLast());
						earningResponsePage.setPageIndex(earningPage.getNumber());
						earningResponsePage.setPageSize(earningPage.getSize());
						earningResponsePage.setTotalElement(earningPage.getTotalElements());
						earningResponsePage.setTotalPages(earningPage.getTotalPages());
						earningResponsePage.setEarningResponseList(list1);
					} else {
						earningResponsePage = null;
					}
				}

				/* ----------------- Get economic data ------------------ */
				if (requestPayload.getCountry().equals("united states")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Economic> economicPage = economicRepository.findAllEconomic("US", Constant.ONE, pageable);
					if (economicPage != null) {
						economicList = economicPage.getContent();
						for (Economic economic : economicList) {
							EconomicResponse economicResponse = new EconomicResponse();
							BeanUtils.copyProperties(economic, economicResponse);
							list2.add(economicResponse);
						}
						economicResponsePage.setIsFirstPage(economicPage.isFirst());
						economicResponsePage.setIsLastPage(economicPage.isLast());
						economicResponsePage.setPageIndex(economicPage.getNumber());
						economicResponsePage.setPageSize(economicPage.getSize());
						economicResponsePage.setTotalElement(economicPage.getTotalElements());
						economicResponsePage.setTotalPages(economicPage.getTotalPages());
						economicResponsePage.setEconomicResponseList(list2);
					} else {
						economicResponsePage = null;
					}
				} else if (requestPayload.getCountry().equals("saudi arabia")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Economic> economicPage = economicRepository.findAllEconomic("SA", Constant.ONE, pageable);
					if (economicPage != null) {
						economicList = economicPage.getContent();
						for (Economic economic : economicList) {
							EconomicResponse economicResponse = new EconomicResponse();
							BeanUtils.copyProperties(economic, economicResponse);
							list2.add(economicResponse);
						}
						economicResponsePage.setIsFirstPage(economicPage.isFirst());
						economicResponsePage.setIsLastPage(economicPage.isLast());
						economicResponsePage.setPageIndex(economicPage.getNumber());
						economicResponsePage.setPageSize(economicPage.getSize());
						economicResponsePage.setTotalElement(economicPage.getTotalElements());
						economicResponsePage.setTotalPages(economicPage.getTotalPages());
						economicResponsePage.setEconomicResponseList(list2);
					} else {
						economicResponsePage = null;
					}
				}
				/* ----------------- Get ipos data ------------------ */
				if (requestPayload.getCountry().equals("united states")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Ipos> iposPage = iposRepository.findAllIpos("US", Constant.ONE, pageable);
					if (iposPage != null) {
						iposList = iposPage.getContent();
						for (Ipos ipos : iposList) {
							IposResponse iposResponse = new IposResponse();
							BeanUtils.copyProperties(ipos, iposResponse);
							list3.add(iposResponse);
						}
						iposResponsePage.setIsFirstPage(iposPage.isFirst());
						iposResponsePage.setIsLastPage(iposPage.isLast());
						iposResponsePage.setPageIndex(iposPage.getNumber());
						iposResponsePage.setPageSize(iposPage.getSize());
						iposResponsePage.setTotalElement(iposPage.getTotalElements());
						iposResponsePage.setTotalPages(iposPage.getTotalPages());
						iposResponsePage.setIposResponseList(list3);
					} else {
						iposResponsePage = null;
					}
				} else if (requestPayload.getCountry().equals("saudi arabia")) {
					pageable = PageRequest.of(requestPayload.getPageIndex(), requestPayload.getPageSize());
					Page<Ipos> iposPage = iposRepository.findAllIpos("SA", Constant.ONE, pageable);
					if (iposPage != null) {
						iposList = iposPage.getContent();
						for (Ipos ipos : iposList) {
							IposResponse iposResponse = new IposResponse();
							BeanUtils.copyProperties(ipos, iposResponse);
							list3.add(iposResponse);
						}
						iposResponsePage.setIsFirstPage(iposPage.isFirst());
						iposResponsePage.setIsLastPage(iposPage.isLast());
						iposResponsePage.setPageIndex(iposPage.getNumber());
						iposResponsePage.setPageSize(iposPage.getSize());
						iposResponsePage.setTotalElement(iposPage.getTotalElements());
						iposResponsePage.setTotalPages(iposPage.getTotalPages());
						iposResponsePage.setIposResponseList(list3);
					} else {
						iposResponsePage = null;
					}
				}
				// set response
				allExploreResponsePage.setDividendResponsePage(dividendResponsePage);
				allExploreResponsePage.setEarningResponsePage(earningResponsePage);
				allExploreResponsePage.setEconomicResponsePage(economicResponsePage);
				allExploreResponsePage.setIposResponsePage(iposResponsePage);
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND);
				map.put(Constant.DATA, allExploreResponsePage);
				log.info(Constant.DATA_FOUND + " !! status - {}", Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.PAGE_LIMIT_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " ! status - {}", Constant.BAD_REQUEST);
			}

		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	Boolean isExist(HolidayResquest holidayResquest) {
		Boolean flag = false;
		Holiday holiday = holidayRepository.findByHolidayNameAndCountryIdFk(holidayResquest.getHolidayName(),
				holidayResquest.getCountryId());
		if (holiday != null) {
			flag = true;
		}
		return flag;
	}

	public Map<String, Object> holiday(HolidayResquest holidayResquest) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (holidayResquest.getCountryId() > 0) {
				Holiday holiday = null;
				HolidayResponse holidayResponse = new HolidayResponse();
				Optional<Country> optionalCountry = Optional.empty();
				optionalCountry = countryRepository.findById(holidayResquest.getCountryId());
				if (!optionalCountry.isEmpty()) {
					Country country = optionalCountry.get();
					if (holidayResquest.getId() == 0) {
						if (!isExist(holidayResquest)) {
							holiday = new Holiday();
							BeanUtils.copyProperties(holidayResquest, holiday);
							holiday.setDate(DateUtil.StringToDate(holidayResquest.getDate()));
							holiday.setYear(Integer.parseInt(Year.now().toString()));
							holiday.setCountry(country.getCountry());
							holiday.setCountryIdFk(country.getId());
							holiday.setStatus(Constant.ONE);
							holiday.setCreationDate(new Date());
							holiday = holidayRepository.save(holiday);

							BeanUtils.copyProperties(holiday, holidayResponse);
							holidayResponse.setCountryId(holiday.getCountryIdFk());
							holidayResponse.setDate(DateUtil.convertDateToStringDate(holiday.getDate()));
							holidayResponse
									.setCreationDate(DateUtil.convertDateToStringDateTime(holiday.getCreationDate()));
							holidayResponse
									.setUpdationDate(DateUtil.convertDateToStringDateTime(holiday.getUpdationDate()));

							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.HOLIDAY_SAVED_MESSAGE);
							map.put(Constant.DATA, holidayResponse);
							log.info(Constant.HOLIDAY_SAVED_MESSAGE + " status - {} " + Constant.OK);
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.HOLIDAY_ALREADY_EXIST_MESSAGE);
							log.info(Constant.HOLIDAY_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
							return map;
						}
					} else {
						holiday = holidayRepository.findByIdAndStatus(holidayResquest.getId(), Constant.ONE);
						if (holiday != null) {
							if (!isExist(holidayResquest)) {
								BeanUtils.copyProperties(holidayResquest, holiday);
								holiday.setDate(DateUtil.StringToDate(holidayResquest.getDate()));
								holiday.setUpdationDate(new Date());
								holiday = holidayRepository.save(holiday);

								BeanUtils.copyProperties(holiday, holidayResponse);
								holidayResponse.setCountryId(holiday.getCountryIdFk());
								holidayResponse.setDate(DateUtil.convertDateToStringDate(holiday.getDate()));
								holidayResponse.setCreationDate(
										DateUtil.convertDateToStringDateTime(holiday.getCreationDate()));
								holidayResponse.setUpdationDate(
										DateUtil.convertDateToStringDateTime(holiday.getUpdationDate()));

								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.HOLIDAY_UPDATED_MESSAGE);
								map.put(Constant.DATA, holidayResponse);
								log.info(Constant.HOLIDAY_UPDATED_MESSAGE + " status - {} " + Constant.OK);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.HOLIDAY_ALREADY_EXIST_MESSAGE);
								log.info(Constant.HOLIDAY_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
							}
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.OK);
							map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
							map.put(Constant.DATA, holidayResponse);
							log.info(Constant.ID_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
						}
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
					log.info(Constant.ID_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.COUNTRY_ID_CANT_BE_ZERO_MESSAGE);
				log.info(Constant.COUNTRY_ID_CANT_BE_ZERO_MESSAGE + " status - {} " + Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getHoliday(RequestPage requestPage) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<HolidayResponse> holidayResponseList = new ArrayList<>();
			HolidayResponsePage holidayResponsePage = new HolidayResponsePage();
			Page<Holiday> page = null;
			Pageable pageable = PageRequest.of(requestPage.getPageIndex(), requestPage.getPageSize(),
					Sort.by("id").descending());
			if (!requestPage.getKeyWord().isBlank() && requestPage.getCountry().isBlank()) {
				page = holidayRepository.searchHoliday(pageable, requestPage.getKeyWord());
			} else if (requestPage.getKeyWord().isBlank() && !requestPage.getCountry().isBlank()) {
				page = holidayRepository.findByCountry(pageable, requestPage.getCountry());
			} else if (requestPage.getKeyWord().isBlank() && requestPage.getCountry().isBlank()) {
				page = holidayRepository.findAll(pageable);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " status - {} " + Constant.BAD_REQUEST);
				return map;
			}

			if (page != null && !page.isEmpty()) {
				List<Holiday> holidayList = page.getContent();
				for (Holiday holiday : holidayList) {
					HolidayResponse holidayResponse = new HolidayResponse();
					BeanUtils.copyProperties(holiday, holidayResponse);
					holidayResponse.setCountryId(holiday.getCountryIdFk());
					holidayResponse.setCreationDate(DateUtil.convertDateToStringDateTime(holiday.getCreationDate()));
					holidayResponse.setUpdationDate(DateUtil.convertDateToStringDateTime(holiday.getUpdationDate()));
					holidayResponse.setDate(DateUtil.convertDateToStringDateTime(holiday.getDate()));
					holidayResponseList.add(holidayResponse);
				}
				holidayResponsePage.setHolidayResponseList(holidayResponseList);
				holidayResponsePage.setPageIndex(page.getNumber());
				holidayResponsePage.setPageSize(page.getSize());
				holidayResponsePage.setTotalElement(page.getTotalElements());
				holidayResponsePage.setTotalPages(page.getTotalPages());
				holidayResponsePage.setIsLastPage(page.isLast());
				holidayResponsePage.setIsFirstPage(page.isFirst());

				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				map.put(Constant.DATA, holidayResponsePage);
				log.info(Constant.RECORD_FOUND_MESSAGE + " status - {} " + Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, holidayResponsePage);
				log.info(Constant.RECORD_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public List<Exchange> getAllExchangeDetails(String country) {
		List<Exchange> exchangelist = new ArrayList<>();
		if (country != "" && country != null) {
			exchangelist = exchangeRepository.findAllExchangeUsingSearch(country);
		} else {
			exchangelist = exchangeRepository.findAllExchanges();
		}
		return exchangelist;
	}

	@Override
	public List<Indices> getAllIndicesDetails(RequestPage requestPage) {
		List<Indices> indiceslist = new ArrayList<>();
		if (requestPage.getKeyWord() != "" && requestPage.getKeyWord() != null) {
			indiceslist = indicesRepository.findAllIndicesUsingSearch(requestPage.getKeyWord());
		} else if (!requestPage.getKeyWord().isBlank() && !requestPage.getCountry().isBlank()) {
			indiceslist = indicesRepository.findAllIndicesWithCountryAndSearching(requestPage.getCountry(),
					requestPage.getKeyWord());
		} else if (!requestPage.getCountry().isBlank()) {
			indiceslist = indicesRepository.findAllIndicesWithCountry(requestPage.getCountry());
		} else {
			indiceslist = indicesRepository.findAllIndices();
		}
		return indiceslist;
	}

	@Override
	public Map<String, Object> getSecFilings(SecFilingRequestPage secFilingRequestPage) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<SecFilingRes> secFilingResList = new ArrayList<>();
			SecFilingResponsePage secFilingResponsePage = new SecFilingResponsePage();
			Page<SecFiling> page = null;
			Pageable pageable = PageRequest.of(secFilingRequestPage.getPageIndex(), secFilingRequestPage.getPageSize());
			if (!secFilingRequestPage.getKeyWord().isBlank() && !secFilingRequestPage.getCountry().isBlank()
					&& secFilingRequestPage.getType().isBlank()) {
				page = secFilingRepository.searchSecFiling(pageable, secFilingRequestPage.getKeyWord(),
						secFilingRequestPage.getCountry());
			} else if (secFilingRequestPage.getKeyWord().isBlank() && !secFilingRequestPage.getCountry().isBlank()
					&& !secFilingRequestPage.getType().isBlank()) {
				page = secFilingRepository.findByCountryAndFilingType(pageable, secFilingRequestPage.getCountry(),
						secFilingRequestPage.getType());
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " status - {} " + Constant.BAD_REQUEST);
				return map;
			}

			if (page != null && !page.isEmpty()) {
				List<SecFiling> secFilingList = page.getContent();
				Map<Integer, List<SecFiling>> mapList = secFilingList.stream()
						.collect(Collectors.groupingBy(SecFiling::getYear));
				log.info(mapList + " status - {} " + Constant.OK);
				for (Map.Entry<Integer, List<SecFiling>> entry : mapList.entrySet()) {
					List<SecFilingResponse> secFilingResponsesList = new ArrayList<>();
					for (SecFiling secFiling : entry.getValue()) {
						SecFilingResponse secFilingResponse = new SecFilingResponse();
						BeanUtils.copyProperties(secFiling, secFilingResponse);
						secFilingResponse.setType(secFiling.getFilingType());
						secFilingResponse.setFillingDate(secFiling.getFillingDate());
						secFilingResponse.setAcceptedDate(secFiling.getAcceptedDate());
						secFilingResponsesList.add(secFilingResponse);
					}
					Collections.reverse(secFilingResponsesList);

					SecFilingRes secFilingRes = new SecFilingRes();
					secFilingRes.setFilingResponsesList(secFilingResponsesList);
					secFilingRes.setTotalReport(secFilingResponsesList.size());
					secFilingRes.setYear(entry.getKey());
					secFilingResList.add(secFilingRes);
				}
				secFilingResList.sort(Comparator.comparingInt(SecFilingRes::getYear).reversed());

				secFilingResponsePage.setSecFilingResList(secFilingResList);
				secFilingResponsePage.setPageIndex(page.getNumber());
				secFilingResponsePage.setPageSize(page.getSize());
				secFilingResponsePage.setTotalElement(page.getTotalElements());
				secFilingResponsePage.setTotalPages(page.getTotalPages());
				secFilingResponsePage.setIsLastPage(page.isLast());
				secFilingResponsePage.setIsFirstPage(page.isFirst());

				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				map.put(Constant.DATA, secFilingResponsePage);
				log.info(Constant.RECORD_FOUND_MESSAGE + " status - {} " + Constant.OK);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, secFilingResponsePage);
				log.info(Constant.RECORD_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public List<Stock> getAllStocksDetails(StockRequestPage stockRequest) {
		List<Stock> stocklist = new ArrayList<>();
		try {
			if (!stockRequest.getKeyWord().isBlank() && stockRequest.getCountry().isBlank()
					&& stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& stockRequest.getFilterBySector().isBlank()
					&& stockRequest.getFilterByShariahCompliance().isBlank()) {
				stocklist = stockRepository.findAllStocksWithSearching(stockRequest.getKeyWord());
			}
			if (!stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
					&& stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& stockRequest.getFilterBySector().isBlank()
					&& stockRequest.getFilterByShariahCompliance().isBlank()) {
				stocklist = stockRepository.findAllStockByCountry(stockRequest.getKeyWord(), stockRequest.getCountry());
			} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
					&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& stockRequest.getFilterBySector().isBlank()
					&& stockRequest.getFilterByShariahCompliance().isBlank()) {
				stocklist = stockRepository.getStockListBySorting(stockRequest.getCountry(),
						stockRequest.getExchange());
			} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
					&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& !stockRequest.getFilterBySector().isBlank()
					&& stockRequest.getFilterByShariahCompliance().isBlank()) {
				stocklist = stockRepository.filterBySector(stockRequest.getCountry(), stockRequest.getFilterBySector());
			} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
					&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& !stockRequest.getFilterBySector().isBlank()
					&& !stockRequest.getFilterByShariahCompliance().isBlank()) {
				if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("1")) {
					stocklist = stockRepository.getStockListByCompliance(stockRequest.getCountry(),
							stockRequest.getFilterBySector());
				} else if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("0")) {
					stocklist = stockRepository.getStockListByNonCompliance(stockRequest.getCountry(),
							stockRequest.getFilterBySector());
				} else {
					stocklist = null;
				}
			} else if (stockRequest.getKeyWord().isBlank() && !stockRequest.getCountry().isBlank()
					&& !stockRequest.getExchange().isBlank() && !stockRequest.getSortBy().isBlank()
					&& stockRequest.getFilterBySector().isBlank()
					&& !stockRequest.getFilterByShariahCompliance().isBlank()) {
				if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("1")) {
					stocklist = stockRepository.getStockListByCompliance(stockRequest.getCountry());
				} else if (stockRequest.getFilterByShariahCompliance().equalsIgnoreCase("0")) {
					stocklist = stockRepository.getStockListByNonCompliance(stockRequest.getCountry());
				} else {
					stocklist = null;
				}
			}

			if (stockRequest.getDirection() != null && !stockRequest.getDirection().isBlank()) {
				if (stockRequest.getSortBy().matches("price")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getPrice).reversed()).toList();
				} else if (stockRequest.getSortBy().matches("market_cap")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getMarketCap).reversed())
							.toList();
				} else if (stockRequest.getSortBy().matches("volume")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getVolume).reversed()).toList();
				} else if (stockRequest.getSortBy().matches("symbol")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getSymbol).reversed()).toList();
				} else if (stockRequest.getSortBy().matches("percent_change")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getPercent_change).reversed())
							.toList();
				}
			} else {
				if (stockRequest.getSortBy().matches("price")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getPrice)).toList();
				} else if (stockRequest.getSortBy().matches("market_cap")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getMarketCap)).toList();
				} else if (stockRequest.getSortBy().matches("volume")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getVolume)).toList();
				} else if (stockRequest.getSortBy().matches("symbol")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getSymbol)).toList();
				} else if (stockRequest.getSortBy().matches("percent_change")) {
					stocklist = stocklist.stream().sorted(Comparator.comparing(Stock::getPercent_change)).toList();
				}
			}
			log.info("Data found! status - {}", Constant.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stocklist;
	}

	@Override
	public ShariaCompliance getAllShariyaComplianceDetails(@Valid ShariyaComplianceRequest shariyaComplianceRequest) {
		ShariaCompliance shariaCompliance = new ShariaCompliance();
		try {
			shariaCompliance = sariyaComplianceRepository.findAllShariaComplianceDetails(
					shariyaComplianceRequest.getCountry(), shariyaComplianceRequest.getTickerSymbol());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shariaCompliance;
	}

	@Override
	public Map<String, Object> getCsvFileExcelDetails(String country) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SaudiShariahCompliance> saudiShariahComplianceList = new ArrayList<SaudiShariahCompliance>();
		List<CsvSaudiExportFileRespnse> csvSaudiExportFileRespnseList = new ArrayList<CsvSaudiExportFileRespnse>();
		List<CsvUsaExportFileRespnse> csvUsExportFileRespnseList = new ArrayList<CsvUsaExportFileRespnse>();
		List<csvAlRajhiComplianceExportFileResponse> csvAlRajhiComplianceExportFileRespnseList = new ArrayList<csvAlRajhiComplianceExportFileResponse>();
		CsvExportFileResponse csvExportFileResponse = new CsvExportFileResponse();
		List<ShariaCompliance> shariaComplianceList = new ArrayList<ShariaCompliance>();
		try {
			if (country.equalsIgnoreCase(Constant.SAUDI_AAOIFI_COMPLIANCE)
					|| country.equalsIgnoreCase(Constant.USA_AAOIFI_COMPLIANCE)) {
				if (country.equalsIgnoreCase(Constant.SAUDI_AAOIFI_COMPLIANCE)) {
					shariaComplianceList = sariyaComplianceRepository.findCsvFile("saudi arabia");
				} else if (country.equalsIgnoreCase(Constant.USA_AAOIFI_COMPLIANCE)) {
					shariaComplianceList = sariyaComplianceRepository.findCsvFile("united states");
				}
				if (shariaComplianceList != null && !shariaComplianceList.isEmpty()) {
					for (ShariaCompliance compliance : shariaComplianceList) {
						CsvSaudiExportFileRespnse csvSaudiExportFileRespnse = new CsvSaudiExportFileRespnse();
						CsvUsaExportFileRespnse csvUsaExportFileRespnse = new CsvUsaExportFileRespnse();
						if (compliance.getCountry().equalsIgnoreCase("saudi arabia")) {
							BeanUtils.copyProperties(compliance, csvSaudiExportFileRespnse);
							csvSaudiExportFileRespnseList.add(csvSaudiExportFileRespnse);
						} else if (compliance.getCountry().equalsIgnoreCase("united states")) {
							BeanUtils.copyProperties(compliance, csvUsaExportFileRespnse);
							csvUsExportFileRespnseList.add(csvUsaExportFileRespnse);
						}
					}
					csvExportFileResponse.setCsvSaudiExportFileRespnse(csvSaudiExportFileRespnseList);
					csvExportFileResponse.setCsvUsaExportFileRespnse(csvUsExportFileRespnseList);
					csvExportFileResponse.setCsvAlRajhiComplianceList(null);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, csvExportFileResponse);
					log.info(Constant.DATA_FOUND_MESSAGE + " " + Constant.OK);
				} else {
					csvExportFileResponse.setCsvSaudiExportFileRespnse(null);
					csvExportFileResponse.setCsvUsaExportFileRespnse(null);
					csvExportFileResponse.setCsvAlRajhiComplianceList(null);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, csvExportFileResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " " + Constant.OK);
				}
			} else if (country.equalsIgnoreCase(Constant.AL_RAJHI_COMPLIANCE)) {
				saudiShariahComplianceList = saudiShariahComplianceRepository.findAll();
				if (saudiShariahComplianceList != null && !saudiShariahComplianceList.isEmpty()) {
					for (SaudiShariahCompliance saudiShariahCompliance : saudiShariahComplianceList) {
						csvAlRajhiComplianceExportFileResponse alRajhiComplianceExportFileResponse = new csvAlRajhiComplianceExportFileResponse();
						BeanUtils.copyProperties(saudiShariahCompliance, alRajhiComplianceExportFileResponse);
						csvAlRajhiComplianceExportFileRespnseList.add(alRajhiComplianceExportFileResponse);
					}
					csvExportFileResponse.setCsvSaudiExportFileRespnse(null);
					csvExportFileResponse.setCsvUsaExportFileRespnse(null);
					csvExportFileResponse.setCsvAlRajhiComplianceList(csvAlRajhiComplianceExportFileRespnseList);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					map.put(Constant.DATA, csvExportFileResponse);
					log.info(Constant.DATA_FOUND_MESSAGE + " " + Constant.OK);
				} else {
					csvExportFileResponse.setCsvSaudiExportFileRespnse(null);
					csvExportFileResponse.setCsvUsaExportFileRespnse(null);
					csvExportFileResponse.setCsvAlRajhiComplianceList(null);
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, csvExportFileResponse);
					log.info(Constant.DATA_NOT_FOUND_MESSAGE + " " + Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
				log.info(Constant.BAD_REQUEST_MESSAGE + " " + Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
		}
		return map;
	}

	@Override
	public Map<String, Object> getFile() {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<CsvFileType> fileList = csvFileRepository.findAll();
			if (!fileList.isEmpty() && fileList != null) {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
				map.put(Constant.DATA, fileList);
				log.info(" country updated successfully ! status - {} " + Constant.OK + fileList);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				map.put(Constant.DATA, fileList);
				log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> getIndicesSeriesData(TimeSeriesRequest timeSeriesRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			String previous_close = null;
			int count = 0;
			IndicesTimeSeriesResponse indicesTimeSeriesResponse = new IndicesTimeSeriesResponse();
			List<IndicesYearGraph> indicesYearGraphList = new ArrayList<IndicesYearGraph>();
			List<IndicesDayGraph> indicesDayGraphList = new ArrayList<IndicesDayGraph>();
			List<IndicesMinuteGraph> indicesMinutesGraphList = new ArrayList<IndicesMinuteGraph>();
			List<TimeSeriesDetails> indicesGraphList2 = new ArrayList<TimeSeriesDetails>();
			List<IndicesMinuteGraph> indicesOneDayData = new ArrayList<IndicesMinuteGraph>();
			List<IndicesMinuteGraph> indicesOneWeekData = new ArrayList<IndicesMinuteGraph>();
			List<IndicesDayGraph> indicesFiveYearData = new ArrayList<IndicesDayGraph>();
			List<IndicesMinuteGraph> indicesOneMonthMinuteData = new ArrayList<IndicesMinuteGraph>();

			LocalDate currentDate = LocalDate.now();
			Country country = countryRepository.findByCountry(timeSeriesRequest.getCountry());
			if (country != null) {
				Indices indices = indicesRepository.findBySymbol(timeSeriesRequest.getSymbol());
				if (indices != null) {
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_DAY)) {
						indicesOneDayData = indicesMinuteGraphRepository
								.findAllMinutesData(timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry());
						if (indicesOneDayData != null && !indicesOneDayData.isEmpty()) {
							String oneDayOldDate = DateUtil.getPreviousDateTimes(indicesOneDayData.get(0).getDate(),
									Constant.ONE_DAY);
							for (IndicesMinuteGraph minuteGraph : indicesOneDayData) {
								String[] date = minuteGraph.getDate().split(" ");
								if (!oneDayOldDate.equals(date[0])) {
									indicesMinutesGraphList.add(minuteGraph);
								} else {
									previous_close = minuteGraph.getClose();
									break;
								}
							}
						}
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_WEEK)) {
						LocalDate previousDate = currentDate.minusDays(15);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedPreviousDate = previousDate.format(formatter);
						System.out.println("Previous date: " + formattedPreviousDate);
						indicesOneWeekData = indicesMinuteGraphRepository.findAllFifteenDaysMinutesData(
								timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry(), formattedPreviousDate);
						if (indicesOneWeekData != null && !indicesOneWeekData.isEmpty()) {
							String oneWeekOldDate = DateUtil.getPreviousDateTimes(indicesOneWeekData.get(0).getDate(),
									Constant.ONE_WEEK);
							LocalDateTime latestDate = DateUtil.stringToLocalDateTime(oneWeekOldDate + " 00:00:00");
							LocalDateTime fifteenMinuteOldData = DateUtil
									.stringToLocalDateTime(indicesOneWeekData.get(0).getDate());
							for (IndicesMinuteGraph indicesMinuteGraph : indicesOneWeekData) {
								String[] date = DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate()).toString()
										.split("T");
								String[] oldDate = fifteenMinuteOldData.toString().split("T");
								if (!date[0].contains(oldDate[0])) {
									fifteenMinuteOldData = DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate());
								}
								if (latestDate.equals(DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate()))
										|| DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
												.isAfter(latestDate)) {
									if (DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
											.equals(fifteenMinuteOldData)) {
										indicesMinutesGraphList.add(indicesMinuteGraph);
										fifteenMinuteOldData = fifteenMinuteOldData.minusMinutes(15);
									}
								} else {
									if (DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
											.equals(fifteenMinuteOldData)) {
										log.info("1 week 15 mint data :- " + indicesMinuteGraph.getDate());
										previous_close = indicesMinuteGraph.getClose();
										break;
									}
								}
							}
						}
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_MONTH)) {
						LocalDate previousDate = currentDate.minusDays(31);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedPreviousDate = previousDate.format(formatter);
						System.out.println("Previous date: " + formattedPreviousDate);
						indicesOneMonthMinuteData = indicesMinuteGraphRepository.findAllOneMonthMinuteData(
								timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry(), formattedPreviousDate);
						if (indicesOneMonthMinuteData != null && !indicesOneMonthMinuteData.isEmpty()) {
							String oneMonthOldDate = DateUtil.getPreviousDateTimes(
									indicesOneMonthMinuteData.get(0).getDate(), Constant.ONE_MONTH);
							LocalDateTime latestDate = DateUtil.stringToLocalDateTime(oneMonthOldDate + " 00:00:00");
							LocalDateTime thirtyMinuteOldData = DateUtil
									.stringToLocalDateTime(indicesOneMonthMinuteData.get(0).getDate());
							for (IndicesMinuteGraph indicesMinuteGraph : indicesOneMonthMinuteData) {
								String[] date = DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate()).toString()
										.split("T");
								String[] oldDate = thirtyMinuteOldData.toString().split("T");
								if (!date[0].contains(oldDate[0])) {
									thirtyMinuteOldData = DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate());
								}
								if (latestDate.equals(DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate()))
										|| DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
												.isAfter(latestDate)) {
									if (DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
											.equals(thirtyMinuteOldData)) {
										indicesMinutesGraphList.add(indicesMinuteGraph);
										thirtyMinuteOldData = thirtyMinuteOldData.minusMinutes(30);
									} else {
										if (DateUtil.stringToLocalDateTime(indicesMinuteGraph.getDate())
												.equals(thirtyMinuteOldData)) {
											log.info("1 month 30 mint data :- " + indicesMinuteGraph.getDate());
											previous_close = indicesMinuteGraph.getClose();
											break;
										}
									}
								}
							}
						}
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
						LocalDate previousDate = currentDate.minusMonths(6).minusDays(1);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedPreviousDate = previousDate.format(formatter);
						System.out.println("Previous date: " + formattedPreviousDate);
						indicesDayGraphList = indicesDayGraphRepository.findAllSixMonthDaysData(
								timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry(), formattedPreviousDate);

						previous_close = indicesDayGraphList.get(indicesDayGraphList.size() - 1).getClose();
						indicesDayGraphList.remove(indicesDayGraphList.size() - 1);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
						LocalDate previousDate = currentDate.minusYears(1).minusDays(1);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedPreviousDate = previousDate.format(formatter);
						System.out.println("Previous date: " + formattedPreviousDate);
						indicesDayGraphList = indicesDayGraphRepository.findAllOneYearMonthDaysData(
								timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry(), formattedPreviousDate);
						previous_close = indicesDayGraphList.get(indicesDayGraphList.size() - 1).getClose();
						indicesDayGraphList.remove(indicesDayGraphList.size() - 1);
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.FIVE_YEAR)) {
						LocalDate previousDate = currentDate.minusYears(5);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formattedPreviousDate = previousDate.format(formatter);
						System.out.println("Previous date: " + formattedPreviousDate);
						indicesFiveYearData = indicesDayGraphRepository.findAllFiveYearMonthDaysData(
								timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry(), formattedPreviousDate);
						if (indicesFiveYearData != null && !indicesFiveYearData.isEmpty()) {
							String fiveYearOldDate = DateUtil.getPreviousDateTimes(indicesFiveYearData.get(0).getDate(),
									Constant.FIVE_YEAR);
							LocalDateTime latestDate = DateUtil.stringToLocalDateTime(fiveYearOldDate + " 00:00:00");
							LocalDateTime oneWeekOldData = DateUtil
									.stringToLocalDateTime(indicesFiveYearData.get(0).getDate());
							for (IndicesDayGraph indicesDayGraph : indicesFiveYearData) {
								String[] date = DateUtil.stringToLocalDateTime(indicesDayGraph.getDate()).toString()
										.split("T");
								String[] oldDate = oneWeekOldData.toString().split("T");
								if (latestDate.equals(DateUtil.stringToLocalDateTime(indicesDayGraph.getDate()))
										|| DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
												.isAfter(latestDate)) {
									if (DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
											.equals(oneWeekOldData)) {
										indicesDayGraphList.add(indicesDayGraph);
										oneWeekOldData = oneWeekOldData.minusDays(7);
									}
								}

//								else {
//									if (DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
//											.equals(oneWeekOldData)) {
//										log.info("5 year 1 week data :- " + indicesDayGraph.getDate());
//										previous_close = indicesDayGraph.getClose();
//										break;
//									}
//								}
							}
//							if(!(DateUtil.stringToLocalDateTime(indicesDayGraphList.get(indicesDayGraphList.size()-1).getDate()).equals(oneWeekOldData))) {
//								oneWeekOldData = oneWeekOldData.minusDays(7);
//								for (IndicesDayGraph indicesDayGraph : indicesFiveYearData) {
//									String[] date = DateUtil.stringToLocalDateTime(indicesDayGraph.getDate()).toString()
//											.split("T");
//									String[] oldDate = oneWeekOldData.toString().split("T");
//									if (latestDate.equals(DateUtil.stringToLocalDateTime(indicesDayGraph.getDate()))
//											|| DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
//													.isAfter(latestDate)) {
//										if (DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
//												.equals(oneWeekOldData)) {
//											indicesDayGraphList.add(indicesDayGraph);
//											oneWeekOldData = oneWeekOldData.minusDays(7);
//										}
//									}
//									
//									
////									else {
////										if (DateUtil.stringToLocalDateTime(indicesDayGraph.getDate())
////												.equals(oneWeekOldData)) {
////											log.info("5 year 1 week data :- " + indicesDayGraph.getDate());
////											previous_close = indicesDayGraph.getClose();
////											break;
////										}
////									}
//								}
//
//							}
						}
					}
					if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
						indicesYearGraphList = indicesYearGraphRepository
								.findBySymbolAndCountry(timeSeriesRequest.getSymbol(), timeSeriesRequest.getCountry());
						log.info("lat year data :- "
								+ indicesYearGraphList.get(indicesYearGraphList.size() - 1).getDate());
						previous_close = indicesYearGraphList.get(indicesYearGraphList.size() - 1).getClose();
						indicesYearGraphList.remove(indicesYearGraphList.size() - 1);
					}
					if (!indicesYearGraphList.isEmpty() || !indicesDayGraphList.isEmpty()
							|| !indicesMinutesGraphList.isEmpty()) {
						Double change, percentChange;
						TimeSeriesResponse timeSeriesResponse = new TimeSeriesResponse();
						if (!indicesYearGraphList.isEmpty()) {
							for (IndicesYearGraph indicesYearGraph : indicesYearGraphList) {
								TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
								BeanUtils.copyProperties(indicesYearGraph, seriesDetails);
								indicesGraphList2.add(seriesDetails);
							}
						} else if (!indicesDayGraphList.isEmpty()) {
							for (IndicesDayGraph indicesDayGraph : indicesDayGraphList) {
								TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
								BeanUtils.copyProperties(indicesDayGraph, seriesDetails);
								indicesGraphList2.add(seriesDetails);
							}
						} else if (!indicesMinutesGraphList.isEmpty()) {
							for (IndicesMinuteGraph indicesMinuteGraph : indicesMinutesGraphList) {
								TimeSeriesDetails seriesDetails = new TimeSeriesDetails();
								BeanUtils.copyProperties(indicesMinuteGraph, seriesDetails);
								indicesGraphList2.add(seriesDetails);
							}
						}
						/* functionality for indices market data */
						IndicesDetails indicesDetails = new IndicesDetails();
						BeanUtils.copyProperties(indices, indicesDetails);
						TimeSeriesDetails highestTimeSeries = indicesGraphList2.stream()
								.max(Comparator.comparing(TimeSeriesDetails::getHigh)).get();
						TimeSeriesDetails lowestTimeSeries = indicesGraphList2.stream()
								.min(Comparator.comparing(TimeSeriesDetails::getLow)).get();
						log.info("" + indicesGraphList2.get(indicesGraphList2.size() - 1).getClose());
						change = Double.parseDouble(indicesGraphList2.get(indicesGraphList2.size() - 1).getClose())
								- Double.parseDouble(
										indicesGraphList2.get(0).getClose()); /* change = prev.close - current price */
						percentChange = (change
								/ Double.parseDouble(indicesGraphList2.get(indicesGraphList2.size() - 1).getClose())
								* 100);
						indicesDetails.setChange(change.toString());
						indicesDetails.setPercent_change(percentChange.toString());
						indicesDetails.setDatetime(DateUtil.convertUnixTimeStampToStringDate(indices.getTimestamp(),
								country.getTimeZone()));
						indicesDetails.setKeyValueResponseList(
								MethodUtil.indicesMarketData(indices));/* IndicesMarketDetails */
						/* End indices market data */

						timeSeriesResponse.setHigh(highestTimeSeries.getHigh());
						timeSeriesResponse.setLow(lowestTimeSeries.getLow());
						timeSeriesResponse.setPrevious_close(previous_close);
						timeSeriesResponse.setValues(indicesGraphList2);
						indicesTimeSeriesResponse.setIndicesDetails(indicesDetails);
						indicesTimeSeriesResponse.setTimeSeriesResponse(timeSeriesResponse);

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
						map.put(Constant.DATA, indicesTimeSeriesResponse);
						log.info("Data found! status - {}", Constant.OK);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						log.info(Constant.DATA_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.SYMBOL_NOT_FOUND);
					log.info(Constant.SYMBOL_NOT_FOUND + " status - {}", Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
				log.error("Country not found : status - {}", Constant.NOT_FOUND);
			}
		} catch (IllegalArgumentException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		} catch (DataAccessResourceFailureException e) {
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_DB_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;

	}
}