package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.StockRecommendation;

@Repository
public interface StockRecommendationRepository extends JpaRepository<StockRecommendation, Long> {

	StockRecommendation findBySymbolAndCountry(String symbol, String country);

	StockRecommendation findBySymbol(String symbol);

	StockRecommendation findBySymbolAndStatus(String symbol, Short one);

}
