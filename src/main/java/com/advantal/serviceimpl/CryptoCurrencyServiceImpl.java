
package com.advantal.serviceimpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.advantal.exception.BadRequestException;
import com.advantal.model.Assets;
import com.advantal.model.BtcDominance;
import com.advantal.model.Crypto;
import com.advantal.model.CryptoExchange;
import com.advantal.model.FavoriteCrypto;
import com.advantal.model.Portfolio;
import com.advantal.model.User;
import com.advantal.model.Wallet;
import com.advantal.repository.BtcDominanceRepository;
import com.advantal.repository.CryptoExchangeRepository;
import com.advantal.repository.CryptoRepository;
import com.advantal.repository.FavoriteCryptoRepository;
import com.advantal.repository.UserRepository;
import com.advantal.requestpayload.CryptoRequest;
import com.advantal.requestpayload.CryptoTopGainersAndLosersRequest;
import com.advantal.requestpayload.PaginationPayLoad;
import com.advantal.responsepayload.CryptoCurrencyDetailsResponse;
import com.advantal.responsepayload.CryptoGraphData;
import com.advantal.responsepayload.CryptoGraphResponse;
import com.advantal.responsepayload.CryptoMarketChart;
import com.advantal.responsepayload.CryptoResponse;
import com.advantal.responsepayload.CryptoResponsePage;
import com.advantal.responsepayload.KeyValueGlobalResponse;
import com.advantal.responsepayload.KeyValueResponse;
import com.advantal.responsepayload.NewlyListedCryptoResponsePage;
import com.advantal.responsepayload.NewlyListedItemsResponse;
import com.advantal.responsepayload.RelatedNews;
import com.advantal.responsepayload.TSCryptoTopGainersAnLosersResponse;
import com.advantal.responsepayload.TsCryptoDetailsResponse;
import com.advantal.responsepayload.TsCryptoGlobalStatusResponse;
import com.advantal.responsepayload.TsCryptoListPrice;
import com.advantal.responsepayload.TsCryptoRatingData;
import com.advantal.responsepayload.TsCryptoRatingResponse;
import com.advantal.responsepayload.TsCryptoResponse;
import com.advantal.responsepayload.TsCryptoResponse1;
import com.advantal.responsepayload.TsCryptoTopGainersAndLosersData;
import com.advantal.responsepayload.TsData1;
import com.advantal.responsepayload.TsDescription;
import com.advantal.responsepayload.TsItem;
import com.advantal.responsepayload.TsItemsNewlyListed;
import com.advantal.responsepayload.TsNewsResponse;
import com.advantal.responsepayload.TsNewsResponseData;
import com.advantal.responsepayload.TsPrice;
import com.advantal.responsepayload.TsTradingMarket;
import com.advantal.responsepayload.TsTradingPairResponse;
//import com.advantal.responsepayload.TsPrice;
import com.advantal.service.CryptoCurrencyService;
import com.advantal.utils.Constant;
import com.advantal.utils.DateUtil;
import com.advantal.utils.MethodUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

	@Autowired
	CryptoRepository cryptoRepository;

	@Autowired
	ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired(required = true)
	MethodUtil methodUtil;

	@Autowired
	FavoriteCryptoRepository favoriteCryptoRepository;

	@Autowired
	CryptoExchangeRepository cryptoExchangeRepository;

	@Autowired
	BtcDominanceRepository btcDominanceRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public void saveCryptoList(Integer limit, Integer offset) {
		String cryptoResponseList = thirdPartyApiUtil.getCryptoList(limit, offset);
		try {
			if (cryptoResponseList != null) {
				Gson gson = new Gson();
				TsCryptoResponse data = gson.fromJson(cryptoResponseList, TsCryptoResponse.class);
				List<Crypto> cryptoList = new ArrayList<>();
				for (TsItem tsItem : data.getData().getItems()) {
					Crypto crypto = new Crypto();
					BeanUtils.copyProperties(tsItem, crypto);
					crypto.setCryptoId(tsItem.getId());
					cryptoList.add(crypto);
				}
				cryptoRepository.saveAll(cryptoList);

			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public Map<String, Object> getCryptoDetails(CryptoRequest cryptoRequest) {
		Map<String, Object> response = new HashMap<>();
		CryptoCurrencyDetailsResponse cryptoCurrencyDetailsResponse = new CryptoCurrencyDetailsResponse();
		try {
			User user = userRepository.findByIdAndStatus(cryptoRequest.getUserId(), Constant.ONE);
			if (user != null) {
				/* crypto analyst rating */
				Crypto crypto = cryptoRepository.findByIdAndCryptoId(cryptoRequest.getInstrumentId(),
						cryptoRequest.getCryptoId());
				if (crypto != null) {
					BeanUtils.copyProperties(crypto, cryptoCurrencyDetailsResponse);
					cryptoCurrencyDetailsResponse.setInstrumentId(crypto.getId());
					List<FavoriteCrypto> favoriteCryptoList = favoriteCryptoRepository
							.findByUserId(cryptoRequest.getUserId());
					if (favoriteCryptoList.size() > 0) {
						for (FavoriteCrypto favoriteCrypto : favoriteCryptoList) {
							if (favoriteCrypto.getCrypto().getId().equals(crypto.getId())) {
								cryptoCurrencyDetailsResponse.setFavorite(Constant.TRUE);
							}
						}
					}
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.INSTRUMENT_NOT_FOUND_MESSAGE);
					log.info(Constant.INSTRUMENT_NOT_FOUND_MESSAGE + "! status - {}", Constant.OK);
					return response;
				}
//			/* crypto analyst rating */
				String rating = thirdPartyApiUtil.getAnalystRating(cryptoRequest.getCryptoId());
				log.info("rating data found successfully " + rating);
				/* crypto details */
				String cryptoDetailsResponse = thirdPartyApiUtil.getCryptoDetails(cryptoRequest.getCryptoId());

				if ((!cryptoDetailsResponse.isBlank())) {
					log.info("data found ! status - {}", cryptoDetailsResponse);
					/* using objectMapper */
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					/* get CryptoMarketDetails */
					Map<?, ?> mapResponse = mapper.readValue(cryptoDetailsResponse, Map.class);
					Integer code = (Integer) mapResponse.get("code");
					if (code == null) {
						Double max_supply = 0.0, fully_diluted = 0.0, circulating = 0.0, market = 0.0, volume = 0.0;
						String price = "0", percenatge = "0", rank = "0";
						TsCryptoDetailsResponse data = new TsCryptoDetailsResponse();
						data = mapper.convertValue(mapResponse, TsCryptoDetailsResponse.class);
						log.info("data found ! status - {}", data);
						cryptoCurrencyDetailsResponse.setCryptoId(data.getData().getId());
						cryptoCurrencyDetailsResponse.setName(data.getData().getName());
						cryptoCurrencyDetailsResponse.setLogo(data.getData().getLogo());
						cryptoCurrencyDetailsResponse.setSymbol(data.getData().getSymbol());
						/* set Description */
						for (TsDescription description : data.getData().getLocalization()) {
							if (description.getLang().equals("en")) {
								cryptoCurrencyDetailsResponse.setDescription(description.getDescription());
							}
						}
						/* save market data in third party apis */
						for (TsPrice cryptoListPrice : data.getData().getMarket_data().getPrice()) {
							/* CryptoMarketDetails */
							cryptoCurrencyDetailsResponse.setCurrency(cryptoListPrice.getCurrency());
							if (cryptoListPrice.getPrice_latest() == null) {
								price = "0";
							} else {
								price = methodUtil.formattedValues(cryptoListPrice.getPrice_latest());
							}
							if (data.getData().getMarket_data().getCirculating_supply() == null) {
								circulating = 0.0;
							} else {
								circulating = data.getData().getMarket_data().getCirculating_supply();
							}
							if (data.getData().getMarket_data().getMax_supply() == null) {
								max_supply = 0.0;
							} else {
								max_supply = data.getData().getMarket_data().getMax_supply();
							}
							if (cryptoListPrice.getFully_diluted_valuation() == null) {
								fully_diluted = 0.0;
							} else {
								fully_diluted = cryptoListPrice.getFully_diluted_valuation();
							}
							if (cryptoListPrice.getMarket_cap() == null) {
								market = 0.0;
							} else {
								market = cryptoListPrice.getMarket_cap();
							}
							if (cryptoListPrice.getVol_spot_24h() == null) {
								volume = 0.0;
							} else {
								volume = cryptoListPrice.getVol_spot_24h();
							}
							if (data.getData().getRank() != null) {
								rank = data.getData().getRank().toString();
							} else {
								rank = "0";
							}
//							cryptoCurrencyDetailsResponse.setUpdate_date(data.getData().getMarket_data().getLast_updated());
							cryptoCurrencyDetailsResponse.setUpdate_date(data.getStatus().getTimestamp());

							//
							Assets oldAssets = new Assets();
							boolean isFound = false;
							Portfolio oldPortfolio = new Portfolio();
							for (int i = 1; i < user.getPortfolioList().size(); i++) {
								if (user.getPortfolioList().get(i).getId().equals(cryptoRequest.getPortfolioId())) {
									isFound = true;
									BeanUtils.copyProperties(user.getPortfolioList().get(i), oldPortfolio);
									for (Wallet wallet : oldPortfolio.getWalletList()) {
										if (wallet.getStatus().equals(Constant.ONE)) {
											for (Assets assets : wallet.getAssetsList()) {
												if (cryptoRequest.getCryptoId().equalsIgnoreCase(assets.getSymbol())) {
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
//								response.put(Constant.RESPONSE_CODE, Constant.OK);
//								response.put(Constant.MESSAGE, Constant.PORTFOLIO_ID_NOT_FOUND_MESSAGE);
//								log.info(Constant.PORTFOLIO_ID_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
//								return response;
//							}

							List<KeyValueResponse> keyValueAdvancMatrixList = null;
							if (oldAssets.getId() != null) {
								keyValueAdvancMatrixList = MethodUtil.getAdvanceMatrix(price, oldAssets);
							}
							//

							List<KeyValueResponse> keyValueResponsesList = MethodUtil.cryptoProfileMarketData(
									methodUtil.convertToAbbreviation(circulating),
									methodUtil.convertToAbbreviation(market), methodUtil.convertToAbbreviation(volume),
									methodUtil.convertToAbbreviation(max_supply),
									methodUtil.convertToAbbreviation(fully_diluted), rank);

							/* set high low 24h or 7d */
							cryptoCurrencyDetailsResponse
									.setHigh_24h(methodUtil.formattedValues(cryptoListPrice.getHigh_24h()));
							cryptoCurrencyDetailsResponse
									.setLow_24h(methodUtil.formattedValues(cryptoListPrice.getLow_24h()));
							cryptoCurrencyDetailsResponse
									.setHigh_7d(methodUtil.formattedValues(cryptoListPrice.getHigh_7d()));
							cryptoCurrencyDetailsResponse
									.setLow_7d(methodUtil.formattedValues(cryptoListPrice.getLow_7d()));
							/* End */
							cryptoCurrencyDetailsResponse.setPrice(price);
							if (cryptoListPrice.getPrice_change_percentage_24h() == null) {
								percenatge = "0";
							} else {
								percenatge = methodUtil
										.formattedValues(cryptoListPrice.getPrice_change_percentage_24h());
							}
							log.info("new percentage:-" + percenatge);
							cryptoCurrencyDetailsResponse.setPercent_change(percenatge);
							/* calculate the change value */
							log.info("change_value ! status - {}", methodUtil.calculatePercentageValue(
									Double.parseDouble(price), Double.parseDouble(percenatge)));
							cryptoCurrencyDetailsResponse.setChange_value(methodUtil.calculatePercentageValue(
									Double.parseDouble(price), Double.parseDouble(percenatge)));
							cryptoCurrencyDetailsResponse.setKeyValueResponseList(keyValueResponsesList);
							cryptoCurrencyDetailsResponse.setKeyValueAdvancMatrixList(keyValueAdvancMatrixList);
						}
					} else {
						log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
						throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
					}
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
				List<KeyValueResponse> keyValueResponseRatingList = new ArrayList<KeyValueResponse>();
				if (!rating.isBlank()) {
					/* using objectMapper */
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					/* get crypto analyst rating data */
					Map<?, ?> mapResponse1 = mapper.readValue(rating, Map.class);
					Integer code1 = (Integer) mapResponse1.get("code");
					if (code1 == null) {
						TsCryptoRatingResponse ratingData = new TsCryptoRatingResponse();
						ratingData = mapper.convertValue(mapResponse1, TsCryptoRatingResponse.class);
						/* save crypto analyst rating data */
						for (TsCryptoRatingData cryptoRatingData : ratingData.getData()) {
							cryptoCurrencyDetailsResponse.setRating_level(cryptoRatingData.getRating_level());
							cryptoCurrencyDetailsResponse.setRating_report(cryptoRatingData.getRating_page());
							cryptoCurrencyDetailsResponse.setRating_score(cryptoRatingData.getRating_score());
							cryptoCurrencyDetailsResponse.setData_updated(cryptoRatingData.getUpdate_time());
							cryptoCurrencyDetailsResponse.setLast_Review_Date(cryptoRatingData.getReview_time());
							keyValueResponseRatingList = MethodUtil.cryptoAnalystRatingMarketData(
									cryptoRatingData.getTeam_partners_investors(),
									cryptoRatingData.getToken_economics(),
									cryptoRatingData.getUnderlying_technology_security(),
									cryptoRatingData.getRoadmap_progress(), cryptoRatingData.getToken_performance(),
									cryptoRatingData.getEcosystem_development());
						}
					} else {
						log.info("Bad Request ! status - {}", Constant.BAD_REQUEST);
						throw new BadRequestException(Constant.BAD_REQUEST_MESSAGE);
					}
				} else /* if(cryptoRequest.getCryptoType().equals("newly")) */ {
					log.info("rating data not found " + Constant.OK);
					cryptoCurrencyDetailsResponse.setRating_level(null);
					cryptoCurrencyDetailsResponse.setRating_report(null);
					cryptoCurrencyDetailsResponse.setRating_score(null);
					cryptoCurrencyDetailsResponse.setLast_Review_Date(null);
				}
				cryptoCurrencyDetailsResponse.setKeyValueResponseRatingList(keyValueResponseRatingList);
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				response.put(Constant.DATA, cryptoCurrencyDetailsResponse);
				log.info("crypto detail record found successfully ! status - {}", Constant.OK);
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.USER_ID_NOT_FOUND_MESSAGE);
				log.info(Constant.USER_ID_NOT_FOUND_MESSAGE + " status - {}", Constant.OK);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.info("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	/* new implement 01/11/23 */
	@Override
	public Map<String, Object> getCryptoList(PaginationPayLoad paginationPayLoad) {
		Map<String, Object> response = new HashMap<>();
		String crypto_id = "";
		CryptoResponsePage cryptoResponsePage = new CryptoResponsePage();
		/* get crypto global market status */
		List<Crypto> cryptolist = new ArrayList<>();
		List<Crypto> cryptoListWithoutPagination = new ArrayList<Crypto>();
		List<CryptoResponse> cryptoResponseList = new ArrayList<>();
		List<FavoriteCrypto> favoriteCryptoList = null;
		try {
			User user = userRepository.findByIdAndStatus(paginationPayLoad.getUserId(), Constant.ONE);
			/* using objectMapper */
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			/* get crypto global market status */
			String cryptoGlobalMarketResponse = thirdPartyApiUtil.getCryptoGlobalStatus();
			if (!cryptoGlobalMarketResponse.isBlank()) {
				Map<?, ?> mapResponse2 = mapper.readValue(cryptoGlobalMarketResponse, Map.class);
				Integer code2 = (Integer) mapResponse2.get("code");
				if (code2 == null) {
					log.info("data found ! status - {}", cryptoGlobalMarketResponse);
					TsCryptoGlobalStatusResponse statusResponse = mapper.convertValue(mapResponse2,
							TsCryptoGlobalStatusResponse.class);
					/* save the crypto global market status */
					/* convert value to K,M,T,B */
					String market_cap = methodUtil
							.convertToAbbreviation(statusResponse.getData().getTotal_market_cap());
					String volume = methodUtil.convertToAbbreviation(statusResponse.getData().getSpot_volume_24h());
					String dom = statusResponse.getData().getMarket_dominances().getBtc();
					Double stringToDouble = Double.parseDouble(dom);
					Double dominance = stringToDouble * 100;
					/* get the btcDominance percentage value */
					Double change_percentage = 0.0;
					LocalDate currentDate = LocalDate.now();
					log.info(" current date :- " + currentDate);
					LocalDate previousDate = currentDate.minusDays(1);
					log.info(" one day prevoius date :- " + previousDate);
					BtcDominance btcDominance2 = btcDominanceRepository.getBtcUpdationDate(previousDate.toString());
					if (btcDominance2 != null) {
						/*
						 * formula Double change_percentage = current_btc_dominance - old_btc_dominance;
						 */
						log.info(" current btc dominace values :- " + dominance);
						change_percentage = dominance - btcDominance2.getBtcDominance();
						log.info(" change percentage :- " + change_percentage);
					}
					Double market_percentage = methodUtil.formattedValuesForPercentage(
							statusResponse.getData().getTotal_market_cap_change_percentage_24h() * 100);
					Double volume_percentage = methodUtil.formattedValuesForPercentage(
							statusResponse.getData().getSpot_volume_change_percentage_24h() * 100);
					List<KeyValueGlobalResponse> cryptoGloalStatusResponseList = MethodUtil.cryptoGlobalStatus(
							market_cap, volume, String.format("%.2f", dominance), market_percentage, volume_percentage,
							String.format("%.2f", change_percentage));
					cryptoResponsePage.setCryptoGloalStatusResponsList(cryptoGloalStatusResponseList);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				return response;
			}
			/* get the local server data */
			Page<Crypto> page = null;
			if (paginationPayLoad.getPageSize() != 0) {
				Pageable pageable = PageRequest.of(paginationPayLoad.getPageIndex(), paginationPayLoad.getPageSize());
				if (paginationPayLoad.getKeyWord().isBlank()) {
					page = cryptoRepository.findAllCrypto(pageable);
				} else {
					page = cryptoRepository.findAllCryptoBySearch(paginationPayLoad.getKeyWord(), pageable);
				}
			} else if (paginationPayLoad.getPageIndex() == 0 && paginationPayLoad.getPageSize() == 0
					&& paginationPayLoad.getUserId() == 0 && paginationPayLoad.getKeyWord().isBlank()) {
				cryptoListWithoutPagination = cryptoRepository.findAllCryptosWithoutPagination();
			} else if (paginationPayLoad.getPageIndex() == 0 && paginationPayLoad.getPageSize() == 0
					&& paginationPayLoad.getUserId() == 0
					&& (paginationPayLoad.getKeyWord() != null && !paginationPayLoad.getKeyWord().isBlank())) {
				cryptoListWithoutPagination = cryptoRepository
						.findAllCryptosWithoutPaginationWithSearching(paginationPayLoad.getKeyWord());
			}
			if (page != null && !page.isEmpty()) {
				log.info("data found ! status - {}", page);
				cryptolist = page.getContent();
				for (Crypto crypto : cryptolist) {
					CryptoResponse cryptoResponse = new CryptoResponse();
					/* store crypto id */
					crypto_id = crypto_id + crypto.getCryptoId() + ",";
					cryptoResponse.setInstrumentId(crypto.getId());
					cryptoResponse.setCryptoId(crypto.getCryptoId());
					cryptoResponse.setLogo(crypto.getLogo());
					cryptoResponse.setName(crypto.getName());
					cryptoResponse.setCurrency(crypto.getCurrency());
					cryptoResponse.setSymbol(crypto.getSymbol());
					cryptoResponse.setInstrumentType(crypto.getInstrumentType());
					cryptoResponse.setFavorite(crypto.getFavorite());
					cryptoResponseList.add(cryptoResponse);
				}
				if (crypto_id != "") {
					int index = crypto_id.lastIndexOf(",");
					crypto_id = crypto_id.substring(0, index);
				}
				/* get crypto list in third part apis */
				String res = thirdPartyApiUtil.getCryptoList(crypto_id);
//					String res = thirdPartyApiUtil.getCryptoList(paginationPayLoad.getPageSize(),
//							paginationPayLoad.getPageIndex() * 10);
				String price = "0", percentage = "0";
				if (res != null && !res.isBlank()) {
					Gson gson = new Gson();
					TsCryptoResponse1 data = gson.fromJson(res, TsCryptoResponse1.class);
					favoriteCryptoList = favoriteCryptoRepository.findByUserId(paginationPayLoad.getUserId());
					if (favoriteCryptoList.size() > 0) {
						for (CryptoResponse cryptoResponse : cryptoResponseList) {
							for (TsData1 item : data.getData()) {
								for (TsCryptoListPrice cryptoListPrice : item.getPrice()) {
									if (cryptoResponse.getCryptoId().equals(item.getId())) {
										price = methodUtil.formattedValues(cryptoListPrice.getPrice_latest() != null
												? cryptoListPrice.getPrice_latest()
												: 0d);
										percentage = methodUtil.formattedValues(
												cryptoListPrice.getPrice_change_percentage_24h() != null
														? cryptoListPrice.getPrice_change_percentage_24h()
														: 0d);
										cryptoResponse.setPrice(price);
										cryptoResponse.setPercent_change(percentage);
										cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
												Double.parseDouble(price), Double.parseDouble(percentage)));
									}
								}

//									if (cryptoResponse.getCryptoId().equals(item.getId())) {
//										price = methodUtil.formattedValues(item.getPrice());
//										percentage = methodUtil.formattedValues(item.getPrice_change_percentage_24h());
//										cryptoResponse.setPrice(price);
//										cryptoResponse.setPercent_change(percentage);
//										cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
//												Double.parseDouble(price), Double.parseDouble(percentage)));
//									}
							}
							// favorites
							for (FavoriteCrypto favoriteCrypto : favoriteCryptoList) {
								if (cryptoResponse.getInstrumentId().equals(favoriteCrypto.getCrypto().getId())) {
									cryptoResponse.setFavorite(Constant.TRUE);
								}
							}
						}
					} else {
						for (CryptoResponse cryptoResponse : cryptoResponseList) {
							for (TsData1 item : data.getData()) {
								for (TsCryptoListPrice cryptoListPrice : item.getPrice()) {
									if (cryptoResponse.getCryptoId().equals(item.getId())) {
										price = methodUtil.formattedValues(cryptoListPrice.getPrice_latest() != null
												? cryptoListPrice.getPrice_latest()
												: 0d);
										percentage = methodUtil.formattedValues(
												cryptoListPrice.getPrice_change_percentage_24h() != null
														? cryptoListPrice.getPrice_change_percentage_24h()
														: 0d);
										cryptoResponse.setPrice(price);
										cryptoResponse.setPercent_change(percentage);
										cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
												Double.parseDouble(price), Double.parseDouble(percentage)));
									}
								}
							}
						}
					}
					cryptoResponsePage.setIsFirstPage(page.isFirst());
					cryptoResponsePage.setIsLastPage(page.isLast());
					cryptoResponsePage.setPageIndex(page.getNumber());
					cryptoResponsePage.setPageSize(page.getSize());
					cryptoResponsePage.setTotalElement(page.getTotalElements());
					cryptoResponsePage.setTotalPages(page.getTotalPages());
					cryptoResponsePage.setCryptoResponseList(cryptoResponseList);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					response.put(Constant.DATA, cryptoResponsePage);
					log.info("data found successfully ! status - {}", Constant.OK);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
					log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
//					log.info("data found ! status - {}", page);
//					cryptolist = page.getContent();
				for (Crypto crypto : cryptoListWithoutPagination) {
					CryptoResponse cryptoResponse = new CryptoResponse();
					/* store crypto id */
//						crypto_id = crypto_id + crypto.getCryptoId() + ",";
					cryptoResponse.setInstrumentId(crypto.getId());
					cryptoResponse.setCryptoId(crypto.getCryptoId());
					cryptoResponse.setLogo(crypto.getLogo());
					cryptoResponse.setName(crypto.getName());
					cryptoResponse.setCurrency(crypto.getCurrency());
					cryptoResponse.setSymbol(crypto.getSymbol());
					cryptoResponse.setInstrumentType(crypto.getInstrumentType());
					cryptoResponse.setFavorite(crypto.getFavorite());
					cryptoResponseList.add(cryptoResponse);
				}
//						if (crypto_id != "") {
//							int index = crypto_id.lastIndexOf(",");
//							crypto_id = crypto_id.substring(0, index);
//						}
				/* get crypto list in third part apis */
//						String res = thirdPartyApiUtil.getCryptoList(crypto_id);
//					String res = thirdPartyApiUtil.getCryptoList(paginationPayLoad.getPageSize(),
//							paginationPayLoad.getPageIndex() * 10);
//					String price = "0", percentage = "0";
//					if (res != null && !res.isBlank()) {
//						Gson gson = new Gson();
//						TsCryptoResponse data = gson.fromJson(res, TsCryptoResponse.class);
				favoriteCryptoList = favoriteCryptoRepository.findByUserId(paginationPayLoad.getUserId());
				if (favoriteCryptoList.size() > 0) {
					for (CryptoResponse cryptoResponse : cryptoResponseList) {
//								for (TsItem item : data.getData().getItems()) {
//									if (cryptoResponse.getCryptoId().equals(item.getId())) {
//										price = methodUtil.formattedValues(item.getPrice());
//										percentage = methodUtil.formattedValues(item.getPrice_change_percentage_24h());
//										cryptoResponse.setPrice(price);
//										cryptoResponse.setPercent_change(percentage);
//										cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
//												Double.parseDouble(price), Double.parseDouble(percentage)));
//									}
//								}
//								// favorites
						for (FavoriteCrypto favoriteCrypto : favoriteCryptoList) {
							if (cryptoResponse.getInstrumentId().equals(favoriteCrypto.getCrypto().getId())) {
								cryptoResponse.setFavorite(Constant.TRUE);
							}
						}
					}
				}
				log.info("crypto list size :- " + cryptoResponseList.size());

//						else {
//							for (CryptoResponse cryptoResponse : cryptoResponseList) {
//								for (TsItem item : data.getData().getItems()) {
//									if (cryptoResponse.getCryptoId().equals(item.getId())) {
//										price = methodUtil.formattedValues(item.getPrice());
//										percentage = methodUtil.formattedValues(item.getPrice_change_percentage_24h());
//										cryptoResponse.setPrice(price);
//										cryptoResponse.setPercent_change(percentage);
//										cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
//												Double.parseDouble(price), Double.parseDouble(percentage)));
//									}
//								}
//							}
//						}
				cryptoResponsePage.setIsFirstPage(null);
				cryptoResponsePage.setIsLastPage(null);
				cryptoResponsePage.setPageIndex(null);
				cryptoResponsePage.setPageSize(null);
				cryptoResponsePage.setTotalElement(null);
				cryptoResponsePage.setTotalPages(null);
				cryptoResponsePage.setCryptoResponseList(cryptoResponseList);
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				response.put(Constant.DATA, cryptoResponsePage);
				log.info("data found successfully ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getNewsList(String id) {
		Map<String, Object> map = new HashMap<>();
		try {
			if (!id.isBlank() && id != null) {
				String newsresponse = thirdPartyApiUtil.getNews(id);
				if (!newsresponse.isBlank()) {
					Gson gson = new Gson();
					TsNewsResponse data = gson.fromJson(newsresponse, TsNewsResponse.class);
					List<RelatedNews> relatedNewsList = new ArrayList<>();
					for (TsNewsResponseData responseData : data.getData()) {
						for (RelatedNews relatedNews : responseData.getRelated_news()) {
							RelatedNews news = new RelatedNews();
							news.setTitle(relatedNews.getTitle());
							news.setContent(relatedNews.getContent());
							news.setImage_url(relatedNews.getImage_url());
							news.setUrl(relatedNews.getUrl());
							long timestampMillis = Long.parseLong(relatedNews.getTimestamp());
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// Convert the timestamp to a Date object
							Date date = new Date(timestampMillis);
							// Format the Date object as a string
							String dateString = dateFormat.format(date);
							// Set the converted local timestamp
							news.setTimestamp(dateString);
							relatedNewsList.add(news);
						}
					}
					if (!relatedNewsList.isEmpty()) {
						map.put(Constant.DATA, relatedNewsList);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
						log.info("Data found! status - {}", Constant.OK);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						log.info(" data not found! status - {}", Constant.NOT_FOUND);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
					log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.CRYPTO_ID_NOT_EMPTY);
				log.info("crypto id can't be null ! status - {}", Constant.BAD_REQUEST);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return map;

	}

	@Override
	public Map<String, Object> getTopGainersAndLosers(CryptoTopGainersAndLosersRequest gainersAndLosersRequest) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			List<CryptoResponse> topGainersAndLosersList = new ArrayList<>();
			Gson gson = new Gson();
			String topGainersAndLosersResponse = thirdPartyApiUtil
					.getCryptoTopGainersAndLosers(gainersAndLosersRequest.getTop(), gainersAndLosersRequest.getRange());
			if (!topGainersAndLosersResponse.isEmpty()) {
				if (!topGainersAndLosersResponse.equals("Invalid Request !!")) {
					List<FavoriteCrypto> favoriteCryptoList = favoriteCryptoRepository
							.findByUserId(gainersAndLosersRequest.getUserId());
					if (favoriteCryptoList.size() > 0) {
						TSCryptoTopGainersAnLosersResponse data = gson.fromJson(topGainersAndLosersResponse,
								TSCryptoTopGainersAnLosersResponse.class);
						for (TsCryptoTopGainersAndLosersData gainersAndLosersData : data.getData()) {
							/* compare to local crypto id to top gainers and losers id */
							Crypto crypto = cryptoRepository.findByCryptoId(gainersAndLosersData.getId());
							if (crypto != null) {
								String value = "0", price = "0";
								CryptoResponse cryptoResponse = new CryptoResponse();
								BeanUtils.copyProperties(crypto, cryptoResponse);
								cryptoResponse.setCryptoId(crypto.getCryptoId());
								cryptoResponse.setInstrumentId(crypto.getId());
								if (gainersAndLosersData.getPrice_change_24h() == null) {
									value = "0";
								} else {
									value = methodUtil.formattedValues(gainersAndLosersData.getPrice_change_24h());
								}
								if (gainersAndLosersData.getPrice() == null) {
									price = "0";
								} else {
									price = methodUtil.formattedValues(gainersAndLosersData.getPrice());
								}
								/* chnge 17 oct */
//								cryptoResponse.setChange_value(value * 100);
								cryptoResponse.setPrice(price);
//								Double change_percentage = methodUtil
//										.calculatePercentageValue(Double.parseDouble(price), Double.parseDouble(value));
								/* chnge 17 oct */
								cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
										Double.parseDouble(price), Double.parseDouble(value)));
								cryptoResponse.setPercent_change(value);
								/* Setting favorite value true here */
								for (FavoriteCrypto favoriteCrypto : favoriteCryptoList) {
									if (favoriteCrypto.getCrypto().getId().equals(cryptoResponse.getInstrumentId())) {
										cryptoResponse.setFavorite(Constant.TRUE);
									}
								}
								topGainersAndLosersList.add(cryptoResponse);
							} else {
								response.put(Constant.RESPONSE_CODE, Constant.OK);
								response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
								log.info("crypto top gainers and losers not found ! status - {}", Constant.OK);
								return response;
							}
						}
					} else {
						TSCryptoTopGainersAnLosersResponse data = gson.fromJson(topGainersAndLosersResponse,
								TSCryptoTopGainersAnLosersResponse.class);
						for (TsCryptoTopGainersAndLosersData gainersAndLosersData : data.getData()) {
							/* compare to local crypto id to top gainers and losers id */
							Crypto crypto = cryptoRepository.findByCryptoId(gainersAndLosersData.getId());
							if (crypto != null) {
								String value = "0", price = "0";
								CryptoResponse cryptoResponse = new CryptoResponse();
								BeanUtils.copyProperties(gainersAndLosersData, cryptoResponse);
								if (gainersAndLosersData.getPrice_change_24h() == null) {
									value = "0";
								} else {
									value = methodUtil.formattedValues(gainersAndLosersData.getPrice_change_24h());
								}
								if (gainersAndLosersData.getPrice() == null) {
									price = "0";
								} else {
									price = methodUtil.formattedValues(gainersAndLosersData.getPrice());
								}
								/* chnge 17 oct */
//								cryptoResponse.setChange_value(value * 100);
								cryptoResponse.setCryptoId(gainersAndLosersData.getId());
								cryptoResponse.setInstrumentId(crypto.getId());
								cryptoResponse.setInstrumentType(crypto.getInstrumentType());
								cryptoResponse.setCurrency(crypto.getCurrency());
								cryptoResponse.setFavorite(crypto.getFavorite());
//								Double price = methodUtil.formattedValues(gainersAndLosersData.getPrice());
//								Double change_percentage = methodUtil
//										.calculatePercentageValue(Double.parseDouble(price), Double.parseDouble(value));
//								Double percentage = Double.valueOf(change_percentage);
								/* chnge 17 oct */
								cryptoResponse.setChange_value(methodUtil.calculatePercentageValue(
										Double.parseDouble(price), Double.parseDouble(value)));
								cryptoResponse.setPercent_change(value);
								cryptoResponse.setPrice(price);
								topGainersAndLosersList.add(cryptoResponse);
							} else {
								response.put(Constant.RESPONSE_CODE, Constant.OK);
								response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
								log.info("crypto top gainers and losers not found ! status - {}", Constant.OK);
								return response;
							}
						}
					}
					response.put(Constant.TOP_GAINERS_LOSERS, topGainersAndLosersList);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.DATA_FOUND_MESSAGE);
					log.info("crypto top gainers and losers found successfully ! status - {}", Constant.OK);

				} else {
					response.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					response.put(Constant.MESSAGE, Constant.INVALID_REQUEST);
					log.error(Constant.INVALID_REQUEST + "! status - {}", Constant.BAD_REQUEST);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	/* new implement 13/09/23 */
	@Override
	public Map<String, Object> getNewlyListed(PaginationPayLoad paginationPayLoad) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<CryptoResponse> cryptoResponseList = new ArrayList<CryptoResponse>();

		String crypto_id = "";
		List<FavoriteCrypto> favoriteCryptoList = null;
		NewlyListedCryptoResponsePage listedCryptoResponsePage = new NewlyListedCryptoResponsePage();
		List<Crypto> list = new ArrayList<Crypto>();
		LocalDate currentDate = LocalDate.now();
		Page<Crypto> page = null;
		// Calculate one year ago from the current date
		LocalDate oneYearAgoDate = currentDate.minus(Period.ofYears(1));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Format the one year ago date as a string
		String formattedOneYearAgoDate = oneYearAgoDate.format(formatter);
		try {
			if (paginationPayLoad.getPageSize() != 0) {
				Pageable pageable = PageRequest.of(paginationPayLoad.getPageIndex(), paginationPayLoad.getPageSize());
				if (paginationPayLoad.getKeyWord().isBlank()) {
					page = cryptoRepository.findAllNewlyListedCrypto(pageable, formattedOneYearAgoDate);
				} else {
					page = cryptoRepository.findAllNewlyListedCryptoBySearch(paginationPayLoad.getKeyWord(),
							formattedOneYearAgoDate, pageable);
				}

			} else {
				if (!paginationPayLoad.getKeyWord().isBlank()) {
					list = cryptoRepository.findAllNewlyListedCryptosWithSearching(paginationPayLoad.getKeyWord(),
							formattedOneYearAgoDate);
				} else {
					list = cryptoRepository.findAllNewlyListedCryptos(formattedOneYearAgoDate);
				}
			}
			if (page != null && !page.isEmpty()) {
				list = page.getContent();
			}
			/* get the data our local database */
			for (Crypto crypto : list) {
				CryptoResponse cryptoResponse = new CryptoResponse();
				/* store crypto id */
				crypto_id = crypto_id + crypto.getCryptoId() + ",";
				cryptoResponse.setInstrumentId(crypto.getId());
				cryptoResponse.setCryptoId(crypto.getCryptoId());
				cryptoResponse.setLogo(crypto.getLogo());
				cryptoResponse.setName(crypto.getName());
				cryptoResponse.setSymbol(crypto.getSymbol());
				cryptoResponse.setCurrency(crypto.getCurrency());
				cryptoResponse.setInstrumentType(crypto.getInstrumentType());
				cryptoResponse.setFavorite(crypto.getFavorite());
				cryptoResponseList.add(cryptoResponse);
			}
			/* calling thrid part */
//				String res = thirdPartyApiUtil.getCryptoList(crypto_id);
			String res = thirdPartyApiUtil.getNewlyListed(paginationPayLoad.getPageSize(),
					paginationPayLoad.getPageIndex() * 10);
			NewlyListedItemsResponse cryptoListResponse = new NewlyListedItemsResponse();
			if (res != null & !res.isBlank()) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> mapResponse = mapper.readValue(res, Map.class);
				cryptoListResponse = mapper.convertValue(mapResponse, NewlyListedItemsResponse.class);
				if (!cryptoListResponse.getData().getItems().isEmpty() && cryptoListResponse.getData() != null) {
					/* get the favorite crypto list */
					favoriteCryptoList = favoriteCryptoRepository.findByUserId(paginationPayLoad.getUserId());
					if (favoriteCryptoList.size() > 0) {
						for (CryptoResponse cryptoResponse : cryptoResponseList) {
							for (TsItemsNewlyListed newlyListed : cryptoListResponse.getData().getItems()) {
								if (cryptoResponse.getCryptoId().equals(newlyListed.getId())) {
									String change_value = "0", price = "0";
									if (newlyListed.getPrice() == null) {
										price = "0";
									} else {
										price = methodUtil.formattedValues(Double.parseDouble(newlyListed.getPrice()));
									}
									if (newlyListed.getPrice_change_24h() == null) {
										change_value = "0";
									} else {
										change_value = methodUtil
												.formattedValues(Double.parseDouble(newlyListed.getPrice_change_24h()));
									}
									cryptoResponse.setPrice(methodUtil.formattedValues(Double.parseDouble(price)));
									cryptoResponse.setChange_value(
											methodUtil.formattedValues(Double.parseDouble(change_value)));
									cryptoResponse.setPercent_change(methodUtil.calculatePercentageValue(
											Double.parseDouble(price), Double.parseDouble(change_value)));
								}
								/* add favorite */
								for (FavoriteCrypto favoriteCrypto : favoriteCryptoList) {
									if (cryptoResponse.getInstrumentId().equals(favoriteCrypto.getCrypto().getId())) {
										cryptoResponse.setFavorite(Constant.TRUE);
									}
								}
							}
						}
					} else {
						for (CryptoResponse cryptoResponse : cryptoResponseList) {
							for (TsItemsNewlyListed newlyListed : cryptoListResponse.getData().getItems()) {
								if (cryptoResponse.getCryptoId().equals(newlyListed.getId())) {
									String change_value = "0", price = "0";
									if (newlyListed.getPrice() == null) {
										price = "0";
									} else {
										price = methodUtil.formattedValues(Double.parseDouble(newlyListed.getPrice()));
									}
									if (newlyListed.getPrice_change_24h() == null) {
										change_value = "0";
									} else {
										change_value = methodUtil
												.formattedValues(Double.parseDouble(newlyListed.getPrice_change_24h()));
									}
									cryptoResponse.setPrice(methodUtil.formattedValues(Double.parseDouble(price)));
									cryptoResponse.setChange_value(
											methodUtil.formattedValues(Double.parseDouble(change_value)));
									cryptoResponse.setPercent_change(methodUtil.calculatePercentageValue(
											Double.parseDouble(price), Double.parseDouble(change_value)));
								}
							}
						}
					}
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
					log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.error(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
			listedCryptoResponsePage.setIsFirstPage(page != null ? page.isFirst() : null);
			listedCryptoResponsePage.setIsLastPage(page != null ? page.isLast() : null);
			listedCryptoResponsePage.setPageIndex(page != null ? page.getNumber() : null);
			listedCryptoResponsePage.setPageSize(page != null ? page.getSize() : null);
			listedCryptoResponsePage
					.setTotalElement(page != null ? page.getTotalElements() : cryptoResponseList.size());
			listedCryptoResponsePage.setTotalPages(page != null ? page.getTotalPages() : null);
			listedCryptoResponsePage.setCryptoResponseList(cryptoResponseList);
			response.put(Constant.RESPONSE_CODE, Constant.OK);
			response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
			response.put(Constant.DATA, listedCryptoResponsePage);
			log.info("data found successfully ! status - {}", Constant.OK);
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getTradingPair(String cryptoId, String exchangeName) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> pairList = new ArrayList<String>();
		TsTradingPairResponse pairResponse = new TsTradingPairResponse();
		int limit = 1500, offset = 0;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (cryptoId != null && !cryptoId.isBlank()) {
				String tradingPairResponse = thirdPartyApiUtil.getTradingPair(cryptoId, limit, offset);
				if (tradingPairResponse != null && !tradingPairResponse.isBlank()) {
					log.info(" third party response tradingPair list ! status - {}", Constant.OK);
					Map<?, ?> mapResponse = mapper.readValue(tradingPairResponse, Map.class);
					pairResponse = mapper.convertValue(mapResponse, TsTradingPairResponse.class);
					if (pairResponse.getStatus().getCode() == 0) {
						log.info(" Trading Pair data found successfully ! status - {}" + Constant.OK);
						for (TsTradingMarket tsTradingMarket : pairResponse.getData().getMarkets()) {
							if (tsTradingMarket.getExchange_name().equals(exchangeName)) {
								String base_quote = tsTradingMarket.getBase().toUpperCase() + "/"
										+ tsTradingMarket.getQuote().toUpperCase();
								pairList.add(base_quote);
							}
						}
						// Create a Set to store unique values
						Set<String> uniqueSet = new HashSet<>();
						// Iterate through the list and add unique values to the set
						for (String item : pairList) {
							uniqueSet.add(item);
						}
						// Clear the original list
						pairList.clear();
						// Reconstruct the list with unique values
						pairList.addAll(uniqueSet);
						// Sort the ArrayList in ascending order
						Collections.sort(pairList);
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_FOUND);
						response.put(Constant.DATA, pairList);
						log.info(pairList.size() + " Trading pair data found successfully !" + Constant.OK);
					} else {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, pairList);
						log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
					}
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					response.put(Constant.DATA, pairList);
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
				log.info(" All crypto list trading pair data save successfully ! status - {}" + Constant.OK);
			} else {
				log.info("crypto list not found in your database ! status - {}", Constant.NOT_FOUND);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getCryptoExchangeList() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		try {
			List<CryptoExchange> exchangeList = cryptoExchangeRepository.findAll();
			for (CryptoExchange cryptoExchange : exchangeList) {
				list.add(cryptoExchange.getExchangeName());
			}
			// Sort the ArrayList in ascending order
			Collections.sort(list);
			if (exchangeList != null && !exchangeList.isEmpty()) {
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.DATA_FOUND);
				response.put(Constant.DATA, list);
				log.info(exchangeList.size() + " Exchange list data found successfully !" + Constant.OK);
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
				response.put(Constant.DATA, exchangeList);
				log.info("Exchange list data not found ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	@Override
	public List<Crypto> getAllCryptoDetails(@Valid PaginationPayLoad paginationPayLoad) {
		List<Crypto> cryptoList = new ArrayList<Crypto>();
		try {
			if (paginationPayLoad.getKeyWord() != null && !paginationPayLoad.getKeyWord().isBlank()) {
				cryptoList = cryptoRepository.findAllCryptosWithSearching(paginationPayLoad.getKeyWord());
			} else {
				cryptoList = cryptoRepository.findAllCryptos();
			}
		} catch (Exception e) {
			e.getMessage();
			log.error("Exception! status - {}", e.getMessage());
		}
		return cryptoList;
	}

	@Override
	public List<Crypto> getAllNewlyListedCryptoDetails(@Valid PaginationPayLoad paginationPayLoad) {
		List<Crypto> newlyListedcryptoList = new ArrayList<Crypto>();
		try {
			LocalDate currentDate = LocalDate.now();
			// Calculate one year ago from the current date
			LocalDate oneYearAgoDate = currentDate.minus(Period.ofYears(1));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// Format the one year ago date as a string
			String formattedOneYearAgoDate = oneYearAgoDate.format(formatter);
			if (paginationPayLoad.getKeyWord() != null && !paginationPayLoad.getKeyWord().isBlank()) {
				newlyListedcryptoList = cryptoRepository.findAllNewlyListedCryptosWithSearching(
						paginationPayLoad.getKeyWord(), formattedOneYearAgoDate);
			} else {
				newlyListedcryptoList = cryptoRepository.findAllNewlyListedCryptos(formattedOneYearAgoDate);
			}
		} catch (Exception e) {
			e.getMessage();
			log.error("Exception! status - {}", e.getMessage());
		}
		return newlyListedcryptoList;
	}

	@Override
	public Map<String, Object> getCryptoGraphData(String cryptoId, String type) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			/* using objectMapper */
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String cryptoGraphdata = thirdPartyApiUtil.getCryptoGraphData(cryptoId, type);
			if (cryptoGraphdata != null && !cryptoGraphdata.isBlank()) {
				Map<?, ?> mapResponse2 = mapper.readValue(cryptoGraphdata, Map.class);
				Integer code2 = (Integer) mapResponse2.get("code");
				if (code2 == null) {
					CryptoGraphResponse cryptoGraphResponse1 = new CryptoGraphResponse();
					CryptoGraphResponse cryptoGraphResponse = mapper.convertValue(mapResponse2,
							CryptoGraphResponse.class);
					log.info("crypto graph data found ! status - {} ", Constant.OK + " " + cryptoGraphdata);
					if (type.equalsIgnoreCase(Constant.FIVE_YEAR)) {
						List<CryptoMarketChart> marketChartList = new ArrayList<CryptoMarketChart>();
						long previousFiveYearDay = DateUtil.previousFiveYearDay();
						CryptoGraphData cryptoGraphData2 = new CryptoGraphData();
						cryptoGraphData2.setId(cryptoGraphResponse.getData().getId());
						cryptoGraphData2.setName(cryptoGraphResponse.getData().getName());
						cryptoGraphData2.setSymbol(cryptoGraphResponse.getData().getSymbol());
						cryptoGraphData2.setVs_currency(cryptoGraphResponse.getData().getVs_currency());
						for (int i = 0; i <= previousFiveYearDay - 1; i++) {
							marketChartList.add(cryptoGraphResponse.getData().getMarket_chart().get(i));
						}
						log.info("market graph size :- " + marketChartList.size());
						cryptoGraphData2.setMarket_chart(marketChartList);
						cryptoGraphResponse1.setData(cryptoGraphData2);
					} else {
						BeanUtils.copyProperties(cryptoGraphResponse, cryptoGraphResponse1);
					}
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.CRYPTO_GRAPH_DATA_FOUND_SUCESSFULLY);
					response.put(Constant.DATA, cryptoGraphResponse1);
					log.info("Crypto graph data found successfully !! status - {} " + Constant.OK);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
					response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				response.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception! status - {}", e.getMessage());
		}
		return response;
	}
}
