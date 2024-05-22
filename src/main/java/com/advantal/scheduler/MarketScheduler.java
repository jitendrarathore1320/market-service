package com.advantal.scheduler;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import com.advantal.backgroundtask.MoverTask;
import com.advantal.model.Country;
import com.advantal.repository.CountryRepository;
import com.advantal.serviceimpl.CryptoDataProcessUtil;
import com.advantal.serviceimpl.DataProcessUtil;
import com.advantal.serviceimpl.MarketDataProcessUtil;
import com.advantal.serviceimpl.NewsDataProcessUtil;
import com.advantal.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarketScheduler {

	@Autowired
	DataProcessUtil dataProcessUtil;

	@Autowired
	MarketDataProcessUtil marketDataProcessUtil;

	@Autowired
	NewsDataProcessUtil newsDataProcessUtil;

	@Autowired
	CryptoDataProcessUtil cryptoDataProcessUtil;

	@Autowired
	CountryRepository countryRepository;

//	@Autowired
//	private MoverTask moverTask;

	/*
	 * Intervals: 5Min=300000, 15Min=900000, 30min=1800000, 1Hr=3600000,
	 * 2.5Hr=9000000, 24Hr=86400000 *
	 */

	/* Scheduler - 1 : Working */
//	@Scheduled(cron = "0 30 02 * * *")
	public void saveStockAndCryptoMarketData() {
		log.info(
				"Scheduler Name : saveStockAndCryptoMarketData() | Task name : To update all market data related to stock and Crypto !!");
		dataProcessUtil.marketThread();
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 2 : Working */
//	@Scheduled(fixedRate = 60000)
	public void saveStockGainerLoser() {
		log.info("Scheduler name : saveStockGainerLoser() | Task name : Update stock Gainer/Loser list");
		List<Country> countryList = countryRepository.findAll();
		if (!countryList.isEmpty()) {
			dataProcessUtil.gainerLoserThread(countryList);
		} else {
			log.info("Country list Empty !!");
		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 3 : Working */
//	@Scheduled(fixedRate = 60000)
	public void saveIndicesMarketData() {
		log.info("Scheduler name : saveIndicesMarketData() | Task name : Update Saudi and USA Indices Market Data");
		List<Country> countryList = countryRepository.findAll();
		if (!countryList.isEmpty()) {
			dataProcessUtil.indicesThread(countryList);
		} else {
			log.info("Country list Empty !!");
		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 4 : Working */
//	@Scheduled(cron = "0 0/15 * * * *")
	public void saveMarketData() {
		log.info("Scheduler name : saveMarketData() | Task name : Update Master stock List with market data");
		List<Country> countryList = countryRepository.findAll();
		if (!countryList.isEmpty()) {
			marketDataProcessUtil.saveMarketData(countryList);
		} else {
			log.info("Country list Empty !!");
		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 5 : Working */
//	@Scheduled(cron = "0 0 19,03 * * *")
	public void retryToSaveMarketData() {
		log.info("Scheduler name : retryToSaveMarketData() | Task name : Retry to update market data");
		List<Country> countryList = countryRepository.findAll();
		if (!countryList.isEmpty()) {
			marketDataProcessUtil.retryThread(countryList);
		} else {
			log.info("Country list Empty !!");
		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 6 : Working */
//	@Scheduled(cron = "0 0/15 * * * *")
//	@PostConstruct
	public void saveGlobalNewsFromBrowsAi() {
		log.info("Scheduler name : saveGlobalNewsFromBrowsAi() | Task name : Update News from third party");
		newsDataProcessUtil.newsFormFmpThread();
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 7 : Working */
//	@Scheduled(cron = "0 0 5,8,11,14,17,20,23 * * *")
	public void SaveStockNewsFormFmp() {
		List<Country> countryList = countryRepository.findAll();
		if (!countryList.isEmpty()) {
			newsDataProcessUtil.newsFormFmpThread(countryList);
		} else {
			log.info("Country list Empty !!");
		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 8 : Working */
//	@Scheduled(cron = "0 0 * * * *")
	public void saveSukukIndicesData() {
		log.info("Scheduler name : SaveSukukIndicesData | Task name : Update Sukuk Indices Data from third party");
		newsDataProcessUtil.IndicesFromBrowsThread();
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 9 : Working */
//	@PostConstruct
	public void saveIndicesGraphDataForSaudi() {
		log.info(
				"Scheduler name : SaveIndicesGraphData For Saudi | Task name : Update Indices Graph Data from third party");
		dataProcessUtil.indicesGraphForSaudiThread();
//		List<Country> countryList = countryRepository.findAll();
//		if (!countryList.isEmpty()) {
//			dataProcessUtil.saveIndicesAndStocksGraphData(countryList);
//		} else {
//			log.info("Country list Empty !!");
//		}
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 10 : Working */
//	@PostConstruct
	public void saveIndicesGraphDataForUnitesStated() {
		log.info(
				"Scheduler name : SaveIndicesGraphData For United States | Task name : Update Indices Graph Data from third party");
		dataProcessUtil.indicesGraphForUnitedStatesThread();

	}

	/* Scheduler - 11 : Not Working */
	public void SaveSecFiling() {
		log.info("Scheduler name : SaveSecFiling() | Task name : Update SEC Filing data");
		dataProcessUtil.secFilingThread(); /* Update SEC Filing data */
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 12 : Not Working */
//	@PostConstruct
	public void saveStockYearGraphData() {
		log.info("Scheduler name : SaveStockYearGraphData | Task name : Update Stock Year Graph Data from third party");
		dataProcessUtil.yearGraphThread();
		log.info("<<<--- Done --->>>");
	}

	/* Scheduler - 13 : Not Working */
//	@Scheduled(cron = "0 0 * * * *")
	public void SaveExploreMarketData() {
		log.info("Scheduler name : SaveExploreMarketData | Task name : Update Explore Market Data from third party");
//		dataProcessUtil.saveDividendMarketData();
//		dataProcessUtil.saveEarningMarketData();
//		dataProcessUtil.saveEconomicMarketData();
//		dataProcessUtil.saveIposMarketData();
		log.info("<<<--- Done --->>>");

	}

}
