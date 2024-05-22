package com.advantal.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.model.Broker;
import com.advantal.model.BtcDominance;
import com.advantal.model.Crypto;
import com.advantal.model.CryptoExchange;
import com.advantal.repository.BrokerRepository;
import com.advantal.repository.BtcDominanceRepository;
import com.advantal.repository.CryptoExchangeRepository;
import com.advantal.repository.CryptoRepository;
import com.advantal.responsepayload.NewlyListedItemsResponse;
import com.advantal.responsepayload.TsCryptoExchangeListResponse;
import com.advantal.responsepayload.TsCryptoGlobalStatusResponse;
import com.advantal.responsepayload.TsCryptoResponse;
import com.advantal.responsepayload.TsExchnageItems;
import com.advantal.responsepayload.TsItem;
import com.advantal.responsepayload.TsItemsNewlyListed;
import com.advantal.utils.Constant;
import com.advantal.utils.MethodUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CryptoDataProcessUtil {

	@Autowired
	ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	CryptoRepository cryptoRepository;

	@Autowired
	CryptoExchangeRepository cryptoExchangeRepository;

	@Autowired
	MethodUtil methodUtil;

	@Autowired
	BtcDominanceRepository btcDominanceRepository;

	@Autowired
	private BrokerRepository brokerRepository;

	public void saveCryptoList() {
		try {
			TsCryptoResponse CryptoListResponse = new TsCryptoResponse();
//			List<Crypto> cryptoList = new ArrayList<>();
			Integer limit = 1500, numberOfCryptoSize = 1, offset = 0, oldcount = 0, newcount = 0, totalcount = 0;
			String responsestr = null;
			/* store the multiple cryptos in to the databases for manage the for loop */
			while (offset < numberOfCryptoSize) {
				responsestr = thirdPartyApiUtil.getCryptoList(limit, offset);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				if (!responsestr.isBlank() && responsestr != null) {
					log.info(" third party response cryptoList ! status - {}", Constant.OK);
					Map<?, ?> mapResponse = mapper.readValue(responsestr, Map.class);
					CryptoListResponse = mapper.convertValue(mapResponse, TsCryptoResponse.class);
					log.info(" third party cryptoList response size:-"
							+ CryptoListResponse.getData().getPage_info().getTotal_results());
					numberOfCryptoSize = CryptoListResponse.getData().getPage_info().getTotal_results();
					offset = offset + CryptoListResponse.getData().getItems().size();
					if (CryptoListResponse.getData() != null) {
						for (TsItem item : CryptoListResponse.getData().getItems()) {
							Crypto crypto = new Crypto();
							/* check the crypto are available or not into the databases */
							crypto = cryptoRepository.findByCryptoId(item.getId());
							if (crypto != null) {
								oldcount++;
								log.info(oldcount + " old crypto updated in our local database successfully!! status {}"
										+ Constant.OK);
								BeanUtils.copyProperties(item, crypto);
								crypto.setCryptoId(item.getId());
								crypto.setStatus(Constant.ONE);
								crypto.setUpdationDate(new Date());
//								cryptoList.add(crypto);
								/* total crypto are saved */
								totalcount++;
								cryptoRepository.save(crypto);
								log.info("save the all cryptoList ! status - {} " + Constant.OK + " count = " + oldcount
										+ " old cryptos updated successfully !!");
							} else {
								newcount++;
								log.info(newcount + " new crypto save in our local database successfully!! status {}"
										+ Constant.OK);
								Crypto newCrypto = new Crypto();
								BeanUtils.copyProperties(item, newCrypto);
								newCrypto.setCurrency("usd");
								newCrypto.setFavorite(false);
								newCrypto.setInstrumentType("crypto");
								newCrypto.setListing_time("0");
								newCrypto.setCryptoId(item.getId());
								newCrypto.setStatus(Constant.ONE);
								newCrypto.setUpdationDate(new Date());
//								cryptoList.add(newCrypto);
								/* total crypto are saved */
								totalcount++;
								cryptoRepository.save(newCrypto);
								log.info("save the all cryptoList ! status - {} " + Constant.OK + " count = " + newcount
										+ " new cryptos updated successfully !! - {}");
							}
						}
						log.info(" total = " + totalcount + " cryptos saved successfully !!");
					} else {
						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			}
			log.info("All crypto data updated successully ! status - {} " + Constant.OK);
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	/* save newly crypto data in our local server */
	public void saveNewlyCrypto() {
		try {
			Integer limit = 1500, numberOfCryptoSize = 1, offset = 0, oldcount = 0, newcount = 0, totalcount = 0;
			NewlyListedItemsResponse newlyListedDataResponse = new NewlyListedItemsResponse();
			Crypto oldcrypto = new Crypto();
//			List<Crypto> cryptoList = new ArrayList<Crypto>();
			while (offset < numberOfCryptoSize) {
				log.info("offset size :-" + offset + " and numberOfCryptoSize :-" + numberOfCryptoSize);
				String responseStr = thirdPartyApiUtil.getNewlyListed(limit, offset);
				log.info("third party response" + responseStr);
				if (!responseStr.isBlank() && responseStr != null) {
					log.info("third party data found successfully! status - {}", Constant.OK);
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					Map<?, ?> mapResponse = mapper.readValue(responseStr, Map.class);
					newlyListedDataResponse = mapper.convertValue(mapResponse, NewlyListedItemsResponse.class);
					if (!newlyListedDataResponse.getData().getItems().isEmpty()
							&& newlyListedDataResponse.getData().getItems() != null) {
						numberOfCryptoSize = newlyListedDataResponse.getData().getPage_info().getTotal_results();
						offset = offset + newlyListedDataResponse.getData().getItems().size();
						log.info(" third party cryptoList response size:- "
								+ newlyListedDataResponse.getData().getPage_info().getTotal_results()
								+ " offset size :- " + offset);
						/* get the data into the local server based on the cryptoId */
						for (TsItemsNewlyListed itemsResponse : newlyListedDataResponse.getData().getItems()) {
							oldcrypto = cryptoRepository.findByCryptoId(itemsResponse.getId());
							if (oldcrypto != null) {
								oldcount++;
								totalcount++;
								log.info("old crypto found successfully ! status {}" + Constant.OK);
								BeanUtils.copyProperties(itemsResponse, oldcrypto);
								String listin_date = itemsResponse.getListing_time();
								String date = listin_date.substring(0, 10);
								String time = listin_date.substring(11, 19);
								oldcrypto.setCryptoId(itemsResponse.getId());
								oldcrypto.setListing_time(date + " " + time);
								oldcrypto.setUpdationDate(new Date());
//								cryptoList.add(oldcrypto);
								cryptoRepository.save(oldcrypto);
								log.info(oldcount + " old newly listed crypto updated successfully ! status {}"
										+ Constant.OK);
							} else {
//								log.info("old crypto is not found ! status {}" + Constant.OK);
								newcount++;
								totalcount++;
								Crypto newcrypto = new Crypto();
								BeanUtils.copyProperties(itemsResponse, newcrypto);
								String listin_date = itemsResponse.getListing_time();
								String date = listin_date.substring(0, 10);
								String time = listin_date.substring(11, 19);
								newcrypto.setCryptoId(itemsResponse.getId());
								newcrypto.setListing_time(date + " " + time);
								newcrypto.setCurrency("usd");
								newcrypto.setFavorite(false);
								newcrypto.setInstrumentType("crypto");
								newcrypto.setStatus(Constant.ONE);
								newcrypto.setUpdationDate(new Date());
//								cryptoList.add(newcrypto);
								cryptoRepository.save(newcrypto);
								log.info(newcount + " new newly listed crypto save successfully " + Constant.OK);
							}
						}
//						cryptoRepository.saveAll(cryptoList);
//						log.info("saved all newly listed crypto successfully ! status {}" + Constant.OK);
					} else {
						log.info("Newly listed crypto not found ! status - {}", Constant.OK);
					}
				} else {
					log.info("Newly listed crypto not found ! status - {}", Constant.OK);
				}
			}
			log.info(totalcount + " newly listed crypto update successfully ! status - {}", Constant.OK);
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	/* new implement 11/07/2023 */
	public void saveCryptoExchangeList() {
		int limit = 1500, offset = 0;
		List<Broker> brokerList = new ArrayList<>();
		TsCryptoExchangeListResponse cryptoExchangeListResponse;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String exchangeResponse = thirdPartyApiUtil.getCryptoExchangeList(limit, offset);
			if (exchangeResponse != null && !exchangeResponse.isBlank()) {
				log.info(" third party response cryptoExchangeList ! status - {}", Constant.OK);
				Map<?, ?> mapResponse = mapper.readValue(exchangeResponse, Map.class);
				cryptoExchangeListResponse = mapper.convertValue(mapResponse, TsCryptoExchangeListResponse.class);
				if (cryptoExchangeListResponse.getStatus().getCode() == 0) {
					Long count = brokerRepository.countCryptoExchange();
					if (cryptoExchangeListResponse.getData().getItems().size() >= count) {
						Integer oldCount = 0, newCount = 0;
						for (TsExchnageItems exchnageItems : cryptoExchangeListResponse.getData().getItems()) {
							Broker broker = brokerRepository.findByBroker(exchnageItems.getExchange_name());
							if (broker != null) {
								broker.setBroker(exchnageItems.getExchange_name());
								broker.setUpdationDate(new Date());
								brokerRepository.save(broker);
								oldCount++;
								log.info(oldCount + ": crypto exchange updated successfully! | Exchange is - "
										+ broker.getBroker() + " | status - {}", Constant.OK);
							} else {
								Broker newBroker = new Broker();
								newBroker.setBroker(exchnageItems.getExchange_name());
								newBroker.setType("crypto");
								newBroker.setCreationDate(new Date());
								newBroker.setUpdationDate(new Date());
								newBroker.setStatus(Constant.ONE);
								brokerRepository.save(newBroker);
								newCount++;
								log.info(newCount + ": New crypto exchange saved successfully! | Exchange is - "
										+ newBroker.getBroker() + " | status - {}", Constant.OK);
							}
						}
					} else if (count == 0) {
						for (TsExchnageItems exchnageItems : cryptoExchangeListResponse.getData().getItems()) {
							Broker broker = new Broker();
							broker.setBroker(exchnageItems.getExchange_name());
							broker.setType("crypto");
							broker.setCreationDate(new Date());
							broker.setUpdationDate(new Date());
							broker.setStatus(Constant.ONE);
							brokerList.add(broker);
						}
						brokerRepository.saveAll(brokerList);
						log.info(brokerList.size() + ": New crypto exchange saved successfully! | status - {}",
								Constant.OK);
					}
				} else {
					log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.THIRD_PARTY_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
//		return response;
	}

	public void saveBtcDominance() {
		/* using objectMapper */
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String cryptoGlobalMarketResponse = thirdPartyApiUtil.getCryptoGlobalStatus();
			if (!cryptoGlobalMarketResponse.isBlank()) {
				Map<?, ?> mapResponse2 = mapper.readValue(cryptoGlobalMarketResponse, Map.class);
				Integer code2 = (Integer) mapResponse2.get("code");
				if (code2 == null) {
					log.info("data found ! status - {}", cryptoGlobalMarketResponse);
					TsCryptoGlobalStatusResponse statusResponse = mapper.convertValue(mapResponse2,
							TsCryptoGlobalStatusResponse.class);
					String dom = statusResponse.getData().getMarket_dominances().getBtc();
					Double stringToDouble = Double.parseDouble(dom);
					Double dominance = stringToDouble * 100;
					/* save the dominance percentage value */
					BtcDominance btcDominance = new BtcDominance();
					btcDominance.setBtcDominance(dominance);
//					Date currentDate =new Date();
////					System.out.println("time :- " + currentDate.toString());
//					// Create a Calendar instance and set the current date and time
//			        Calendar calendar = Calendar.getInstance();
//			        calendar.setTime(currentDate);
//			        // Subtract 24 hours from the current date
//			        calendar.add(Calendar.HOUR_OF_DAY, -24);
//			        // Get the date and time 24 hours in the past
//			        Date previousDate = calendar.getTime();
//			        System.out.println("Current Date and Time: " + currentDate);
//			        System.out.println("Date and Time 24 hours in the past: " + previousDate);
//			        BtcDominance btcDominance2=btcDominanceRepository.getUpdationDate(previousDate);
//			        if(btcDominance2!=null) {
//			        	log.info(" Data already updated !! status - {} "+Constant.OK);
//			        }else {
//			        	btcDominance.setUpdationDate(currentDate);
//						btcDominanceRepository.save(btcDominance);
//						log.info(btcDominance + " btc dominance percentage value save successfully !! status - {} "
//								+ Constant.OK);
//					}

					LocalDate currentDate = LocalDate.now();

					System.out.println("time :- " + currentDate.toString());

					btcDominance.setUpdationDate(currentDate.toString());

					btcDominanceRepository.save(btcDominance);

					log.info(btcDominance + " btc dominance percentage value save successfully !! status - {} "

							+ Constant.OK);
//					btcDominance.setUpdationDate(currentDate);
//					btcDominanceRepository.save(btcDominance);
//					log.info(btcDominance + " btc dominance percentage value save successfully !! status - {} "
//							+ Constant.OK);
				}
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}
}
