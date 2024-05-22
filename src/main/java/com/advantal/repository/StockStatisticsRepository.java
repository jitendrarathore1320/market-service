package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.StockStatistics;

@Repository
public interface StockStatisticsRepository extends JpaRepository<StockStatistics, Long> {

	StockStatistics findBySymbol(String symbol);

	StockStatistics findBySymbolAndStatus(String symbol, Short status);




}
