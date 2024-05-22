package com.advantal.utils;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.advantal.model.Indices;
import com.advantal.model.Stock;
import com.advantal.requestpayload.StockMoversRequest;
import com.advantal.requestpayload.TimeSeriesRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ThirdPartyApiUtil {

	@Autowired
	private WebClient webClient;

	String responseStr = "";

//	public String stockList() {
//		try {
//			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.STOCK_LIST_ENDPOINT
//					+ "source=docs &country=united states" + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
//					.exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//		return responseStr;
//	}

	public String stockList(String exchangeName) {
		try {
			if (!exchangeName.equalsIgnoreCase("Tadawul")) {
				responseStr = webClient.get().uri(
						Constant.BASE_URL + Constant.STOCK_LIST_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
//				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.STOCK_LIST_SAUDI_ENDPOINT
//						+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
//							return response.bodyToMono(String.class);
//						}).block();
				
				responseStr = webClient.get().uri(Constant.FMPP_BASE_URL_V4 + Constant.STOCK_LIST_SAUDI_ENDPOINT
						+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getStockListExchangeWise(String exchange) {
		try {
			responseStr = webClient.get().uri(
					Constant.BASE_URL + Constant.STOCK_LIST_ENDPOINT + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//	public String indicesList() {
//		try {
//			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.INDICES_LIST_ENDPOINT + "source="
//					+ Constant.SOURCE + "&country=saudi arabia").exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//		return responseStr;
//	}

	public String indicesList() {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.INDICES_LIST_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getStock(String symbol, String country, String exchange) {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + "symbol=" + symbol + "&" + Constant.API_KEY
							+ Constant.API_KEY_VALUE + "&country=" + country + "&exchange=" + exchange)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getStock(String symbol, String country) {
		try {
			if (!country.equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + symbol + "?"
						+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + symbol + ".SR" + "?"
						+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getStock(String symbol) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + "symbol=" + symbol + "?"
					+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// https://financialmodelingprep.com/api/v4/company-outlook?symbol=AAPL&apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getStockStatistics(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient
						.get().uri(Constant.FMPP_BASE_URL_V4 + Constant.STATISTICS_ENDPOINT + "symbol="
								+ serverStock.getSymbol() + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient
						.get().uri(Constant.FMPP_BASE_URL_V4 + Constant.STATISTICS_ENDPOINT + "symbol="
								+ serverStock.getSymbol() + ".SR" + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getLogo(String symbol, String country) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.LOGO_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE + "&country=" + country).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getLogo(String symbol) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.LOGO_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getCryptoLogo(String symbol) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.LOGO_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getProfile(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.PROFILE_ENDPOINT
						+ serverStock.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.PROFILE_ENDPOINT
						+ serverStock.getSymbol() + ".SR" + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	//https://financialmodelingprep.com/api/v4/private/saudi-details/full?apikey=67e8091a7e1419da6d82f2d1869ef63c
	public String getSaudiProfile() {
		try {
				responseStr = webClient.get().uri(Constant.FMPP_BASE_URL_V4 + Constant.SAUDI_PROFILE_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}
	
	
	public String getExchange(String symbol, String exchange) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE + "&exchange=" + exchange).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getIndices(String symbol, String country) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.QUOTE_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE + "&country=" + country).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getMovers(StockMoversRequest stockMoversRequest) {
		try {
//			responseStr = webClient.get()
//					.uri(Constant.BASE_URL + Constant.MARKET_MOVERS_ENDPOINT + "country="
//							+ stockMoversRequest.getCountry() + "&" + Constant.API_KEY + Constant.API_KEY_VALUE
//							+ "&direction=" + stockMoversRequest.getDirection() + "&outputsize="
//							+ stockMoversRequest.getOutputsize())
//					.exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getTimeSeries(TimeSeriesRequest timeSeriesRequest, String interval) {
		try {
			if (!timeSeriesRequest.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/"
								+ timeSeriesRequest.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/"
								+ timeSeriesRequest.getSymbol() + ".SR" + "?" + Constant.API_KEY
								+ Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	/* ---------- get stock chart data ----------- */
	public String getTimeSeries(String symbol,String country, String interval) {
		try {
			if (!country.equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/"
								+ symbol + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/"
								+ symbol + ".SR" + "?" + Constant.API_KEY
								+ Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}
	public String getIndicesTimeSeries(TimeSeriesRequest timeSeriesRequest, String interval) {
		try {
			if (!timeSeriesRequest.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/" + "^"
								+ timeSeriesRequest.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/" + "^"
								+ timeSeriesRequest.getSymbol() + ".SR" + "?" + Constant.API_KEY
								+ Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}
	
	public String getIndicesTimeSeriesData(Indices indices, String interval) {
		try {
			if (!indices.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/" + "^"
								+ indices.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + interval + "/" + "^"
								+ indices.getSymbol() + ".SR" + "?" + Constant.API_KEY
								+ Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getEOD(String symbol, String country) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.EOD_ENDPOINT + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE + "&country=" + country).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//	public String cryptoList() {
//		try {
//			responseStr = webClient.get()
//					.uri(Constant.BASE_URL + Constant.CRYPTO_LIST_ENDPOINT + "source=" + Constant.SOURCE)
//					.exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
//		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
//		}
//		return responseStr;
//	}
//
//	// limit max=1500,
//	public String cryptoList(Integer limit, Integer offset) {
//		try {
//			responseStr = webClient.get()
//					.uri(Constant.CRYPTO_BASE_URL + Constant.TS_CRYPTO_LIST_ENDPOINT + "list?" + Constant.TS_LIMIT
//							+ limit + "&" + Constant.TS_OFFSET + offset)
//					.header(Constant.TOKEN_INSIGHT_API_KEY, Constant.TOKEN_INSIGHT_API_KEY_VALUE).retrieve()
//					.bodyToMono(String.class).block();
//		} catch (Exception e) {
//
//			log.error("Exception : " + e.getMessage());
//		}
//		return responseStr;
//	}

	// https://api.tokeninsight.com/api/v1/coins/list?limit=1500&offset=1500&vs_currency=usd
	public String getCryptoList(Integer limit, Integer offset) {
		try {
			responseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_SAVE_LIST_ENDPOINT + "list?" + Constant.TS_LIMIT
							+ limit + "&" + Constant.TS_OFFSET + offset + "&vs_currency=usd")
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;

	}

//url https://api.tokeninsight.com/api/v1/rating/coin/bitcoin
	public String getAnalystRating(String cryptoId) {
		String ratingResponseStr = "";
		try {
			ratingResponseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_RATING_ENDPOINT + cryptoId)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
//			System.out.println("Exception :" + e.getMessage());
		}
		return ratingResponseStr;

	}

	public String getCryptoDetails(String cryptoId) {
		String cryptoResponseStr = "";
		try {
			cryptoResponseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_DETAIL_ENDPOINT + cryptoId)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
//			System.out.println("Exception :" + e.getMessage());
		}
		return cryptoResponseStr;

	}

	// https://api.tokeninsight.com/api/v1/simple/price?ids=bitcoin%2Cethereum&include_vol_spot_24h=false&include_price_change_percentage_24h=true
	public String getCryptoList(String cryptoId) {
		try {
			responseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_LIST_ENDPOINT + Constant.TS_IDS + cryptoId
							+ Constant.TS_PRICE_CHANGE_PERCENTAGE)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
//			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;

	}

//	public String getStock(String symbol) {
//		try {
//			responseStr = webClient.get().uri(Constant.TD_BASE_URL + Constant.TD_QUOTE_ENDPOINT + "symbol=" + symbol
//					+ "&" + Constant.TD_API_KEY + Constant.TD_API_KEY_VALUE).exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
//		} catch (Exception e) {
//			System.out.println("Exception :" + e.getMessage());
//		}
//		return responseStr;
//	}
	// https://api.tokeninsight.com/api/v1/rating/coin/bitcoin
	public String getNews(String id) {
		try {
			responseStr = webClient.get().uri(Constant.TS_BASE_URL + Constant.TS_NEWS_ENDPOINT + id)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getSymbolSearch(String symbol, String outputsize) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.SYMBOL_SEARCH + "symbol=" + symbol + "&"
					+ Constant.API_KEY + Constant.API_KEY_VALUE + "&outputsize=" + outputsize)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	/* global market status */
	// https://api.tokeninsight.com/api/v1/global?vs_currency=usd
	public String getCryptoGlobalStatus() {
		try {
			responseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GLOBAL_STATUS + "vs_currency=usd")
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
//			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;

	}

	/* get the third partys data in news */
	// https://api.marketaux.com/v1/news/all?filter_entities=true&country=null&group_similar=false&language=en&api_token=8dpS4ZjxH2cEJerG91YChGz37GrxEk7IolK6hzfJ
	/*
	 * USA local time Instant instant = Instant.now(); Locale locale = new
	 * Locale("en", "US"); ZoneId z = ZoneId.of("America/New_York"); ZonedDateTime
	 * zdt = instant.atZone(z); DateTimeFormatter f =
	 * DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withLocale(locale); String
	 * output = zdt.format(f);
	 */
	public String getAllNews(String last_time, String country, Integer limit, Integer page) {
		String responseStr = "";
		try {
			if (!country.isEmpty()) {
				/* KSA global news and USA global news */
				/* get all global news */
				if (!last_time.isEmpty()) {
					/* check 1H/1D/1W/1Y */
					if (last_time.matches("1H")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.CurrentToOneHourDiff();
						responseStr = ksaAndUsaNewsThirdParty(localDateTime, differenceTime, country, limit, page);
					} else if (last_time.matches("1D")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.CurrentToOneDayDiff();
						responseStr = ksaAndUsaNewsThirdParty(localDateTime, differenceTime, country, limit, page);
					} else if (last_time.matches("1W")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneWeek();
						responseStr = ksaAndUsaNewsThirdParty(localDateTime, differenceTime, country, limit, page);
					} else if (last_time.matches("1M")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneMonth();
						responseStr = ksaAndUsaNewsThirdParty(localDateTime, differenceTime, country, limit, page);
					} else if (last_time.matches("1Y")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneYear();
						responseStr = ksaAndUsaNewsThirdParty(localDateTime, differenceTime, country, limit, page);
					}
				} else {
					// getAll news provide in investing.com &domains=investing.com
					String localDateTime = DateUtil.CurrentLocalDateTime();
					if (country.equals("us")) {
						responseStr = webClient.get()
								.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "countries=" + country
										+ "&must_have_entities=true" + "&filter_entities=true" + "&group_similar=false"
										+ "&limit=" + limit + "&language=en" + "&domains=investing.com" + "&page="
										+ page + Constant.PUBLISHED_BEFORE + localDateTime + Constant.NEWS_API_TOKEN_KEY
										+ "=" + Constant.NEWS_API_TOKEN_VALUE)
								.exchangeToMono(response -> {
									return response.bodyToMono(String.class);
								}).block();
					} else if (country.equals("sa")) {
						// getAll news provide in argaam.com &domains=argaam.com
						responseStr = webClient.get()
								.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "countries=" + country
										+ "&must_have_entities=true" + "&filter_entities=true" + "&group_similar=false"
										+ "&limit=" + limit + "&language=en" + "&domains=argaam.com" + "&page=" + page
										+ Constant.PUBLISHED_BEFORE + localDateTime + Constant.NEWS_API_TOKEN_KEY + "="
										+ Constant.NEWS_API_TOKEN_VALUE)
								.exchangeToMono(response -> {
									return response.bodyToMono(String.class);
								}).block();
					}
				}
			} else {
				/* get all global news */
				if (!last_time.isEmpty()) {
					/* check 1H/1D/1W/1Y */
					if (last_time.matches("1H")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.CurrentToOneHourDiff();
						responseStr = newsThirdParty(localDateTime, differenceTime, limit, page);
					} else if (last_time.matches("1D")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.CurrentToOneDayDiff();
						responseStr = newsThirdParty(localDateTime, differenceTime, limit, page);
					} else if (last_time.matches("1W")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneWeek();
						responseStr = newsThirdParty(localDateTime, differenceTime, limit, page);
					} else if (last_time.matches("1M")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneMonth();
						responseStr = newsThirdParty(localDateTime, differenceTime, limit, page);
					} else if (last_time.matches("1Y")) {
						String localDateTime = DateUtil.CurrentLocalDateTime();
						String differenceTime = DateUtil.currentToOneYear();
						responseStr = newsThirdParty(localDateTime, differenceTime, limit, page);
					}
				} else {
					// https://api.marketaux.com/v1/news/all?&filter_entities=true&group_similar=false&limit=50&language=en&page=1&domains=investing.com
					// getAll news news provide in investing.com &domains=investing.com
					String localDateTime = DateUtil.CurrentLocalDateTime();
					responseStr = webClient.get()
							.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "must_have_entities=true"
									+ "&filter_entities=true" + "&group_similar=false" + "&limit=" + limit
									+ "&language=en" + "&domains=investing.com" + "&page=" + page
									+ Constant.PUBLISHED_BEFORE + localDateTime + Constant.NEWS_API_TOKEN_KEY + "="
									+ Constant.NEWS_API_TOKEN_VALUE)
							.exchangeToMono(response -> {
								return response.bodyToMono(String.class);
							}).block();
				}
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
//			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;
	}

//https://api.marketaux.com/v1/news/all?must_have_entities=true&filter_entities=true&group_similar=false&limit=50&&language=en&page=1
	/* get all global news */
	public String newsThirdParty(String localDateTime, String differenceTime, Integer limit, Integer page) {
		try {

			responseStr = webClient.get()
					.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "must_have_entities=true"
							+ "&filter_entities=true" + "&group_similar=false" + "&limit=" + limit + "&language=en"
							+ "&page=" + page + Constant.PUBLISHED_BEFORE + localDateTime + Constant.PUBLISHED_AFTER
							+ differenceTime + Constant.NEWS_API_TOKEN_KEY + "=" + Constant.NEWS_API_TOKEN_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();

		} catch (Exception e) {
			log.error("Excepton:-" + e.getMessage());
		}
		return responseStr;
	}

	// https://api.marketaux.com/v1/news/all?countries=sa&filter_entities=true&group_similar=false&api_token=8dpS4ZjxH2cEJerG91YChGz37GrxEk7IolK6hzfJ
	/* get all KSA news */
	public String ksaAndUsaNewsThirdParty(String localDateTime, String differenceTime, String country, Integer limit,
			Integer page) {
		try {
			responseStr = webClient.get()
					.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "countries=" + country
							+ "&must_have_entities=true" + "&filter_entities=true" + "&group_similar=false" + "&limit="
							+ limit + "&language=en" + "&page=" + page + Constant.PUBLISHED_BEFORE + localDateTime
							+ Constant.PUBLISHED_AFTER + differenceTime + Constant.NEWS_API_TOKEN_KEY + "="
							+ Constant.NEWS_API_TOKEN_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();

		} catch (Exception e) {
			log.error("Excepton:-" + e.getMessage());
		}
		return responseStr;
	}

	// https://api.tokeninsight.com/api/v1/news/list?lang=en'
	// https://api.marketaux.com/v1/news/all?exchanges=CC&filter_entities=true&published_after=2023-07-26T11:50&api_token=8dpS4ZjxH2cEJerG91YChGz37GrxEk7IolK6hzfJ
	public String getAllCryptoNews(Integer limit, Integer page) {
		try {
			/* tokeninside */
			/*
			 * responseStr = webClient.get() .uri(Constant.TS_BASE_URL +
			 * Constant.TS_GLOBAL_CRYPTO_NEWS_END + "lang=" + "en")
			 * .header(Constant.TS_API_KEY,
			 * Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
			 */
			/* MarketAux */
			// getAll news news provide in investing.com &domains=investing.com
			String localDateTime = DateUtil.CurrentLocalDateTime();
			responseStr = webClient.get()
					.uri(Constant.NEWS_BASE_URL + Constant.NEWS_END_POINT + "exchanges=CC" + "&must_have_entities=true"
							+ "&filter_entities=true" + "&group_similar=false" + "&limit=" + limit + "&language=en"
							+ "&domains=investing.com" + "&page=" + page + Constant.PUBLISHED_BEFORE + localDateTime
							+ Constant.NEWS_API_TOKEN_KEY + "=" + Constant.NEWS_API_TOKEN_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		return responseStr;
	}
	
	//https://api.browse.ai/v2/robots/310bb973-9cb4-40e9-80c2-0a2ffede088b/tasks?page=1&status=successful
	public String getIndices(int page) {
		String response = "";
		try {
			// https://api.browse.ai/v2/robots/3f082241-880f-4717-bf03-1fcf89473785/tasks?page=1&status=successful
			response = webClient.get()
					.uri("https://api.browse.ai/v2/robots/310bb973-9cb4-40e9-80c2-0a2ffede088b/tasks?page=" + page
							+ "&status=successful")
					.header("Authorization",
							"Bearer 7caa22a4-748f-4aaf-8110-f853f4a49c0a:f1725e25-8b5b-4823-aae8-91f68f2d91bc")
					.retrieve().bodyToMono(String.class).block();

		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		System.out.println(response);
		return response;
	}

	

	// browse AI
	// https://api.browse.ai/v2/robots/bfe25d98-a02f-4a07-adb1-c18533495289/tasks
	public String getAllSaudiNews(String country, int page) {
		String response = "";
		try {
			// https://api.browse.ai/v2/robots/3f082241-880f-4717-bf03-1fcf89473785/tasks?page=1&status=successful
			response = webClient.get()
					.uri("https://api.browse.ai/v2/robots/3f082241-880f-4717-bf03-1fcf89473785/tasks?page=" + page
							+ "&status=successful")
					.header("Authorization",
							"Bearer 7caa22a4-748f-4aaf-8110-f853f4a49c0a:f1725e25-8b5b-4823-aae8-91f68f2d91bc")
					.retrieve().bodyToMono(String.class).block();

		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		System.out.println(response);
		return response;
	}

	public String getAllUsaNews(String country, int page) {
		String response = "";
		try {
			response = webClient.get()
					.uri("https://api.browse.ai/v2/robots/4464808a-0246-4336-934d-6640075370f1/tasks?page=" + page
							+ "&status=successful")
					.header("Authorization",
							"Bearer 7caa22a4-748f-4aaf-8110-f853f4a49c0a:f1725e25-8b5b-4823-aae8-91f68f2d91bc")
					.retrieve().bodyToMono(String.class).block();

		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		System.out.println(response);
		return response;
	}

	// announcement news
	// https://api.browse.ai/v2/robots/4810f439-812e-4eb5-b507-fdc4af86223c/tasks?page=1&status=successful
	public String getAllAnnouncementNews(Integer page) {
		String response = "";
		try {
			response = webClient.get()
					.uri("https://api.browse.ai/v2/robots/4810f439-812e-4eb5-b507-fdc4af86223c/tasks?page=" + page
							+ "&status=successful")
					.header("Authorization",
							"Bearer 7caa22a4-748f-4aaf-8110-f853f4a49c0a:f1725e25-8b5b-4823-aae8-91f68f2d91bc")
					.retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		return response;

	}

	// https://api.browse.ai/v2/robots/09b82063-4290-4370-8c69-5c3086ae3445/tasks?page=1&status=successful
	public String getCryptoNews(int page) {
		String response = "";
		try {
			response = webClient.get()
					.uri("https://api.browse.ai/v2/robots/09b82063-4290-4370-8c69-5c3086ae3445/tasks?page=" + page
							+ "&status=successful")
					.header("Authorization",
							"Bearer 7caa22a4-748f-4aaf-8110-f853f4a49c0a:f1725e25-8b5b-4823-aae8-91f68f2d91bc")
					.retrieve().bodyToMono(String.class).block();

		} catch (Exception e) {
			log.error("Exception:-" + e.getMessage());
		}
		System.out.println(response);
		return response;
	}

	/* Crypto top gainers and losers */
	// https://api.tokeninsight.com/api/v1/coins/top-gainers?range=100
	// https://api.tokeninsight.com/api/v1/coins/top-gainers?range=500
	public String getCryptoTopGainersAndLosers(String top, Integer range) {
		try {
			if (top.contains("gainers")) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_TOP_GAINERS_END + "?" + "range=" + range)
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else if (top.contains("losers")) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_TOP_LOSERS_END + "?" + "range=" + range)
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else {
				responseStr = "Invalid Request !!";
			}
		} catch (Exception e) {
			log.info("Exception:-" + e.getMessage());
		}
		return responseStr;
	}

	public String getTopGainer(String country) {
		try {
			if (!country.equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.GAINERS_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = "";
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getTopLooser(String country) {
		try {
			if (!country.equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.LOOSERS_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = "";
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getUSATopGainer() {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.GAINERS_ENDPOINT + "country=" + Constant.UNITED_STATES + "&"
							+ Constant.API_KEY + Constant.API_KEY_VALUE + "&direction=gainers" + "&outputsize=50")
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getUSATopLoser() {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.LOOSERS_ENDPOINT + "country=" + Constant.UNITED_STATES + "&"
							+ Constant.API_KEY + Constant.API_KEY_VALUE + "&direction=losers" + "&outputsize=50")
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getFirstAvailableStockDate(String symbol, String country) {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.FIRST_AVAILABLE_STOCK_DATE_ENDPOINT + "symbol=" + symbol + "&"
							+ Constant.API_KEY + Constant.API_KEY_VALUE + "&country=" + country + "&interval=1day")
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	/*
	 * https://api.twelvedata.com/recommendations?symbol=MSFT&apikey=
	 * dec5774dc4ea4b21a425d1ddb0a99ff2&source=docs
	 */
	public String getAnalystRatings(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.RECOMMENDATIONS_ENDPOINT
						+ serverStock.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.RECOMMENDATIONS_ENDPOINT
						+ serverStock.getSymbol() + ".SR" + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getRatings(Stock serverStock) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.RATING_ENDPOINT + serverStock.getSymbol()
					+ "?" + Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	/*
	 * https://api.twelvedata.com/price_target?symbol=7010&apikey=
	 * dec5774dc4ea4b21a425d1ddb0a99ff2&source=docs
	 */
	// https://financialmodelingprep.com/api/v4/price-target-consensus?symbol=1030.SR&apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getPriceTarget(Stock serverStock) {
		try {
//			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
			responseStr = webClient
					.get().uri(Constant.FMPP_BASE_URL_V4 + Constant.PRICE_TARGET_ENDPOINT + "symbol="
							+ serverStock.getSymbol() + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
//			} else {
//				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.RECOMMENDATIONS_ENDPOINT
//						+ serverStock.getSymbol() + ".SR" + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
//						.exchangeToMono(response -> {
//							return response.bodyToMono(String.class);
//						}).block();
//			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//https://api.twelvedata.com/institutional_holders?symbol=1010&apikey=dec5774dc4ea4b21a425d1ddb0a99ff2&exchange=tadawul
	public String getShareHolders(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.SHARE_HOLDERS_ENDPOINT
						+ serverStock.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.SHARE_HOLDERS_ENDPOINT
						+ serverStock.getSymbol() + ".SR" + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;

	}

	public String getMarketState(String country) {
		try {
			if (!country.equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(
						Constant.BASE_URL + Constant.MARKET_STATE_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = "";
			}

			return responseStr;
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//https://api.tokeninsight.com/api/v1/coins/list/newly-listed?vs_currency=usd&limit=1500&offset=0
	public String getNewlyListed(Integer limit, Integer offset) {
		try {
			responseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_NEWLY_LISTED_ENDPOINT + "price_tracked=true"
							+ "&vs_currency=usd&" + "&limit=" + limit + "&offset=" + offset)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//	public String getIndicsSeries(IndicesSeriesRequest timeSeriesRequest, String currentDateTime, String previousDateTime,
//			String interval, ZoneId zoneId) {
	public String getIndicsSeries(TimeSeriesRequest timeSeriesRequest, String currentDateTime, String previousDateTime,
			String interval, ZoneId zoneId) {
		try {
//			responseStr = webClient.get()
//					.uri(Constant.BASE_URL + Constant.TIME_SERIES_ENDPOINT + "symbol=" + timeSeriesRequest.getSymbol()
//							+ "&" + Constant.API_KEY + Constant.API_KEY_VALUE + "&exchange="
//							+ timeSeriesRequest.getExchange() + "&outputsize=" + timeSeriesRequest.getOutputsize()
//							+ "&interval=" + interval + "&start_date=" + previousDateTime + "&end_date="
//							+ currentDateTime + "&timezone=" + zoneId)
//					.exchangeToMono(response -> {
//						return response.bodyToMono(String.class);
//					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getApiUsage() {
		try {
			responseStr = webClient.get()
					.uri(Constant.BASE_URL + Constant.API_USAGE_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getKeyExecutive(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.KEY_EXECUTIVE_ENDPOINT
						+ serverStock.getSymbol() + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get().uri(Constant.BASE_URL + Constant.KEY_EXECUTIVE_ENDPOINT
						+ serverStock.getSymbol() + ".SR" + "?" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}

		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getDividends(String country) {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.DIVIDENDS_ENDPOINT + "country=" + country
					+ "&" + Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// https://api.tokeninsight.com/api/v1/exchanges/list?cex=false&dex=false&spot=false&derivatives=false&limit=1500&offset=0
	public String getCryptoExchangeList(Integer limit, Integer offset) {
		try {
			responseStr = webClient.get()
					.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_EXCHANGE_LIST_ENDPOINT + "&" + Constant.TS_LIMIT
							+ limit + "&" + Constant.TS_OFFSET + offset)
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
//			log.error("Exception : " + e.getMessage());
			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;

	}

	// https://api.tokeninsight.com/api/v1/coins/ethereum/markets?limit=900&offset=0
	public String getTradingPair(String crytpoId, Integer limit, Integer offset) {
		String responseStr = null;
//			String ff="https://api.tokeninsight.com/api/v1/coins/"+crytpoId+"/markets?limit=900&offset=0";
		try {
//				responseStr = webClient.get()
//						.uri(Constant.TS_BASE_URL +"/api/v1/coins/" + crytpoId+"/markets?" + Constant.TS_LIMIT
//								+ limit + "&" + Constant.TS_OFFSET + offset )
//						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
			responseStr = webClient.get()
					.uri("https://api.tokeninsight.com/api/v1/coins/" + crytpoId + "/markets?limit=900&offset=0")
					.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class).block();
		} catch (Exception e) {
//				log.error("Exception : " + e.getMessage());
			System.out.println("Exception :" + e.getMessage());
		}
		return responseStr;

	}

	// https://financialmodelingprep.com/api/v3/stock_news?page=0&apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp&tickers=A&limit=10
	public String get_usa_stock_news(String symbol) {
		String responseStr = null;
		int page = 0, limit = 10;
		try {
			responseStr = webClient.get()
					.uri(Constant.STOCK_NEWS_ENDPOINT + Constant.PAGE + page + "&" + Constant.API_KEY
							+ Constant.API_KEY_VALUE + "&tickers=" + symbol + "&" + Constant.TS_LIMIT + limit)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// https://financialmodelingprep.com/api/v3/stock_dividend_calendar?apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getDividend() {
		try {
			responseStr = webClient.get().uri(Constant.BASE_URL + Constant.DIVIDENDS_CALENDAR_ENDPOINT
					+ Constant.API_KEY + Constant.API_KEY_VALUE).exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//https://financialmodelingprep.com/api/v3/earning_calendar?apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getEarning() {
		try {
			responseStr = webClient.get().uri(
					Constant.BASE_URL + Constant.EARNING_CALENDAR_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// https://financialmodelingprep.com/api/v3/economic_calendar?apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getEconomic() {
		try {
			responseStr = webClient.get().uri(
					Constant.BASE_URL + Constant.ECONOMIC_CALENDAR_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

//https://financialmodelingprep.com/api/v3/ipo_calendar?apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getIpos() {
		try {
			responseStr = webClient.get().uri(
					Constant.BASE_URL + Constant.IPOS_CALENDAR_ENDPOINT + Constant.API_KEY + Constant.API_KEY_VALUE)
					.exchangeToMono(response -> {
						return response.bodyToMono(String.class);
					}).block();
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// financialmodelingprep.com/api/v3/key-metrics/AAPL?period=quarter&apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getStockStatistics1(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.STATISTICS_SECOND_ENDPOINT + serverStock.getSymbol()
								+ "?period=quarter&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.STATISTICS_SECOND_ENDPOINT + serverStock.getSymbol() + ".SR"
								+ "?period=quarter&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	// https://financialmodelingprep.com/api/v3/ratios/AAPL?period=quarter&apikey=PDuMxtFxjktPz2IEsT6Ip2DF0SYS2dvp
	public String getStockStatistics2(Stock serverStock) {
		try {
			if (!serverStock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.STATISTICS_THIRD_ENDPOINT + serverStock.getSymbol()
								+ "?period=quarter&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.STATISTICS_THIRD_ENDPOINT + serverStock.getSymbol() + ".SR"
								+ "?period=quarter&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	public String getSecFilingData(Stock stock, String filingType) {
		try {
			if (!stock.getCountry().equalsIgnoreCase(Constant.SAUDI_ARABIA)) {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.SEC_FILING_ENDPOINT + stock.getSymbol() + "?" + "type="
								+ filingType + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			} else {
				responseStr = webClient.get()
						.uri(Constant.BASE_URL + Constant.SEC_FILING_ENDPOINT + stock.getSymbol() + ".SR" + "?"
								+ "type=" + filingType + "&" + Constant.API_KEY + Constant.API_KEY_VALUE)
						.exchangeToMono(response -> {
							return response.bodyToMono(String.class);
						}).block();
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	/*
	 * https://api.tokeninsight.com/api/v1/history/coins/bitcoin?interval=minute&
	 * length=1440&vs_currency=usd
	 */
	public String getCryptoGraphData(String cryptoId,String type) {
		String minute = "minute", hour = "hour", day = "day";
		Integer one_minute_length = 1440, one_week_length = 10080, one_month_length = 720,three_month_length = 2160, six_month_length = 4320,
				one_year_length = 8764, five_year_length = -1;//five_year_length = 365;
		try {
			if (type.equalsIgnoreCase(Constant.ONE_DAY)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT+ cryptoId+"?interval="+ minute
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.MinutesInADay() + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else if (type.equalsIgnoreCase(Constant.ONE_WEEK)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+minute
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.MinutesInAWeek() + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else if (type.equalsIgnoreCase(Constant.ONE_MONTH)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+hour
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.HoursInAMonth() + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			}else if (type.equalsIgnoreCase(Constant.THREE_MONTH)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+hour
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.HoursInAThreeMonth(2)+ "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			}else if (type.equalsIgnoreCase(Constant.SIX_MONTH)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+hour
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.HoursInAThreeMonth(5) + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else if (type.equalsIgnoreCase(Constant.ONE_YEAR)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+hour
								+ Constant.TS_CRYPTO_LENGTH + DateUtil.HoursInAOneYear() + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else if (type.equalsIgnoreCase(Constant.FIVE_YEAR)) {
				responseStr = webClient.get()
						.uri(Constant.TS_BASE_URL + Constant.TS_CRYPTO_GRAPH_DATA_ENDPOINT + cryptoId+"?interval="+day
								+ Constant.TS_CRYPTO_LENGTH + five_year_length + "&vs_currency=usd")
						.header(Constant.TS_API_KEY, Constant.TS_API_KEY_VALUE).retrieve().bodyToMono(String.class)
						.block();
			} else {
				return "Invalid interval time !!";
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return responseStr;
	}

	
	

}
