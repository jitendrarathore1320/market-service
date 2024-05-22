package com.advantal.utils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.advantal.model.Assets;
import com.advantal.model.Country;
import com.advantal.model.Crypto;
import com.advantal.model.Indices;
import com.advantal.model.Stock;
import com.advantal.model.StockProfile;
import com.advantal.requestpayload.TimeSeriesRequest;
import com.advantal.responsepayload.KeyValueGlobalResponse;
import com.advantal.responsepayload.KeyValueResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MethodUtil {

	@Autowired
	private ThirdPartyApiUtil thirdPartyApiUtil;

	public static List<KeyValueResponse> indicesMarketData(Indices mapResponse) {
		List<KeyValueResponse> keyValueResponseList = new ArrayList<>();
		try {
//			String[] key = { "PREV. CLOSE", "OPEN", "DAY RANGE", "LAST 52 WEEK RANGE", "NO. OF TRADES",
//					"AVG. TRADE SIZE*", "VOLUME TRADED", "VALUE TRADED", "31 DEC. VALUE", "YTD", "% CHANGE",
//					"1D Return" };
//			String[] value = { previous_close, open, high + " - " + low, ftw_range, "null", "null", "null", "null",
//					"null", "null", percent_change, close };

			String[] key = { "CLOSE", "PREV. CLOSE", "OPEN", "DAY RANGE", "LAST 52 WEEK RANGE", "% CHANGE", "1D Return",
					"VOLUME" };
			String[] value = { mapResponse.getClose(), mapResponse.getPreviousClose(), mapResponse.getOpen(),
					mapResponse.getHigh() + " - " + mapResponse.getLow(),
					mapResponse.getFtw_low() + " - " + mapResponse.getFtw_high(), mapResponse.getPercent_change(),
					mapResponse.getPrice_change(), mapResponse.getVolume() };

			for (int i = 0; i < key.length; i++) {
				KeyValueResponse keyValue = new KeyValueResponse();
				keyValue.setKey(key[i]);
				keyValue.setValue(value[i]);
				keyValueResponseList.add(keyValue);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponseList;
	}

	public static List<KeyValueResponse> cryptoProfileMarketData(Object circulating_supply, Object market_cap,
			Object vol_spot_24h, Object max_supply, Object fully_diluted_valuation, String rank) {
		Map<String, Object> map = new HashMap<>();
		List<KeyValueResponse> keyValueResponsesList = new ArrayList<>();
		try {
			String[] key = { "Market Cap", "Fully Diluted Valuation", "Volume (24h)", "Circulating Supply",
					"Max Supply", "Rank" };
			Object[] value = { market_cap, fully_diluted_valuation, vol_spot_24h, circulating_supply, max_supply,
					"#" + rank };
			for (int i = 0; i <= 5; i++) {
				KeyValueResponse keyValueResponse = new KeyValueResponse();
				keyValueResponse.setKey(key[i]);
				keyValueResponse.setValue(value[i]);
				keyValueResponsesList.add(keyValueResponse);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponsesList;
	}

	public String calculatePercentageValue(Double crypto_price, Double percentage_chnge_value) {
		// Calculate the price value of the percentage change
//		Double price = Double.parseDouble(crypto_price);
//		Double percentChange = Double.parseDouble(percentage_chnge_value);
//		Double priceValueOfPercentage = (crypto_price * percentage_chnge_value) / 100;
		Double priceValueOfPercentage = (crypto_price * percentage_chnge_value);

		// Format the values with decimal formatting
		DecimalFormat decimalFormat = new DecimalFormat("#.###############");
		String change_value = decimalFormat.format(priceValueOfPercentage);
//		return Double.parseDouble(change_value);
		return change_value;

	}

	public static List<KeyValueResponse> cryptoAnalystRatingMarketData(String team_partners_investors,
			String token_economics, String underlying_technology_security, String roadmap_progress,
			String token_performance, String ecosystem_development) {
		Map<String, Object> map = new HashMap<>();
		List<KeyValueResponse> keyValueResponseRatingList = new ArrayList<>();
		try {
			String[] key = { "Roadmap & Progress", "Underlying Technology & Security", "Token Performance",
					"Ecosystem Development", "Team Partners & Investors", "Token Economics" };
			String[] value = { roadmap_progress, underlying_technology_security, token_performance,
					ecosystem_development, team_partners_investors, token_economics };
			for (int i = 0; i <= 5; i++) {
				KeyValueResponse keyValueResponse = new KeyValueResponse();
				keyValueResponse.setKey(key[i]);
				keyValueResponse.setValue(value[i]);
				keyValueResponseRatingList.add(keyValueResponse);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponseRatingList;
	}

	public static List<KeyValueGlobalResponse> cryptoGlobalStatus(String total_market_cap, String spot_volume_24H,
			String btc, Double market_percentage, Double volume_percentage, String btc_change_percentage) {
		Map<String, Object> map = new HashMap<>();
		List<KeyValueGlobalResponse> keyValueResponsesList = new ArrayList<>();
		try {
			Map<String, KeyValueGlobalResponse> keyValueMap = new HashMap<>();
			KeyValueGlobalResponse globalResponse = new KeyValueGlobalResponse();
			globalResponse.setValue(total_market_cap);
			globalResponse.setParcentage(market_percentage + "%");
			keyValueMap.put("MARKET CAP", globalResponse);
			globalResponse = new KeyValueGlobalResponse();
			globalResponse.setValue(spot_volume_24H);
			globalResponse.setParcentage(volume_percentage + "%");
			keyValueMap.put("24H VOLUME", globalResponse);
			globalResponse = new KeyValueGlobalResponse();
			globalResponse.setValue(btc + "%");
			globalResponse.setParcentage(btc_change_percentage + "%");
//			globalResponse.setParcentage("%");
			keyValueMap.put("BTC DOMINANCE", globalResponse);
			for (Map.Entry<String, KeyValueGlobalResponse> entry : keyValueMap.entrySet()) {
				KeyValueGlobalResponse keyValueResponse = new KeyValueGlobalResponse();
				keyValueResponse.setKey(entry.getKey());
				keyValueResponse.setValue(entry.getValue().getValue());
				keyValueResponse.setParcentage(entry.getValue().getParcentage());
				keyValueResponsesList.add(keyValueResponse);
			}
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, e.getMessage());
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponsesList;
	}

	public String getTimeSeries(TimeSeriesRequest timeSeriesRequest, Country country, int minusTime,
			Boolean isMarketOpen) {
		String timeSeries = "", currentDateTime = "", previousDateTime = "";
		try {
			ZoneId zoneId = ZoneId.of(country.getTimeZone());
//			int day = isMarketOpen == true ? 1 : 2;

//			if (timeSeriesRequest.getInstrumentType().equalsIgnoreCase("stock")) {

//			currentDateTime = DateUtil.getCurrentDateTimeInZoneWise(zoneId);

//				if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_HOUR)) {
//					previousDateTime = DateUtil.getCurrentDateTimeToOneHourDiffInZoneWise(zoneId);
//					timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//							Constant.ONE_MINUTE, zoneId);
//				} else 
//			if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_DAY)) {

//				previousDateTime = DateUtil.getOneDayPreviousDateTimeInZoneWise(zoneId, minusTime);

//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_MINUTE, zoneId);
//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_MINUTE, zoneId, isMarketOpen);
//			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_WEEK)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneWeekDiffInZoneWise(zoneId);
//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_HOUR, zoneId, isMarketOpen);
//			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_MONTH)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneMonthDiffInZoneWise(zoneId);
//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_DAY, zoneId, isMarketOpen);
//			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneYearDiffInZoneWise(zoneId);
//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_MONTH, zoneId, isMarketOpen);
//			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
//				int year = 100;
//				previousDateTime = DateUtil.getCurrentDateTimeToStartDateTimeOfInstrumentDiffInZoneWise(zoneId, year);
//				timeSeries = thirdPartyApiUtil.getTimeSeries(timeSeriesRequest, currentDateTime, previousDateTime,
//						Constant.ONE_MONTH, zoneId, isMarketOpen);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeSeries;
	}

	public static List<KeyValueResponse> getMarketState(Map<?, ?> mapResponse) {
		List<KeyValueResponse> keyValueResponsesList = new ArrayList<>();
		try {
			String is_market_open, time_to_open;
			is_market_open = mapResponse.get("is_market_open").toString();
			time_to_open = mapResponse.get("time_to_open").toString();

//			Map<String, String> keyValueMap = new HashMap<>();
//			keyValueMap.put("OPEN", open);
//			keyValueMap.put("HIGH", high);
//
//			for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
//				KeyValueResponse keyValueResponse = new KeyValueResponse();
//				keyValueResponse.setKey(entry.getKey());
//				keyValueResponse.setValue(entry.getValue());
//				keyValueResponsesList.add(keyValueResponse);
//			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponsesList;
	}

	// convert string values to k,m,b,t
	public String convertToAbbreviation(long value) {
		String[] suffixes = new String[] { "", "K", "M", "B", "T" };

		int suffixIndex = 0;
		double adjustedValue = value;

		while (adjustedValue >= 1000 && suffixIndex < suffixes.length - 1) {
			adjustedValue /= 1000.0;
			suffixIndex++;
		}
		return String.format("%.2f%s", adjustedValue, suffixes[suffixIndex]);
	}

	// convert string values to k,m,b,t
	public String convertToAbbreviation(Double value) {
		String[] suffixes = new String[] { "", "K", "M", "B", "T" };

		int suffixIndex = 0;
		double adjustedValue = value;

		while (adjustedValue >= 1000 && suffixIndex < suffixes.length - 1) {
			adjustedValue /= 1000.0;
			suffixIndex++;
		}
		return String.format("%.2f%s", adjustedValue, suffixes[suffixIndex]);
	}

	public String formattedValues(Double value) {
		// Create a DecimalFormat instance to display without scientific notation
		DecimalFormat decimalFormat = new DecimalFormat("0.###############");

		// Format the original value without scientific notation
		String formattedPriceValue = decimalFormat.format(value);
//		Double result = Double.parseDouble(formattedPriceValue);
//		result = Math.round(result * 10000.0) / 10000.0;
		return formattedPriceValue;
	}

	public Double formattedValuesForPercentage(Double value) {
		// Create a DecimalFormat instance to display without scientific notation
		DecimalFormat decimalFormat = new DecimalFormat("0.##");

		// Format the original value without scientific notation
		String formattedPriceValue = decimalFormat.format(value);
		Double result = Double.parseDouble(formattedPriceValue);
		result = Math.round(result * 10000.0) / 10000.0;
		return result;
	}

//	public String getIndicesSeries(IndicesSeriesRequest timeSeriesRequest, Country country) {
	public String getIndicesSeries(TimeSeriesRequest timeSeriesRequest, Country country) {
		String timeSeries = "", currentDateTime = "", previousDateTime = "";
		try {
			ZoneId zoneId = null;
			zoneId = ZoneId.of(country.getTimeZone());
			currentDateTime = DateUtil.getCurrentDateTimeInZoneWise(zoneId);
			LocalDateTime dateTime = LocalDateTime.now(zoneId);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:00 a");
			if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_DAY)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneDayDiffInZoneWise(zoneId);
				dateTime = dateTime.minusDays(1);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_HOUR, zoneId);
			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_WEEK)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneWeekDiffInZoneWise(zoneId);
				dateTime = dateTime.minusWeeks(1);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_DAY, zoneId);
			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_MONTH)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneMonthDiffInZoneWise(zoneId);
				dateTime = dateTime.minusMonths(1);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_DAY, zoneId);
			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.SIX_MONTH)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToSixMonthDiffInZoneWise(zoneId);
				dateTime = dateTime.minusMonths(6);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_MONTH, zoneId);
			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ONE_YEAR)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToOneYearDiffInZoneWise(zoneId);
				dateTime = dateTime.minusYears(1);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_MONTH, zoneId);
			} else if (timeSeriesRequest.getInterval().equalsIgnoreCase(Constant.ALL)) {
//				previousDateTime = DateUtil.getCurrentDateTimeToStartDateTimeOfInstrumentDiffInZoneWise(zoneId);
				dateTime = dateTime.minusYears(100);
				timeSeries = thirdPartyApiUtil.getIndicsSeries(timeSeriesRequest, currentDateTime,
						dateTime.format(formatter), Constant.ONE_MONTH, zoneId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeSeries;
	}

	public static List<KeyValueResponse> marketData(Stock stockStatisticsResponse) {
		List<KeyValueResponse> keyValueResponseList = new ArrayList<>();
		try {
//			String[] key = { "Close price", "Market Value", "Paid Capital", "Multiplier of the book value",
//					"Earning per share", "PE", "Next distribution date", "Last distribution date",
//					"Yearly distribution percent", "issued shares" };
//			String[] value = { stockStatisticsResponse.getPrice().toString(),
//					stockStatisticsResponse.getMarketCap().toString(), "", "",
//					stockStatisticsResponse.getEps().toString(), stockStatisticsResponse.getPe().toString(), "", "", "",
//					"" };

			String[] key = { "Last Trade", "Change", "Change(%)", "Open", "High", "Low", "Prev Close", "24 Hour Range",
					"52 wk Range", "Volume", "Turnover", "Market Value", "Shared Outstanding", "Free Float",
					"Earning per share", "PE" };
			String[] value = { stockStatisticsResponse.getPrice().toString(),
					stockStatisticsResponse.getPrice_change().toString(),
					stockStatisticsResponse.getPercent_change().toString(),
					stockStatisticsResponse.getOpen().toString(), stockStatisticsResponse.getHigh().toString(),
					stockStatisticsResponse.getLow().toString(), stockStatisticsResponse.getPreviousClose().toString(),
					stockStatisticsResponse.getLow() + " - " + stockStatisticsResponse.getHigh(),
					stockStatisticsResponse.getFtw_low() + " - " + stockStatisticsResponse.getFtw_high(),
					stockStatisticsResponse.getVolume().toString(), "",
					stockStatisticsResponse.getMarketCap().toString(),
					stockStatisticsResponse.getSharesOutstanding().toString(), "",
					stockStatisticsResponse.getEps().toString(), stockStatisticsResponse.getPe().toString() };
			for (int i = 0; i < key.length; i++) {
				KeyValueResponse keyValue = new KeyValueResponse();
				keyValue.setKey(key[i]);
				keyValue.setValue(value[i]);
				keyValueResponseList.add(keyValue);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponseList;
	}

	public static List<KeyValueResponse> companyProfileData(StockProfile stockProfileResponse) {
		List<KeyValueResponse> keyValueResponseList = new ArrayList<>();
		try {

//			String[] key = { "COUNTRY", "NUMBER OF EMPLOYEES", "WEBSITE", "EXCHANGE", "IPO DATE", "SEDOL NUMBER",
//					"ISIN NUMBER", "CUSIP NUMBER", "NAICS", "NAICS SECTOR", "NAICS SUBSECTOR" };
//			Object[] value = { stockProfileResponse.getCountry(), stockProfileResponse.getNoOfEmployees(),
//					stockProfileResponse.getWebsite(), stockProfileResponse.getExchange(),
//					stockProfileResponse.getIpoDate(), "", stockProfileResponse.getIsin(),
//					stockProfileResponse.getCusip(), "", "", "" };

			String[] key = { "COUNTRY", "NUMBER OF EMPLOYEES", "WEBSITE", "EXCHANGE", "IPO DATE", "ISIN NUMBER",
					"CUSIP NUMBER", "COMPANY NAME", "INDUSTRY", "SECTOR", "PHONE", "ADDRESS", "CITY", "STATE", "ZIP" };
			Object[] value = { stockProfileResponse.getCountry(), stockProfileResponse.getNoOfEmployees(),
					stockProfileResponse.getWebsite(), stockProfileResponse.getExchange(),
					stockProfileResponse.getIpoDate(), stockProfileResponse.getIsin(), stockProfileResponse.getCusip(),
					stockProfileResponse.getCompanyName(), stockProfileResponse.getIndustry(),
					stockProfileResponse.getSector(), stockProfileResponse.getPhone(),
					stockProfileResponse.getAddress(), stockProfileResponse.getCity(), stockProfileResponse.getState(),
					stockProfileResponse.getZip() };

			for (int i = 0; i < key.length; i++) {
				KeyValueResponse keyValue = new KeyValueResponse();
				keyValue.setKey(key[i]);
				keyValue.setValue(value[i]);
				keyValueResponseList.add(keyValue);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}
		return keyValueResponseList;
	}

	public static List<KeyValueResponse> getAdvanceMatrix(Stock stock, Assets oldAssets) {
		List<KeyValueResponse> keyValueAdvancMatrixList = new ArrayList<>();
		try {
			Double quantity = 0.0, marketValue = 0.0, totalPriceGain = 0.0, totalPercentageGain = 0.0, price = 0.0,
					prevClose = 0.0;
			quantity = oldAssets.getQuantity() != null ? oldAssets.getQuantity() : 0.0;
			price = stock.getPrice() != null ? stock.getPrice() : 0.0;
			marketValue = quantity * price;
			prevClose = oldAssets.getPreviousClosePrice() != null ? oldAssets.getPreviousClosePrice() : 0.0;
			totalPriceGain = marketValue - prevClose;
			if (prevClose != 0.0) {
				totalPercentageGain = ((totalPriceGain / prevClose) * 100);
			}
			String[] key = { "Owned", "Market Value", "Total Gains", "Total Cost Basis", "% Change" };
			Object[] value = { quantity, marketValue, totalPriceGain,
					oldAssets.getTotalPrice() != null ? oldAssets.getTotalPrice() : 0.0, totalPercentageGain };
			for (int i = 0; i < key.length; i++) {
				KeyValueResponse keyValue = new KeyValueResponse();
				keyValue.setKey(key[i]);
				keyValue.setValue(value[i]);
				keyValueAdvancMatrixList.add(keyValue);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}

		return keyValueAdvancMatrixList;
	}

	public static List<KeyValueResponse> getAdvanceMatrix(String marketPrice, Assets oldAssets) {
		List<KeyValueResponse> keyValueAdvancMatrixList = new ArrayList<>();
		try {
			Double quantity = 0.0, marketValue = 0.0, totalPriceGain = 0.0, totalPercentageGain = 0.0, price = 0.0,
					prevClose = 0.0;
			price = Double.parseDouble(marketPrice);
			quantity = oldAssets.getQuantity() != null ? oldAssets.getQuantity() : 0.0;
			marketValue = quantity * price;
			prevClose = oldAssets.getPreviousClosePrice() != null ? oldAssets.getPreviousClosePrice() : 0.0;
			totalPriceGain = marketValue - prevClose;
			if (prevClose != 0.0) {
				totalPercentageGain = ((totalPriceGain / prevClose) * 100);
			}

			String[] key = { "Owned", "Market Value", "Total Gains", "Total Cost Basis", "% Change" };
			Object[] value = { quantity, marketValue, totalPriceGain,
					oldAssets.getTotalPrice() != null ? oldAssets.getTotalPrice() : 0.0, totalPercentageGain };
			for (int i = 0; i < key.length; i++) {
				KeyValueResponse keyValue = new KeyValueResponse();
				keyValue.setKey(key[i]);
				keyValue.setValue(value[i]);
				keyValueAdvancMatrixList.add(keyValue);
			}
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
		}

		return keyValueAdvancMatrixList;
	}

}
