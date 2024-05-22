package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.StockPriceTarget;
import com.advantal.model.StockRecommendation;

@Repository
public interface StockPriceTargetRepository extends JpaRepository<StockPriceTarget, Long> {

	StockPriceTarget findBySymbolAndCountry(String symbol, String country);

	StockPriceTarget findBySymbol(String symbol);

}
