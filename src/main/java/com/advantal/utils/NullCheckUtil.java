package com.advantal.utils;

import java.util.Objects;
import java.util.stream.Stream;

import com.advantal.responsepayload.ExchangeDetailsResponse;
import com.advantal.responsepayload.StockDetailsResponse;
import com.advantal.responsepayload.StockProfileResponse;

public class NullCheckUtil {
//	public static boolean isStockDetailsNull(StockDetailsResponse stockDetailsResponse) {
//		return Stream.of(stockDetailsResponse.getSymbol(), stockDetailsResponse.getName(),
//				stockDetailsResponse.getExchange(), stockDetailsResponse.getCurrency(), stockDetailsResponse.getOpen(),
//				stockDetailsResponse.getPrevious_close(), stockDetailsResponse.getVolume(),
//				stockDetailsResponse.getAverage_volume(), stockDetailsResponse.getChange(),
//				stockDetailsResponse.getPercent_change(), stockDetailsResponse.getHigh(), stockDetailsResponse.getLow(),
//				stockDetailsResponse.getClose(), stockDetailsResponse.is_market_open(),
//				stockDetailsResponse.getDatetime(), stockDetailsResponse.getStatistics(),
//				stockDetailsResponse.getFifty_two_week(), stockDetailsResponse.getLogo().getUrl())
//				.allMatch(Objects::isNull);
//	}

//	public static boolean isProfileNull(StockProfileResponse stockProfileResponse) {
//		return Stream
//				.of(stockProfileResponse.getSector(), stockProfileResponse.getCEO(), stockProfileResponse.getCountry(),
//						stockProfileResponse.getEmployees(), stockProfileResponse.getIndustry())
//				.allMatch(Objects::isNull);
//	}

//	public static boolean isExchangeDetailsNull(ExchangeDetailsResponse exchangeDetailsResponse) {
//		return Stream.of(exchangeDetailsResponse.getChange(), exchangeDetailsResponse.getDatetime(),
//				exchangeDetailsResponse.getFifty_two_week(), exchangeDetailsResponse.getHigh(),
//				exchangeDetailsResponse.getLow(), exchangeDetailsResponse.getName(), exchangeDetailsResponse.getOpen(),
//				exchangeDetailsResponse.getPercent_change(), exchangeDetailsResponse.getPrevious_close(),
//				exchangeDetailsResponse.getSymbol(), exchangeDetailsResponse.getVolume()).allMatch(Objects::isNull);
//	}

}
